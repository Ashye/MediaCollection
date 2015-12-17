package com.ashye.rest;


import android.text.TextUtils;
import android.util.Log;

import com.ashye.rest.converter.StringConverter;
import com.ashye.rest.demo.FastJsonConverter;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;


import java.io.IOException;

import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Header;

/**
 * Created by Administrator on 2015/12/16.
 */
public abstract class BaseApi {

    private Retrofit retrofit;
    private static final String defaultAPI = "http://www.baidu.com";

    private static Interceptor headerInterceptor;
    private static final String[] headerName = new String[]{
            "User-Agent",
            "Accept",
            "Cache-Control"

    };
    private static final String[] headerValue = new String[]{
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0",
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "no-cache"
    };



    public BaseApi() {
        headerInterceptor = getHeaderInterceptor();
    }

    protected void init(Converter.Factory factory) {
        retrofit = new Retrofit.Builder()
                .baseUrl(defaultAPI)
                .client(defaultClient())
                .addConverterFactory(factory == null ? new StringConverter() : factory)
                .build();
    }

    protected void init(String url, Converter.Factory factory) {
        retrofit = new Retrofit.Builder()
                .baseUrl(TextUtils.isEmpty(url) ? defaultAPI : url)
                .client(defaultClient())
                .addConverterFactory(factory == null ? new StringConverter() : factory)
                .build();
    }

    protected OkHttpClient defaultClient() {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(getHeaderInterceptor());
        return client;
    }

    protected Interceptor getHeaderInterceptor() {

        Interceptor interceptor = new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request origin = chain.request();
                Request request = origin.newBuilder()
                        .addHeader(headerName[1], headerValue[1])
                        .addHeader(headerName[2], headerValue[2])
                        .addHeader(headerName[0], headerValue[0])
                        .build();

                return chain.proceed(request);
            }
        };
        return interceptor;
    }

    protected <T> T getService(Class<T> clz) {
        return retrofit.create(clz);
    }

    public static class CallResultWrapper<T> implements Callback<T> {

        private ResultListener<T> listener;


        public CallResultWrapper(ResultListener<T> listener) {
            this.listener = listener;
        }

        @Override
        public void onResponse(Response<T> response, Retrofit retrofit) {
            setResult(true, response.body(), null);
        }

        @Override
        public void onFailure(Throwable t) {
            setResult(false, null, t.getMessage());
        }

        private void setResult(boolean isSuccess, T t, String error) {
            if (listener != null) {
                if (isSuccess) {
                    listener.onSuccess(t);
                }else {
                    listener.onFailure(error);
                }
            }
        }
    }

    public interface ResultListener<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }
}
