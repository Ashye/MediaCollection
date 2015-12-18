package com.ashye.rest.converter;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit.Converter;

/**
 * Created by Administrator on 2015/12/17.
 */
public abstract class BaseConverter<PostBodyType, ReturnType> extends Converter.Factory {

    protected static final MediaType contentType = MediaType.parse("application/octet-stream; charset=utf-8");


    @Override
    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        return new Converter<ResponseBody, ReturnType>() {
            @Override
            public ReturnType convert(ResponseBody value) throws IOException {
                return convertResponseBody(decode(value.string()));
            }
        };
    }

    protected abstract ReturnType convertResponseBody(String body);

    /**
     * POST Method posted body converter
     * @param type
     * @param annotations
     * @return
     */
    @Override
    public Converter<?, RequestBody> toRequestBody(Type type, Annotation[] annotations) {
        return new Converter<PostBodyType, RequestBody>() {
            @Override
            public RequestBody convert(PostBodyType value) throws IOException {
                return RequestBody.create(contentType, encode(convertRequestParameter(value)));
            }
        };
    }


    protected abstract String convertRequestParameter(PostBodyType postData);


    protected String encode(String origin) {
        return origin;
    }

    protected String decode(String cipherText) {
        return cipherText;
    }
}
