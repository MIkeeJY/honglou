package com.hlsp.video.okhttp.callback;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * 解析Json的基类
 * Created by Sunshine on 15/11/23.
 */
public abstract class OkBaseJsonParser<T> extends OkBaseParser<T> {

    public Type mType;

    public OkBaseJsonParser() {
        mType = getSuperclassTypeParameter(getClass());
    }

    protected abstract T parse(Response response) throws IOException;

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }
}
