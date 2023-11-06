package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IWebClientService {
    Result search(String query);

    Result getEmbedding(String path, List<String> jsonData);
    Result updatePaper(String path, MultipartFile multipartFile) throws IOException;

    Result documentProcess(MultipartFile file);

    Result txtProcess(MultipartFile file);
}
