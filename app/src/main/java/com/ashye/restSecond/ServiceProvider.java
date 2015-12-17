package com.ashye.restSecond;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.Url;

/**
 * Created by Administrator on 2015/12/17.
 */
public class ServiceProvider {

    private T service;

    public interface BaiduSearch {
        @GET("/")
        Call<String> search(@Query("kw") String keyword, @Url String url);

    }
}
