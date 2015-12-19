package com.mediamemo.datacontroller;

import android.util.Log;

import com.ashye.rest.BaseApi;
import com.ashye.rest.converter.BaseConverter;
import com.mediamemo.html.HtmlJsoupHelper;
import com.mediamemo.localcollection.CollectionBean;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.HEAD;
import retrofit.http.Url;

/**
 * Created by Administrator on 2015/12/19.
 */
public class CollectionService extends BaseApi {

    private interface CollectionApi {
        @GET
        Call<CollectionBean> getCollectionBean(@Url String itemUrl);

    }

    public CollectionService() {
        super();
        init(new CollectionConverter());
    }

    public void collectionItem(final String url, final ResultListener<CollectionBean> listener) {
        CollectionApi collectionApi = getService(CollectionApi.class);
        Call<CollectionBean> call = collectionApi.getCollectionBean(url);
        CollectionWrapper wrapper = new CollectionWrapper(listener);
        wrapper.setExtra(url);
        call.enqueue(wrapper);
    }

    public void collectionUpdate(final List<CollectionBean> list, final ResultListener<CollectionBean> listener) {
        if (list == null || list.size() <=0) {
            return;
        }
        CollectionApi collectionApi = getService(CollectionApi.class);
        CollectionWrapper wrapper = null;
        Call<CollectionBean> call = null;
        for (CollectionBean bean : list) {
            call = collectionApi.getCollectionBean(bean.getUrl());
            wrapper = new CollectionWrapper(listener);
            wrapper.setExtra(bean.getUrl());
            call.enqueue(wrapper);
        }
    }

    private class CollectionWrapper extends CallResultWrapper<CollectionBean> {

        private String extra;


        public CollectionWrapper(ResultListener<CollectionBean> listener) {
            super(listener);
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        @Override
        public void onResponse(Response<CollectionBean> response, Retrofit retrofit) {
            CollectionBean bean = response.body();
            bean.setUrl(getExtra());
            setResult(true, bean, null);
        }
    }

    private class CollectionConverter extends BaseConverter<String, CollectionBean> {

        @Override
        protected CollectionBean convertResponseBody(String body) {
            HtmlJsoupHelper jsoupHelper = new HtmlJsoupHelper();
            CollectionBean bean = null;
            jsoupHelper.parseHtmlFromString(body);
            bean = new CollectionBean();
            bean.setIconUrl(jsoupHelper.getIconUrl());
            bean.setLatest(jsoupHelper.getLatest());
            bean.setTitle(jsoupHelper.getTitle());
            return bean;
        }

        @Override
        protected String convertRequestParameter(String postData) {
            return null;
        }
    }
}
