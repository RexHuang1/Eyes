package com.dev.rexhuang.http;

import java.util.HashMap;
import java.util.Map;

/**
 * *  created by RexHuang
 * *  on 2019/5/30
 * 定义请求数据的封装方式
 */
public interface IRequest {

    String GET = "GET";

    String POST = "POST";

    String PUT = "PUT";

    String DELETE = "DELETE";

    int request_method_get = 1;
    int request_method_post = 2;
    int request_method_put = 3;
    int request_method_delete = 4;

    /**
     * 定义请求方法
     *
     * @param method
     */
    void setMethod(String method);

    /**
     * 定义请求头部
     *
     * @param headers
     */
    void setHeaders(HashMap<String, String> headers);

    /**
     * 定义请求体
     *
     * @param body
     */
    void setBody(HashMap<String, String> body);

    /**
     * 定义请求的参数,该参数已经转化为json字符串
     *
     * @param jsonString
     */
    void setBody(String jsonString);

    /**
     * 提供给执行库请求行URL
     */
    void setUrl(String url);

    /**
     * 获取请求方法
     */
    int getMethod();

    /**
     * 获取请求头部
     */
    HashMap<String, String> getHeaders();

    /**
     * 获取请求体
     *
     * @return
     */
    String getBody();

    /**
     * 获取执行库请求行URL
     */
    String getUrl();


}
