package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.service.IWebClientService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

// TODO 将Rest改为Web后，需要修改类结构
@Service
public class WebClientServiceImpl implements IWebClientService {
    private String url;
    @Resource
    private WebClient webClient;
    @Resource
    private ResultBuilder resultBuilder;

    /**
     * 根据查询的TS字段进行搜索
     * @return 返回id列表
     */
    @Override
    public Set<Integer> searchByStringVector(String query) {
        url = "api/searchByTs";
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
     * 编码函数
     * @param path flask方法路径
     * @param texts 编码文本
     * @return 返回编码列表
     */
    public Result getEmbedding(String path, List<String> texts){
        return null;
    }


    @Override
    public Result updatePaper(String path, @NotNull MultipartFile multipartFile) throws IOException {
        return null;
    }

    @Override
    public Result documentProcess(@NotNull MultipartFile file) {
        // String fileName = file.getOriginalFilename();
        return resultBuilder.build(StatusCode.STATUS_CODE_200, "", "");
    }

    @Override
    public Result txtProcess(MultipartFile file) {
        return null;
    }

    @Override
    public List<Integer> searchByIdVector(Integer paperId) {
        url = "api/searchByTitleVector";
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
}

