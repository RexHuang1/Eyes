package com.dev.rexhuang.http.impl;

import com.dev.rexhuang.http.IResponse;

/**
 * *  created by RexHuang
 * *  on 2019/5/30
 */
public class CommonResponse implements IResponse {

    private int responseCode;

    private String responseData;

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String getResponseData() {
        return responseData;
    }

    @Override
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    CommonResponse(Builder builder) {
        this.responseCode = builder.responseCode;
        this.responseData = builder.responseData;
    }

    public CommonResponse() {

    }

    public static class Builder {

        private int responseCode;

        private String responseData;

        public Builder setResponseCode(int responseCode) {
            this.responseCode = responseCode;
            return this;
        }

        public Builder setResponseData(String responseData) {
            this.responseData = responseData;
            return this;
        }

        public CommonResponse build() {
            return new CommonResponse(this);
        }
    }
}
