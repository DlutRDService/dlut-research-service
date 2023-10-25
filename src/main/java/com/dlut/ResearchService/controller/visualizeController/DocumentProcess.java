package com.dlut.ResearchService.controller.visualizeController;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.service.impl.FlaskServiceImpl;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("document")
public class DocumentProcess {
    @Resource
    private FlaskServiceImpl flaskServiceImpl;

    @PostMapping(value = "document/txtProcess")
    public Result documentProcess(@Nonnull @RequestParam MultipartFile file){
        return flaskServiceImpl.txtProcess(file);
    }
}
