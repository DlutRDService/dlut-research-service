package com.example.academickg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.academickg.entity.dao.Paper;
import com.example.academickg.entity.dto.PaperDto;

import java.util.HashMap;
import java.util.List;


public interface IPaperService{
    HashMap<String, List<String>> queryFieldProcess(String queryField);
    List<PaperDto> queryProcess(HashMap<String, List<String>> hashMap);
}
