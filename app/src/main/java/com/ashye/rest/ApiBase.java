package com.ashye.rest;


import com.ashye.rest.converter.StringConverter;


import retrofit.Converter;
import retrofit.Retrofit;

/**
 * Created by Administrator on 2015/12/16.
 */
public abstract class ApiBase {

    protected Retrofit retrofit;
    protected final String API = "http://www.baidu.com";


    public ApiBase() {
        init(new StringConverter());
    }

    protected void init(Converter.Factory factory) {
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(factory)
                .build();
    }

    public interface ResultListener {
        void onSuccess(String string);
        void onFailure(String error);
    }
}
