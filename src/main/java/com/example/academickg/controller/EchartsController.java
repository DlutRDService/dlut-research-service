package com.example.academickg.controller;

import com.example.academickg.component.ResultBuilder;
import com.example.academickg.entity.constants.Result;
import com.example.academickg.entity.constants.StatusCode;
import com.example.academickg.mapper.PaperMapper;
import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private ResultBuilder resultBuilder;
    @GetMapping("/paperNumByYears")
    public Result get(@RequestBody List<Integer> idList){
        List<Map<Object, Object>> yearList = paperMapper.selectYearList(idList);
        return resultBuilder.build(StatusCode.STATUS_CODE_200, null, yearList);
    }

}
