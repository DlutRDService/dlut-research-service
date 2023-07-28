package com.example.academickg.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.example.academickg.common.Result;
import com.example.academickg.entity.constants.Regex;
import com.example.academickg.entity.dao.Paper;
import com.example.academickg.entity.dto.PaperDto;
import com.example.academickg.mapper.PaperMapper;
import com.example.academickg.service.impl.PaperServiceImpl;
import com.example.academickg.utils.ScriptTriggerUtils;
import com.example.academickg.utils.StringUtils;
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
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/paper")
public class PaperController {
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private PaperServiceImpl paperService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    /**
//     * 按照ID查询相关论文
//     */
//    @log
//    @GetMapping("/query")
//    public Result queryByIdList(@RequestParam("paperIds") List<Integer> idList){
//        Object data = null;
//        if (idList.size()!=0){
//            if (idList.size()!=1) {
//                data = paperService.queryPaperByIdList(idList);
//            }
//            if (idList.size()==1) {
//                data = paperService.queryPaperById(idList.get(0));
//            }
//        }else {
//            return Result.paramError("Error empty character",null);
//        }
//        return Result.success(null, data);
//    }
    @GetMapping("test")
    public Result test(){
        List<String> Titles = paperMapper.selectAll();
        int num = 0;
        for (String title : Titles) {
            System.out.println(num);
            ScriptTriggerUtils.execute("importMilvus.py", title, String.valueOf(num));
            num++;
        }
        return Result.success("", null);
    }
    /**
     * 高级检索
     * @param queryField 检索字段
     */
    @log
    @GetMapping("/advanced-search")
    public Result searchByKeywords(@RequestParam String queryField){
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
        } catch (Exception e){
            return Result.paramError("Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        StringUtils str = new StringUtils();
        // 判断是否含有字符,以及含有=。
        if (! str.containNumOrChar(queryField) || ! queryField.contains("=")) {
            return Result.paramError("Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        // 检查索引式是否符合要求,例如“AF=”或者“ AF=”，索引字段前不允许含有字母与数字
        if (! queryField.matches(Regex.FORMAT_QUERY)){
            return Result.paramError("Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        //判断是否正确使用布尔操作符，检查不正确使用布尔操作符的情况
        if (queryField.matches(Regex.FALSE_BOOLEAN_FORMAT)){
            return Result.paramError("Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
        HashMap<String, Integer> map = paperService.singleSetQueryFieldProcess(queryField);
        if (map == null){
            HashMap<Object, Object> map1 = paperService.multiSetQueryFieldProcess(queryField);
            List<PaperDto> paperDtoList = paperService.multiSetQueriesProcess(map1);
            return Result.success("", paperDtoList);
        }else {
            List<PaperDto> paperDtoList = paperService.singleSetQueriesProcess(map);
            return Result.success("", paperDtoList);
        }
    }

    // 导出
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void exportPaperRecords(@RequestBody  String ids, HttpServletResponse response) throws Exception{
        List<Integer> idList = JSON.parseArray(JSON.parseObject(ids).getString("ids"), Integer.class);
        List<Paper> list = paperMapper.selectListByIds(idList);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Papers", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }
    //excel表格导入
    @PostMapping("/import/excel")
    public Boolean importPaperRecordsByExcel(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Paper> list = reader.read(0,1, Paper.class);
        // return paperService.saveOrUpdateBatch(list);
        return null;
    }

    /**
     * 删除缓存
     */
    public Result flushRedis(String key){
        stringRedisTemplate.delete(key);
        return Result.success(null, null);
    }


}
