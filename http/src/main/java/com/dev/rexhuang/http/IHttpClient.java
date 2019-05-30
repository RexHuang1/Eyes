package com.dev.rexhuang.http;

/**
 * *  created by RexHuang
 * *  on 2019/5/30
 */
public interface IHttpClient {

    int synchronous = 0x100001;
    int asynchronous = 0x100010;

    IResponse get(IRequest request, int mode);

    IResponse post(IRequest request, int mode);

    IResponse put(IRequest request, int mode);

    IResponse delete(IRequest request, int mode);
}
