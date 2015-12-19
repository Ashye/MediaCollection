package com.mediamemo.datacontroller;

import com.ashye.rest.BaseApi;
import com.ashye.rest.converter.BaseConverter;
import com.mediamemo.html.HtmlJsoupHelper;
import com.mediamemo.localcollection.CollectionBean;

import retrofit.Call;
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

    private String callUrl;
    public void collectionItem(String url, final ResultListener<CollectionBean> listener) {
        callUrl = url;
        CollectionApi collectionApi = getService(CollectionApi.class);
        Call<CollectionBean> call = collectionApi.getCollectionBean(url);
        call.enqueue(new CallResultWrapper<CollectionBean>(listener));
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
            bean.setUrl(callUrl);

            return bean;
        }

        @Override
        protected String convertRequestParameter(String postData) {
            return null;
        }
    }
}
