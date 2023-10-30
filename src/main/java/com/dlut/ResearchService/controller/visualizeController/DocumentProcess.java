package com.dlut.ResearchService.controller.visualizeController;

import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.service.impl.WebClientServiceImpl;
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
    private WebClientServiceImpl webClientServiceImpl;

    @PostMapping(value = "document/txtProcess")
    public Result documentProcess(@Nonnull @RequestParam MultipartFile file){
        return webClientServiceImpl.txtProcess(file);
    }
}
