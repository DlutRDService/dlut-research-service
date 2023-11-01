package com.dlut.ResearchService.service.impl;

import com.dlut.ResearchService.component.ResultBuilder;
import com.dlut.ResearchService.entity.constants.Result;
import com.dlut.ResearchService.entity.constants.StatusCode;
import com.dlut.ResearchService.service.IWebClientService;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
     * @param path flask方法路径
     * @param queries 传入的查询列表
     * @return 返回id列表
     */
    @Override
    public Result search(String query) {
        url = "api/search";
        Set<Integer> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("param", query)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Set<Integer>>(){})
                .onErrorReturn(Collections.emptySet())
                .block();
        return resultBuilder.build(
                StatusCode.STATUS_CODE_200,
                "请求成功，处理完毕", response
        );
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

    public <T> List<T> getResultList(String path, String jsonData, Class<T[]> responseType) {
        url = BASE_URL + "/" + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

        T[] responseArray = webClient.exchange(url, HttpMethod.POST, requestEntity, responseType).getBody();
        if (responseArray != null) {
            return List.of(responseArray);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Result updatePaper(String path, MultipartFile multipartFile) throws IOException {
        String url = BASE_URL + "/" + path;

        // 将文件内容转换为字节数组
        byte[] fileBytes = multipartFile.getBytes();

        // 调用 Flask 程序的接口
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头信息
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 设置请求体
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileBytes, headers);
        // 调用接口
        ResponseEntity<String> response = webClient.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // 返回 Flask 程序的响应
        return resultBuilder.build(StatusCode.STATUS_CODE_200, response.getBody(), "");
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
}

