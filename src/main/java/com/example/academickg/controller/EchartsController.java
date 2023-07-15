package com.example.academickg.controller;

import cn.hutool.core.collection.CollUtil;
import com.example.academickg.common.Result;
import com.example.academickg.mapper.PaperMapper;
import io.swagger.models.auth.In;
import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        return Result.success(null, yearList);
    }



}
