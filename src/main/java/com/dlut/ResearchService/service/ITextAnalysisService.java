package com.dlut.ResearchService.service;

import com.dlut.ResearchService.entity.constants.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ITextAnalysisService {
    Mono<Result> txtImportToMysql(@NotNull MultipartFile file);

    Mono<Result> excelImportToMysql(MultipartFile file);

    Set<Integer> searchByStringVector(String query);

    Mono<Result> getEmbedding(String embeddingModel, @NotNull List<String> sentences);

    Result updatePaper(String path, @NotNull MultipartFile multipartFile) throws IOException;

    Result documentProcess(@NotNull MultipartFile file);

    Mono<Result> ner(String model, String text);

    Mono<Result> classification(String model, String text);

    List<Integer> searchByIdVector(Integer paperId);

    Mono<Result> sequence(String model, String text);

    Mono<ResponseEntity<byte[]>> txtToExcel(MultipartFile file) throws IOException;

    Mono<Result> sentiment(String model, String text);

    Mono<Result> question_answer(String model, String question);
}
