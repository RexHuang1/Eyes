package com.dev.rexhuang.http.impl;

import android.util.Log;

import com.dev.rexhuang.http.IRequest;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * *  created by RexHuang
 * *  on 2019/5/30
 */
public class CommonRequest implements IRequest {

    private String url;

    private int method;

    private HashMap<String, String> headers;

    private HashMap<String, String> requestBodyMap;

    private String requestBody;

    private Gson gson = new Gson();

    @Override
    public void setMethod(String method) {
        switch (method) {
            case GET:
                this.method = request_method_get;
                break;
            case POST:
                this.method = request_method_post;
                break;
            case PUT:
                this.method = request_method_put;
                break;
            case DELETE:
                this.method = request_method_delete;
                break;

        }
    }

    @Override
    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void setBody(HashMap<String, String> body) {
        this.requestBodyMap = body;
        this.requestBody = gson.toJson(body);
    }

    @Override
    public void setBody(String jsonString) {
        this.requestBody = jsonString;
    }

    @Override
    public void setUrl(String url) {
        if (method == request_method_get && requestBodyMap != null) {
            this.url = attachHttpGetParams(url, requestBodyMap);
        } else {
            this.url = url;
        }
    }

    @Override
    public int getMethod() {
        return this.method;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public String getBody() {
        return this.requestBody;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    /**
     * 为get方法拼接url
     *
     * @param url
     * @param params
     * @return
     */
    public String attachHttpGetParams(String url, HashMap<String, String> params) {
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");
        for (int i = 0; i < params.size(); i++) {
            String value = null;
            try {
                value = URLEncoder.encode(values.next(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuffer.append(keys.next() + "=" + value);
            if (i != params.size() - 1) {
                stringBuffer.append("&");
            }

        }
        url += stringBuffer.toString();
        Log.d("attachHttpGetParams", url);
        return url;
    }


    CommonRequest(Builder builder) {
        this.method = builder.method;
        this.headers = builder.headers;
        this.requestBodyMap = builder.requestBodyMap;
        this.requestBody = builder.requestBody;
        setUrl(builder.url);
    }

    public CommonRequest() {

    }

    public static class Builder {

        private String url;

        private int method;

        private HashMap<String, String> headers;

        private HashMap<String, String> requestBodyMap;

        private String requestBody;

        private Gson gson = new Gson();

        public Builder setMethod(String method) {
            switch (method) {
                case GET:
                    this.method = request_method_get;
                    break;
                case POST:
                    this.method = request_method_post;
                    break;
                case PUT:
                    this.method = request_method_put;
                    break;
                case DELETE:
                    this.method = request_method_delete;
                    break;

            }
            return this;
        }

        public Builder setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }


        public Builder setBody(HashMap<String, String> body) {
            this.requestBodyMap = body;
            this.requestBody = gson.toJson(body);
            return this;
        }


        public Builder setBody(String jsonString) {
            this.requestBody = jsonString;
            return this;
        }


        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }


        public CommonRequest build() {
            return new CommonRequest(this);
        }
    }
}
