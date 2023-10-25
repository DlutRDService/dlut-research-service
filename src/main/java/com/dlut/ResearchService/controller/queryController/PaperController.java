package com.dlut.ResearchService.controller.queryController;

import com.alibaba.fastjson.JSONArray;
import com.dlut.ResearchService.utils.RedisUtils;
import com.dlut.ResearchService.utils.ScriptTriggerUtils;
import com.dlut.ResearchService.utils.StringUtils;
import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.redis.RedisKey;
import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.entity.dto.PaperDto;
import com.dlut.ResearchService.service.impl.MilvusServiceImpl;
import com.dlut.ResearchService.service.impl.PaperServiceImpl;
import com.dlut.ResearchService.entity.constants.Result;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import jakarta.annotation.Resource;
import com.dlut.ResearchService.annotation.log;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/paper")
public class PaperController {
    @Resource
    private PaperServiceImpl paperService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MilvusServiceImpl milvusService;
    @Resource
    private ResultBuilder resultBuilder;

    /**
     * 启动Milvus导入脚本，首先查询Mysql中的数据，根据数据变动情况导入向量
     */
    @GetMapping("updateVector")
    public Result updateVector() {
        HashMap<Integer, String> hashMap = paperService.selectTitleAndId();
        for (Integer key : hashMap.keySet()) {
            ScriptTriggerUtils.execute("importMilvus.py", hashMap.get(key), String.valueOf(key));
        }
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "向量导入完毕", null);
    }

    /**
     * 高级检索功能
     * @param queryField 检索字段
     */
    @log
    @GetMapping("/advanced-search")
    public Result searchByKeywords(@RequestParam String queryField) {
        //current = (current-1) * pageSize;
        try {
            // 字符串转化为小写
            queryField = queryField.toLowerCase();
            // 替换中文字符
            queryField = queryField.replace('（', ')');
            queryField = queryField.replace('）', '(');
            queryField = queryField.replace('：', ':');
            queryField = queryField.replace('；', ';');
            queryField = queryField.replace('，', ',');
            queryField = queryField.replace('。', '.');
        } catch (Exception e) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        StringUtils str = new StringUtils();
        // 判断是否含有字符,以及含有=。
        if (!str.containNumOrChar(queryField) || !queryField.contains("=")) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        // 检查索引式是否符合要求,例如“AF=”或者“ AF=”，索引字段前不允许含有字母与数字
        if (!queryField.matches(Regex.FORMAT_QUERY)) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        //判断是否正确使用布尔操作符，检查不正确使用布尔操作符的情况
        if (queryField.matches(Regex.FALSE_BOOLEAN_FORMAT)) {
            return resultBuilder.build(StatusCode.STATUS_CODE_400, "Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        if (queryField.matches(Regex.MATCH_MULTI_CONDITION)) {
            HashMap<String, Integer> map = paperService.singleSetQueryFieldProcess(queryField);
            List<PaperDto> paperDtoList = paperService.singleSetQueriesProcess(map);
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "", paperDtoList);
        } else {
            Set<Integer> map1 = paperService.multiSetQueryFieldProcess(queryField);
            List<Integer> idlist = new ArrayList<>(map1);
            List<PaperDto> paperDtoList = paperService.selectPapersByIdList(idlist);
            return resultBuilder.build(StatusCode.STATUS_CODE_200, "", paperDtoList);
        }
    }

    @GetMapping("full-record/{id}")
    public Result paperInformation(@PathVariable Integer id) {
        ArrayList<Integer> idList = new ArrayList<>();
        idList.add(id);
        RedisUtils<List<Float>> redisUtils = new RedisUtils<>();
        List<Float> vector = redisUtils.getHashValue(RedisKey.REDIS_KEY_TEMP_PAPER_VECTOR, String.valueOf(id));
        R<SearchResults> result = milvusService.queryBySimilarity(vector, 3, "", "",
                "",
               "");
        List<Long> resultIds = result.getData().getResults().getIds().getIntId().getDataList();
        List<Integer> resultIdList = JSONArray.parseArray(resultIds.toString(), Integer.class);
        idList.addAll(resultIdList);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "", paperService.selectPapersByIdList(idList));
    }

    public Result recommendation(String ts) {
        ScriptTriggerUtils.execute("vectorAndImportRedis.py", ts);
        List<Float> queryVector = null;
        // 获取该向量值
        List<String> value = stringRedisTemplate.opsForList().range(RedisKey.REDIS_KEY_TEMP_QUERY_VECTOR, 0, -1);
        if (value != null) {
            queryVector = StringUtils.toFLoatList(value);
        }
        R<SearchResults> result = milvusService.queryBySimilarity(queryVector, 3, "", "",
                "",
                "");
        List<Long> ids = result.getData().getResults().getIds().getIntId().getDataList();
        List<Integer> idList = JSONArray.parseArray(ids.toString(), Integer.class);
        List<PaperDto> paperDtos = paperService.selectPapersByIdList(idList);
        return resultBuilder.build(StatusCode.STATUS_CODE_400, "", paperDtos);
    }

}
