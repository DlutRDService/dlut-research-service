package com.example.academickg.service;

import com.example.academickg.common.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFlaskService {
    Result search(String path, List<String> jsonData);
    Result getEmbedding(String path, List<String> jsonData);
    <T> List<T> getResultList(String path, String jsonData, Class<T[]> responseType);
    Result updatePaper(String path, MultipartFile multipartFile) throws IOException;
}
