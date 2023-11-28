package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IWebClientService {

    Mono<Result> importToMysql(MultipartFile file) throws IOException;

    Set<Integer> searchByStringVector(String query);

    Result updatePaper(String path, MultipartFile multipartFile) throws IOException;

    Result documentProcess(MultipartFile file);

    List<Integer> searchByIdVector(Integer paperId);
}
