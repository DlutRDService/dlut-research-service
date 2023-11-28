package com.dlut.ResearchService.controller.dataController;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.dao.Paper;
import com.dlut.ResearchService.service.impl.PaperServiceImpl;
import com.dlut.ResearchService.service.impl.WebClientServiceImpl;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("document")
public class DocumentController {
    @Resource
    private PaperServiceImpl paperService;
    @Resource
    private WebClientServiceImpl webClientServiceImpl;

    /**
     * 上传文件到flask服务器，并导入数据到mysql
     * @param file 上传文件
     */
    @RequestMapping(value = "/importToMysql", method = RequestMethod.POST)
    public Mono<Result> importToMysql(@NotNull @RequestParam MultipartFile file) {
        return webClientServiceImpl.importToMysql(file);
    }
    @PostMapping("/import/excel")
    public Mono<Result> getEmbedding(@NotNull String embeddingModel, @NotNull List<String> sentences){
        return webClientServiceImpl.getEmbedding(embeddingModel, sentences);
    }

    /*
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void exportPaperRecords(@RequestBody String ids, @NotNull HttpServletResponse response) throws Exception {
        List<Integer> idList = JSON.parseArray(JSON.parseObject(ids).getString("ids"), Integer.class);
        List<Paper> list = paperService.selectPapersByIdList(idList);
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

    @PostMapping("/import/excel")
    public Boolean importPaperRecordsByExcel(@NotNull MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Paper> list = reader.read(0, 1, Paper.class);
        // return paperService.saveOrUpdateBatch(list);
        return null;
    }

    */
}
