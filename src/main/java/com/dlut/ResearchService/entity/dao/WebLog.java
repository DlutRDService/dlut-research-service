package com.dlut.ResearchService.entity.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 日志封装类
 */
@Data
public class WebLog {
    @ApiModelProperty(value =  "日志主键")
    private Integer ops_id;
    @ApiModelProperty(value =  "请求方式")
    private String request_method;
    @ApiModelProperty(value =  "操作类别（0其它 1后台用户 2普通用户）")
    private Integer operator_type;
    @ApiModelProperty(value =  "请求URL")
    private String ops_url;
    @ApiModelProperty(value =  "主机地址")
    private String ops_ip;
    @ApiModelProperty(value =  "请求参数")
    private Object ops_param;
    @ApiModelProperty(value =  "返回参数")
    private String json_result;
    @ApiModelProperty(value =  "异常状态0：正常。1：异常")
    private Integer status;
    @ApiModelProperty(value =  "错误信息")
    private String error_msg;
    @ApiModelProperty(value =  "访问时间")
    private String ops_time;
    @ApiModelProperty(value =  "消耗时间")
    private String cost_time;
}
