package com.example.academickg.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.academickg.common.Result;
import com.example.academickg.constants.RedisKey;
import com.example.academickg.constants.Regex;
import com.example.academickg.constants.StatusCode;
import com.example.academickg.entity.constants.RedisKey;
import com.example.academickg.entity.constants.Regex;
import com.example.academickg.entity.dao.Paper;
import com.example.academickg.entity.dto.PaperDto;
import com.example.academickg.service.impl.MilvusServiceImpl;
import com.example.academickg.service.impl.PaperServiceImpl;
import com.example.academickg.utils.RedisUtils;
import com.example.academickg.utils.ScriptTriggerUtils;
import com.example.academickg.utils.StringUtils;
import io.milvus.grpc.SearchResults;
import io.milvus.param.R;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import com.example.academickg.annotation.log;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    /**
     * 启动Milvus导入脚本，首先查询Mysql中的数据，根据数据变动情况导入向量
     */
    @GetMapping("updateVector")
    public Result updateVector() {
        HashMap<Integer, String> hashMap = paperService.selectTitleAndId();
        for (Integer key : hashMap.keySet()) {
            ScriptTriggerUtils.execute("importMilvus.py", hashMap.get(key), String.valueOf(key));
        }
        return new Result(StatusCode.STATUS_CODE_200, "向量导入完毕", null);
    }

    /**
     * 高级检索功能
     *
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
            return new Result(StatusCode.STATUS_CODE_400, "Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        StringUtils str = new StringUtils();
        // 判断是否含有字符,以及含有=。
        if (!str.containNumOrChar(queryField) || !queryField.contains("=")) {
            return new Result(StatusCode.STATUS_CODE_400, "Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        // 检查索引式是否符合要求,例如“AF=”或者“ AF=”，索引字段前不允许含有字母与数字
        if (!queryField.matches(Regex.FORMAT_QUERY)) {
            return new Result(StatusCode.STATUS_CODE_400, "Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        //判断是否正确使用布尔操作符，检查不正确使用布尔操作符的情况
        if (queryField.matches(Regex.FALSE_BOOLEAN_FORMAT)) {
            return new Result(StatusCode.STATUS_CODE_400, "Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        if (queryField.matches(Regex.MATCH_MULTI_CONDITION)) {
            HashMap<String, Integer> map = paperService.singleSetQueryFieldProcess(queryField);
            List<PaperDto> paperDtoList = paperService.singleSetQueriesProcess(map);
            return new Result(StatusCode.STATUS_CODE_200, "", paperDtoList);
        } else {
            Set<Integer> map1 = paperService.multiSetQueryFieldProcess(queryField);
            List<Integer> idlist = new ArrayList<>(map1);
            List<PaperDto> paperDtoList = paperService.selectPapersByIdList(idlist);
            return new Result(StatusCode.STATUS_CODE_200, "", paperDtoList);
        }
    }

    @GetMapping("full-record/{id}")
    public Result paperInformation(@PathVariable Integer id) {
        ArrayList<Integer> idList = new ArrayList<>();
        idList.add(id);
        RedisUtils<List<Float>> redisUtils = new RedisUtils<>();
        List<Float> vector = redisUtils.getHashValue(RedisKey.REDIS_KEY_TEMP_PAPER_VECTOR, String.valueOf(id));
        R<SearchResults> result = milvusService.query(vector, 3);
        List<Long> resultIds = result.getData().getResults().getIds().getIntId().getDataList();
        List<Integer> resultIdList = JSONArray.parseArray(resultIds.toString(), Integer.class);
        idList.addAll(resultIdList);
        return new Result(StatusCode.STATUS_CODE_200, "", paperService.selectPapersByIdList(idList));
    }

    /**
     * 文件导出
     *
     * @param ids 导出目录
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void exportPaperRecords(@RequestBody String ids, HttpServletResponse response) throws Exception {
        List<Integer> idList = JSON.parseArray(JSON.parseObject(ids).getString("ids"), Integer.class);
        List<PaperDto> list = paperService.selectPapersByIdList(idList);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Papers", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }

    /**
     * 文件导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/import/excel")
    public Boolean importPaperRecordsByExcel(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Paper> list = reader.read(0, 1, Paper.class);
        // return paperService.saveOrUpdateBatch(list);
        return null;
    }

    public Result recommendation(String ts) {
        ScriptTriggerUtils.execute("vectorAndImportRedis.py", ts);
        List<Float> queryVector = null;
        // 获取该向量值
        List<String> value = stringRedisTemplate.opsForList().range(RedisKey.REDIS_KEY_TEMP_QUERY_VECTOR, 0, -1);
        if (value != null) {
            queryVector = StringUtils.toFLoatList(value);
        }
        R<SearchResults> result = milvusService.query(queryVector, 100);
        List<Long> ids = result.getData().getResults().getIds().getIntId().getDataList();
        List<Integer> idList = JSONArray.parseArray(ids.toString(), Integer.class);
        List<PaperDto> paperDtos = paperService.selectPapersByIdList(idList);
        return new Result(StatusCode.STATUS_CODE_400, "", paperDtos);
    }

}
