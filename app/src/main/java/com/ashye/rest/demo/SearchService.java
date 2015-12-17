package com.ashye.rest.demo;

import com.ashye.rest.BaseApi;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Administrator on 2015/12/16.
 */
public class SearchService extends BaseApi {

    private interface SearchApi {
        @GET("/baidu")
        Call<String> search(@Query("kw") String key);
    }


    private SearchApi searchApi;

    public SearchService() {
        super();
        init(new StringConverter());
        searchApi = getService(SearchApi.class);
    }

    public void search(String key, final ResultListener<String> listener) {
        Call<String> call = searchApi.search(key);
        call.enqueue(new CallResultWrapper<String>(listener));
    }
}
