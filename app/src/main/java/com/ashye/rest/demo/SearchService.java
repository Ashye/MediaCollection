package com.ashye.rest.demo;

import android.util.Log;

import com.ashye.rest.ApiBase;
import com.ashye.rest.converter.FastJsonConverter;
import com.ashye.rest.converter.StringConverter;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Administrator on 2015/12/16.
 */
public class SearchService extends ApiBase {

    private interface SearchApi {
        @GET("/baidu")
        Call<String> search(@Query("kw") String key);
    }


    private SearchApi searchApi;

    public SearchService() {
        super();
//        init(new StringConverter());
        searchApi = retrofit.create(SearchApi.class);
    }

    public void search(String key, final ResultListener listener) {
        Call<String> call = searchApi.search(key);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                setResult(true, response.body(), listener);
                Log.e("sss", ""+response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                setResult(false, t.getMessage(), listener);
//                t.printStackTrace();
            }
        });
    }

    private void setResult(boolean isSuccess, String message, ResultListener listener) {
        if (listener != null) {
            if (isSuccess) {
                listener.onSuccess(message);
            }else {
                listener.onFailure(message);
            }
        }
    }
}
