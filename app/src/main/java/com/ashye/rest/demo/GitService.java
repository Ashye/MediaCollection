package com.ashye.rest.demo;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ashye.rest.BaseApi;
import com.ashye.rest.converter.BaseConverter;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by Administrator on 2015/12/17.
 */
public class GitService extends BaseApi {

//    protected final String API = "https://api.github.com";
    private final String API = "http://www.playaround.tk";

    private interface GitApi {
        @GET("/")
        Call<JSONObject> getApis();

        @GET("/")
        Call<GitApiBean> getApiBean();

        /**
         * page = /userId/login?pass=password
         * @param userId
         * @param password
         * @return
         */
        @GET("/{user}/login")
        Call<GitApiBean> getApiBean(@Path("user") String userId, @Query("pass") String password);

        /**
         * page = /echo?key=value&key=value
         * @param map
         * @return
         */
        @GET("/echo")
        Call<JSONObject> getApiBean(@QueryMap Map<String, Object> map);

//        @Headers({"Cache-Control: max-age=640000","User-Agent: Meizu mobile"})
        @POST("/echo")
        Call<JSONObject> postData(@Body Map<String, Object> map);
    }


    private GitApi apiService;
    public GitService() {
        super();
        init(API, new GitApiConverter());
        apiService = getService(GitApi.class);
    }

//    public void listApis(ResultListener<JSONObject> listener) {
//        Call<JSONObject> call = apiService.getApis();
//        call.enqueue(new CallResultWrapper<JSONObject>(listener));
//    }
//
//    public void getApiBean(ResultListener<GitApiBean> listener) {
//        Call<GitApiBean> call = apiService.getApiBean();
//        call.enqueue(new CallResultWrapper<GitApiBean>(listener));
//    }
//
//    public void getApiBean(ResultListener<JSONObject> listener, Map<String, Object> map) {
//        Call<JSONObject> call = apiService.getApiBean(map);
//        call.enqueue(new CallResultWrapper<JSONObject>(listener));
//    }
//
//    public void post(ResultListener<JSONObject> listener, Map<String, Object> map) {
//        Call<JSONObject> call = apiService.postData(map);
//        call.enqueue(new CallResultWrapper<JSONObject>(listener));
////        call.enqueue(new call);
//    }

    public static class GitApiBean {
        private String current_user_url;
        private String code_search_url;

        public String getCurrent_user_url() {
            return current_user_url;
        }

        public void setCurrent_user_url(String current_user_url) {
            this.current_user_url = current_user_url;
        }

        public String getCode_search_url() {
            return code_search_url;
        }

        public void setCode_search_url(String code_search_url) {
            this.code_search_url = code_search_url;
        }
    }

    private static class GitApiConverter extends BaseConverter<Map, JSONObject> {
        @Override
        protected JSONObject convertResponseBody(String body) {
//            JSONObject bean = JSON.parseObject(body, GitApiBean.class);
//            return bean;
            return JSON.parseObject(body);
        }

        @Override
        protected String convertRequestParameter(Map postData) {
            JSONObject jsonObject = new JSONObject(postData);
            return encode(jsonObject.toJSONString());
        }
    }
}
