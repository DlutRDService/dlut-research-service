package com.dlut.ResearchService.controller.queryController;

import com.alibaba.fastjson.JSONArray;
import com.dlut.ResearchService.entity.dao.Paper;
import com.dlut.ResearchService.utils.RedisUtils;
import com.dlut.ResearchService.utils.ScriptTriggerUtils;
import com.dlut.ResearchService.utils.StringUtils;
import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.redis.RedisKey;
import com.dlut.ResearchService.entity.constants.Regex;
import com.dlut.ResearchService.entity.constants.StatusCode;
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
     * 高级检索功能
     * @param queryField 检索字段
     */
    @log
    @GetMapping("/advanced-search")
    public Result advancedSearch(@RequestParam String queryField) {
        return paperService.advancedQuery(queryField);
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
        List<Paper> paperDtos = paperService.selectPapersByIdList(idList);
        return resultBuilder.build(StatusCode.STATUS_CODE_400, "", paperDtos);
    }

}
