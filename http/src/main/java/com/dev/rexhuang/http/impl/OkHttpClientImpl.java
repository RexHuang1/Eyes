package com.dev.rexhuang.http.impl;

import com.dev.rexhuang.http.IHttpClient;
import com.dev.rexhuang.http.IRequest;
import com.dev.rexhuang.http.IResponse;
import com.dev.rexhuang.http.R;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * *  created by RexHuang
 * *  on 2019/5/30
 */
public class OkHttpClientImpl implements IHttpClient {

    private OkHttpClient client;
    private Callback callback;

    public OkHttpClientImpl(OkHttpClient client) {
        this.client = client;
    }

    public IResponse request(IRequest request, int mode) {
        switch (request.getMethod()) {
            case IRequest.request_method_get:
                return get(request, mode);

            case IRequest.request_method_post:
                return post(request, mode);

            case IRequest.request_method_put:
                return put(request, mode);

            case IRequest.request_method_delete:
                return delete(request, mode);

            default:
                return null;
        }
    }

    @Override
    public IResponse get(IRequest request, int mode) {
        Request.Builder okRequestBuilder = new Request.Builder();
        Headers.Builder okRequestHeaderBuilder = new Headers.Builder();
        HashMap<String, String> headersMap = request.getHeaders();
        for (String key : headersMap.keySet()) {
            okRequestHeaderBuilder.add(key, headersMap.get(key));
        }
        Request okRequest = okRequestBuilder.url(request.getUrl())
                .headers(okRequestHeaderBuilder.build())
                .build();
        switch (mode) {
            case IHttpClient.synchronous:
                return execute(okRequest);
            case IHttpClient.asynchronous:
                enqueue(okRequest);
                break;
        }
        return null;
    }

    @Override
    public IResponse post(IRequest request, int mode) {
        Request.Builder okRequestBuilder = new Request.Builder();
        Headers.Builder okRequestHeaderBuilder = new Headers.Builder();
        HashMap<String, String> headersMap = request.getHeaders();
        for (String key : headersMap.keySet()) {
            okRequestHeaderBuilder.add(key, headersMap.get(key));
        }
        String body = request.getBody();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
        Request okRequest = okRequestBuilder.url(request.getUrl())
                .headers(okRequestHeaderBuilder.build())
                .post(requestBody)
                .build();
        switch (mode) {
            case IHttpClient.synchronous:
                return execute(okRequest);
            case IHttpClient.asynchronous:
                enqueue(okRequest);
                break;
        }
        return null;
    }

    @Override
    public IResponse put(IRequest request, int mode) {
        return null;
    }

    @Override
    public IResponse delete(IRequest request, int mode) {
        return null;
    }

    private IResponse execute(Request okRequest) {
        CommonResponse okResponse = new CommonResponse();
        try {
            Response response = client.newCall(okRequest).execute();
            okResponse.setResponseCode(response.code());
            okResponse.setResponseData(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
//            okResponse.setResponseCode(CommonResponse.STATE_UNKNOWN_ERROR);
            okResponse.setResponseData(e.getMessage());
        }
        return okResponse;
    }

    private void enqueue(Request okRequest) {
        if (callback != null) {
            client.newCall(okRequest).enqueue(callback);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
