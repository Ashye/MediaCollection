package com.ashye.rest.converter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit.Converter;

/**
 * Created by Administrator on 2015/12/17.
 */
public abstract class BaseConverter<T> extends Converter.Factory {

    protected static final MediaType contentType = MediaType.parse("application/octet-stream; charset=utf-8");


    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        return new Converter<ResponseBody, T>() {
            @Override
            public T convert(ResponseBody value) throws IOException {
                return converterBody(decode(value.string()));
            }
        };
    }

    protected abstract T converterBody(String body);

    /**
     * POST Method posted body converter
     * @param type
     * @param annotations
     * @return
     */
    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
//        Log.e("sssssssss", "aaaaaaaaaaaaaaa:"+Type.class.getCanonicalName());
        if (Type.class.equals(Map.class) || Type.class.equals(HashMap.class)) {
            return new Converter<Map<String, Object>, RequestBody>() {
                @Override
                public RequestBody convert(Map<String, Object> value) throws IOException {
                    JSONObject json = mapConvertJson(value);
                    RequestBody body = RequestBody.create(contentType, encode(json.toJSONString()));
                    return body;
                }
            };
        }else if (Type.class.equals(String.class)) {
            return new Converter<String, RequestBody>() {
                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(contentType, encode(value));
                }
            };
        }
        return super.toRequestBody(type, annotations);
    }

    private JSONObject mapConvertJson(Map<String, Object> data) {
        JSONObject object = new JSONObject(data);
        return object;
    }

    protected String encode(String origin) {
        return origin;
    }

    protected String decode(String cipherText) {
        return cipherText;
    }
}
