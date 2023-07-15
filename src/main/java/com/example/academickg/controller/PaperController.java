package com.example.academickg.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.example.academickg.annotation.lock;
import com.example.academickg.common.Result;
import com.example.academickg.entity.dao.Paper;
import com.example.academickg.mapper.PaperMapper;
import com.example.academickg.service.impl.PaperServiceImpl;
import com.example.academickg.utils.BM25;
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

    /**
     * 小批量查询指定id的论文
     */
    @log
    @GetMapping("/query")
    public Result queryByIdList(@RequestParam("paperIds") List<Integer> idList){
        Object data = null;
        if (idList.size()!=0){
            if (idList.size()!=1) {
                data = paperService.queryPaperByIdList(idList);
            }
            if (idList.size()==1) {
                data = paperService.queryPaperById(idList.get(0));
            }
        }else {
            return Result.paramError("Error empty character",null);
        }
        return Result.success(null, data);
    }

    /**
     * 主题匹配查询，使用BM25算法
     * @param queryField 检索字段
     */
    @log
    @GetMapping("/advanced-search?queryField=")
    public Result searchByKeywords(@RequestParam String queryField){
        StringUtils str = new StringUtils();
        if (! str.containNumOrChar(queryField) || ! queryField.contains("=")) {
            return Result.paramError("Check your query to make sure search terms, parentheses, " +
                    "and Boolean operators (AND, OR, NOT) are used properly.", null);
        }
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
        // 切割字符串，返回检索式列表
        String [] queryFields = str.splitQueryField(queryField);
        // 遍历检索式列表，对各个字段进行检索
        for (String field : queryFields) {
            // 判断字段是否为主题检索
            if (field.substring(0, 3).equalsIgnoreCase("ts=")) {
                BM25 bm25 = new BM25(1.2, 0);
                //bm25.similarity()
            }
            // 判断字段是否为期刊检索
            if (field.substring(0, 3).equalsIgnoreCase("so=")) {

            }
            // 判断字段是否为作者检索
            if (field.substring(0, 3).equalsIgnoreCase("au=")) {

            }
            // 判断字段是否为年份检索
            if (field.substring(0, 3).equalsIgnoreCase("py=")) {

            }
        }
        return Result.success("", null);
    }


    //实现分页查询
    @GetMapping("/pageQuery")
    public List<Paper> findPage(@RequestParam Integer current, @RequestParam Integer pageSize){
        current = (current-1) * pageSize;
        return paperMapper.pageQuery(current, pageSize);
    }
    // 导出
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void exportPaperRecords(@RequestBody  String ids, HttpServletResponse response) throws Exception{
        List<Integer> idList = JSON.parseArray(JSON.parseObject(ids).getString("ids"), Integer.class);
        List<Paper> list = paperMapper.selectListByIds(idList);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("论文信息", StandardCharsets.UTF_8);
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
        return paperService.saveOrUpdateBatch(list);
    }

    /**
     * 删除缓存
     */
    public Result flushRedis(String key){
        stringRedisTemplate.delete(key);
        return Result.success(null, null);
    }


//
//    @GetMapping("/find/{id}")
//    Paper findById(@PathVariable Integer id){
//        return paperMapper.selectById(id);
//    }
//
//    // 按照id删除
//    @lock
//    @DeleteMapping("/delete/{id}")
//    public int deleteById(@PathVariable Integer id){
//        return paperMapper.deleteById(id);
//    }
//
//    // 插入一条数据
//    @lock
//    @RequestMapping(value = "/insert/entity", method = RequestMethod.POST)
//    public int insertPaper(@RequestBody Paper paper){
//        return paperMapper.insert(paper);
//    }
//
//    @lock
//    // 修改一条数据
//    @RequestMapping(value = "update/entity", method = RequestMethod.POST)
//    public int updatePaper(@RequestBody Paper paper){
//        return paperMapper.update(paper, null);
//    }
//    @PostMapping("/import/txt")
//    public Boolean importPaperRecordsByTxt(MultipartFile file) throws Exception{
//        InputStream inputStream = file.getInputStream();
//        return false;
//    }
}
