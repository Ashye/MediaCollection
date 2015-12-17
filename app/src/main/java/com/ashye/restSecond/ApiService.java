package com.ashye.restSecond;

import com.ashye.rest.converter.StringConverter;

import retrofit.Retrofit;

/**
 * Created by Administrator on 2015/12/17.
 */
public class ApiService {
    private Retrofit retrofit;
    private final String API = "http://www.baidu.com";


    public ApiService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(new StringConverter())
                .build();
    }

    public <T> T getService(Class<T> clz) {
        return retrofit.create(clz);
    }


}
