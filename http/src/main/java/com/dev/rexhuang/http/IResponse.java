package com.dev.rexhuang.http;

/**
 * *  created by RexHuang
 * *  on 2019/5/30
 *    定义响应数据封装方式.
 */
public interface IResponse {

    /**
     * 获得响应状态码
     * @return
     */
    int getResponseCode();

    /**
     * 获得响应数据体
     * @return
     */
    String getResponseData();

    void setResponseCode(int code);

    void setResponseData(String data);
}
