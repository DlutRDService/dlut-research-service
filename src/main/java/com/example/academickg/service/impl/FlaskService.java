package com.example.academickg.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.academickg.common.Result;
import com.example.academickg.entity.constants.StatusCode;
import com.example.academickg.service.IFlaskService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Service
public class FlaskService implements IFlaskService {

    private final RestTemplate restTemplate;
    private final String BASE_URL;
    @Resource
    private Result result;

    @Autowired
    public FlaskService(RestTemplate restTemplate,
                        @Value("${flask.api.url}") String BASE_URL){
        this.restTemplate = restTemplate;
        this.BASE_URL = BASE_URL;
    }

    /**
     * 根据查询的TS字段进行搜索
     * @param path flask方法路径
     * @param queries 传入的查询列表
     * @return 返回id列表
     */
    public Result search(String path, List<String> queries) {
        String jsonData = JSON.toJSONString(queries);
        Class<Integer[]> responseType= Integer [].class;
        List<Integer> resultList = getResultList(path, jsonData, responseType);
        return result.changeResultState(
                result,
                StatusCode.STATUS_CODE_200,
                "请求成功，处理完毕",
                List.of(resultList)
        );
    }

    /**
     * 编码函数
     * @param path flask方法路径
     * @param texts 编码文本
     * @return 返回编码列表
     */
    public Result getEmbedding(String path, List<String> texts){
        String jsonData = JSON.toJSONString(texts);
        Class<List<Float>[]> responseType = (Class<List<Float>[]>) List[].class;
        List<List<Float>> resultList = getResultList(path, jsonData,responseType);
        return result.changeResultState(result, StatusCode.STATUS_CODE_200, "查询完毕", resultList);
    }

    public <T> List<T> getResultList(String path, String jsonData, Class<T[]> responseType) {
        String url = BASE_URL + "/" + path;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

        T[] responseArray = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType).getBody();
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
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // 返回 Flask 程序的响应
        return result.changeResultState(result, StatusCode.STATUS_CODE_200, response.getBody());
    }




}

