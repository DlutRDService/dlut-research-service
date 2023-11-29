package com.dlut.ResearchService.controller.dataController;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.service.impl.TextAnalysisServiceImpl;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("document_process")
public class DocumentController {
    @Resource
    private TextAnalysisServiceImpl textAnalysisService;

    /**
     * 上传文件到flask服务器，并导入数据到mysql(txt)
     * @param file 上传文件
     */
    @RequestMapping(value = "/importToMysql", method = RequestMethod.POST)
    public Mono<Result> importToMysql(@NotNull @RequestParam MultipartFile file) {
        String contentType = file.getContentType();
        if ("text/plain".equals(contentType)) {
            return textAnalysisService.txtImportToMysql(file);
        } else if ("application/vnd.ms-excel".equals(contentType) || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            return textAnalysisService.excelImportToMysql(file);
        } else {
            return Mono.just(new Result(StatusCode.STATUS_CODE_400, "文件为空或格式不正确", null));
        }
    }
    @PostMapping("/import/excel")
    public Mono<Result> getEmbedding(@NotNull String embeddingModel, @NotNull List<String> sentences){
        return textAnalysisService.getEmbedding(embeddingModel, sentences);
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
