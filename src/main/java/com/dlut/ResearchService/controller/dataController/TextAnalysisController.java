package com.dlut.ResearchService.controller.dataController;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.service.impl.TextAnalysisServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("text_analysis")
public class TextAnalysisController {
    @Resource
    private TextAnalysisServiceImpl textAnalysisService;

    @PostMapping("NER")
    public Result NER(@RequestParam String model, @RequestParam String text) {
        return textAnalysisService.ner(model, text);
    }
    @PostMapping("CLA")
    public Result classification(@RequestParam String model, String text) {
        return textAnalysisService.classification(model, text);
    }

    @PostMapping("sequence_CLA")
    public Result sequence(@RequestParam String model, String text) {
        return textAnalysisService.sequence(model, text);
    }
    @PostMapping("tokens")
    public Result sentiment(@RequestParam String model, String text) {
        return null;
    }
    @PostMapping("QA")
    public Result question_answer(String question) {
        return null;
    }

}
