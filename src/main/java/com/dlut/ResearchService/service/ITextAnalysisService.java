package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface ITextAnalysisService {
    Mono<Result> txtImportToMysql(@NotNull MultipartFile file);

    Mono<Result> excelImportToMysql(MultipartFile file);

    Set<Integer> searchByStringVector(String query);

    Mono<Result> getEmbedding(String embeddingModel, @NotNull List<String> sentences);

    Result updatePaper(String path, @NotNull MultipartFile multipartFile) throws IOException;

    Result documentProcess(@NotNull MultipartFile file);

    Result ner(String model, String text);

    Result classification(String model, String text);


    List<Integer> searchByIdVector(Integer paperId);

    Result sequence(String model, String text);
}
