package com.mediamemo.localcollection;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.jar.Attributes;

/**
 * Created by Administrator on 2015/12/10.
 */
public class CollectionBean {
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "itemUrl")
    private String url;
    @JSONField(name = "itemIcon")
    private String iconUrl;


    public CollectionBean() {
    }

    public CollectionBean(String title, String url, String iconUrl) {
        this.title = title;
        this.url = url;
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
