package com.dlut.ResearchService.controller.dataController;

import com.dlut.ResearchService.service.impl.PaperServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("visualize")
public class VisualizeController {
    @Resource
    private PaperServiceImpl paperService;
    @Resource
    private WebClientServiceImpl webClientServiceImpl;
}
