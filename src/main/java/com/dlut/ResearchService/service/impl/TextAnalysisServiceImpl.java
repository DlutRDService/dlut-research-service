package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.FlaskUrl;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.service.ITextAnalysisService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class TextAnalysisServiceImpl implements ITextAnalysisService {
    @Resource
    private WebClient webClient;
    @Resource
    private ResultBuilder resultBuilder;

    /**
     * 调用flask服务器，导入数据到Mysql
     * @param file 上传的文件
     * @return 导入结果
     */
    @Override
    public Mono<Result> txtImportToMysql(@NotNull MultipartFile file) {
        if (file.isEmpty()) {
            return Mono.just(resultBuilder.build(StatusCode.STATUS_CODE_400, "上传文件为空"));
        }
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(FlaskUrl.TEXT_IMPORT_TO_MYSQL_URL).build())
                .body(BodyInserters.fromMultipartData("file", file.getResource()))
                .retrieve()
                .bodyToMono(Result.class)
                .map(result -> {
                    // TODO 处理Flask服务器端的相应。
                    result.setMsg("");
                    return result;
                });
    }

    @Override
    public Mono<Result> excelImportToMysql(MultipartFile file) {
        return Mono.just(resultBuilder.build(StatusCode.STATUS_CODE_400, "暂不支持该操作"));
    }

    /**
     * 根据查询的TS字段进行搜索
     * @return 返回id列表
     */
    @Override
    public Set<Integer> searchByStringVector(String query) {
        String url = "/api/milvus/search";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("param", query)
                        .build())
                .retrieve()
                // TODO 查询返回结果类型
                .bodyToMono(new ParameterizedTypeReference<Set<Integer>>(){})
                .onErrorReturn(Collections.emptySet())
                .block();
    }

    /**
     * 调用flask服务器，获取向量
     * @param sentences 待编码的句子
     * @return 向量
     */
    @Override
    public Mono<Result> getEmbedding(String model, @NotNull List<String> sentences){
        if (!sentences.isEmpty()) {
            String url = "/api/embedding";
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path(url)
                            .queryParam("model", model)
                            .queryParam("sentences", sentences)
                            .build())
                    .retrieve()
                    .bodyToMono(Result.class);
        }
        return Mono.just(resultBuilder.build(StatusCode.STATUS_CODE_400, "检查输入的内容", null));
    }

    @Override
    public Result documentProcess(@NotNull MultipartFile file) {
        // String fileName = file.getOriginalFilename();
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "", "");
    }
    @Override
    public Mono<Result> ner(String model, String text) {
        return webClient.post()
               .uri(uriBuilder -> uriBuilder
                       .path(FlaskUrl.TEXT_NER_URL)
                       .queryParam("model", model)
                       .queryParam("text", text)
                       .build())
               .retrieve()
               .bodyToMono(Result.class);
    }
    @Override
    public Mono<Result> classification(String model, String text) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(FlaskUrl.TEXT_NER_URL)
                        .queryParam("model", model)
                        .queryParam("text", text)
                        .build())
                .retrieve()
                .bodyToMono(Result.class);
    }
    @Override
    public List<Integer> searchByIdVector(Integer paperId) {
        String url = "api/searchByTitleVector";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("param", paperId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Integer>>(){})
                .onErrorReturn(Collections.emptyList())
                .block();
    }
    @Override
    public Mono<Result> sequence(String model, String text) {
        return webClient.post()
            .uri(uriBuilder -> uriBuilder
                    .path(FlaskUrl.TEXT_SEQUENCE_URL)
                    .queryParam("model", model)
                    .queryParam("text", text)
                    .build())
            .retrieve()
            .bodyToMono(Result.class);
    }

    @Override
    public Mono<Result> txtToExcel(MultipartFile file) {
        return null;
    }
    @Override
    public Mono<Result> sentiment(String model, String text) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(FlaskUrl.TEXT_SENTIMENT_URL)
                        .queryParam("model", model)
                        .queryParam("text", text)
                        .build())
                .retrieve()
                .bodyToMono(Result.class);
    }
    @Override
    public Result updatePaper(String path, @NotNull MultipartFile multipartFile) {
        return null;
    }

    @Override
    public Mono<Result> question_answer(String model, String question) {
        return webClient.post()
               .uri(uriBuilder -> uriBuilder
                       .path(FlaskUrl.TEXT_QA_URL)
                       .queryParam("model", model)
                       .queryParam("question", question)
                       .build())
               .retrieve()
               .bodyToMono(Result.class);
    }
}
