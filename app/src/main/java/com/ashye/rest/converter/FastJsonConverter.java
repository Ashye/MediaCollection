package com.ashye.rest.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.ResponseBody;

import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit.Converter;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FastJsonConverter extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        Converter<ResponseBody, JSONObject> converter = new Converter<ResponseBody, JSONObject>() {
            @Override
            public JSONObject convert(ResponseBody value) throws IOException {
                JSONObject json = null;
                try {
                    json = JSON.parseObject(value.string());
                } catch (Exception e) {
                    throw new RuntimeException("FastJsonConverter --> convert to Json failed!!!");
                }
                return json;
            }
        };

        return converter;
    }
}
