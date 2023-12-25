package com.dlut.ResearchService.controller.dataController;

import com.dlut.ResearchService.annotation.log;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.mapper.UserInfoMapper;
import com.dlut.ResearchService.service.impl.TextAnalysisServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/textAnalysis")
public class TextAnalysisController {
    @Resource
    private TextAnalysisServiceImpl textAnalysisService;
    @Resource
    private UserInfoMapper userInfo;
    /**
     * 上传文件到flask服务器，并导入数据到mysql(txt),需要管理员身份
     * @param file 上传文件
     */
    @log
    @RequestMapping(value = "/importToMysql", method = RequestMethod.POST)
    public Mono<Result> importToMysql(@NotNull @RequestParam MultipartFile file, @NotNull HttpSession session) {
        if (!userInfo.checkManagerIdentity((String) session.getAttribute("emailOrAccount"))){
            return Mono.just(new Result(StatusCode.STATUS_CODE_500, "没有权限", null));
        }
        String contentType = file.getContentType();
        if ("text/plain".equals(contentType)) {
            return textAnalysisService.txtImportToMysql(file);
        } else if ("application/vnd.ms-excel".equals(contentType) || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            return textAnalysisService.excelImportToMysql(file);
        } else {
            return Mono.just(new Result(StatusCode.STATUS_CODE_400, "文件为空或格式不正确", null));
        }
    }

    @log
    @PostMapping("txtToExcel")
    public Mono<ResponseEntity<byte[]>> txtToExcel(@NotNull @RequestParam MultipartFile file) throws IOException {
        return textAnalysisService.txtToExcel(file);
    }

    @PostMapping("NER")
    public Mono<Result> NER(@RequestParam String model, @RequestParam String text) {
        return textAnalysisService.ner(model, text);
    }
    @PostMapping("CLA")
    public Mono<Result> classification(@RequestParam String model, @RequestParam String text) {
        return textAnalysisService.classification(model, text);
    }

    @PostMapping("sequence_CLA")
    public Mono<Result> sequence(@RequestParam String model, @RequestParam String text) {
        return textAnalysisService.sequence(model, text);
    }
    @PostMapping("tokens_CLA")
    public Mono<Result> sentiment(@RequestParam String model, @RequestParam String text) {
        return textAnalysisService.sentiment(model, text);
    }
    @PostMapping("QA")
    public Mono<Result> question_answer(@RequestParam String model, @RequestParam String question) {
        return textAnalysisService.question_answer(model, question);
    }

}
