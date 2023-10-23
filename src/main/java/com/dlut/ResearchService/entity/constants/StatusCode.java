package com.dlut.ResearchService.entity.constants;

public enum StatusCode {
    // 2xx(Success Status Codes):指示请求已经成功接受、理解和处理
    STATUS_CODE_200,  // OK
    STATUS_CODE_201,  // Created
    STATUS_CODE_204,  // No Content
    // 3xx(Redirection Status Code):指示需要采取进一步的操作以完成请求
    STATUS_CODE_301,  // Moved
    STATUS_CODE_302,  // Found
    STATUS_CODE_304,  // Not Modified
    STATUS_CODE_307,  // Temporary Redirect
    STATUS_CODE_308,  // Permanent Redirect
    // 4xx(Client Error Status Codes):指示客户端发出的请求有错误或无法完成请求
    STATUS_CODE_401,  // Unauthorized
    STATUS_CODE_400,  // Bad Request
    STATUS_CODE_404,  // Not Found
    STATUS_CODE_403,  // Forbidden
    // 5xx(Server Error Status Codes)
    STATUS_CODE_500,  // Internal Server Error
    STATUS_CODE_502,  // Bad Gateway
    STATUS_CODE_504  // Gateway Timeout
    }
