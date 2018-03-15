package com.hlsp.video.okhttp.callback;



import java.io.IOException;

import okhttp3.Response;

/**
 * 负责对返回值进行解析，使用策略设计模式
 * Created by Sunshine on 15/11/22.
 */
public abstract class OkBaseParser<T> {

    protected int code;

    protected abstract T parse(Response response) throws IOException;

    public T parseResponse(Response response) throws IOException {
        code = response.code();
        return parse(response);
    }

    public int getCode() {
        return code;
    }

}
