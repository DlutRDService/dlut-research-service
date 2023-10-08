package com.example.academickg.controller;

import com.example.academickg.common.Result;
import com.example.academickg.constants.StatusCode;
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
    @GetMapping("/paperNumByYears")
    public Result get(@RequestBody List<Integer> idList){
        List<Map<Object, Object>> yearList = paperMapper.selectYearList(idList);
        return new Result(StatusCode.STATUS_CODE_200, null, yearList);
    }



}
