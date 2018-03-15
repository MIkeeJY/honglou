package com.hlsp.video.okhttp.callback;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Modifier;

import okhttp3.Response;

/**
 * 解析JSON，转换为实体类
 * Created by Sunshine on 15/11/22.
 */
public abstract class OkJsonParser<T> extends OkBaseJsonParser<T> {

    protected Gson mGson;

    public OkJsonParser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithModifiers(
                            Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC);
            mGson = gsonBuilder.create();
        } else {
            mGson = new Gson();
        }
    }

    @Override
    public T parse(Response response) throws IOException {
        String body = response.body().string();
        //在这里打印返回的json
        LogUtils.json(body);

//        json(body);
        return mGson.fromJson(body, mType);
    }

    public void json(String json) {
        byte indent = 4;
        if (TextUtils.isEmpty(json)) {
            Log.i("ogami", "JSON{json is empty}");
        } else {
            try {
                String msg;
                if (json.startsWith("{")) {
                    JSONObject e = new JSONObject(json);
                    msg = e.toString(indent);
                    Log.i("ogami", msg);
                } else if (json.startsWith("[")) {
                    JSONArray e1 = new JSONArray(json);
                    msg = e1.toString(indent);
                    Log.i("ogami", msg);
                }
            } catch (JSONException var5) {
                Log.i("ogami", var5.toString() + "\n\njson = " + json);
            }

        }
    }

}