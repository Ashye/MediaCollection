package com.mediamemo.html;

import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Administrator on 2015/12/14.
 */
public class HtmlJsoupHelper {

    private Document document = null;



    private void reset() {
        iconUrl = null;
        latest = null;
    }


    public void parseHtmlFromString(String html) {
        document = Jsoup.parse(html, "html.parser");
        reset();
    }

    public void parseHtmlFromUrl(String url, OnHtmlPageLoadListener htmlPageLoadListener) throws IOException {
        document = Jsoup.connect(url).get();
        reset();
        documentPrepared(htmlPageLoadListener);
    }

    private void documentPrepared (OnHtmlPageLoadListener htmlPageLoadListener) {
        if (htmlPageLoadListener != null) {
            htmlPageLoadListener.onHtmlPageLoadedFinished(this);
        }
    }

    public String getTitle() {
        Elements titleDiv = document.select("div.titleDiv > h1");
        for (Element el : titleDiv) {
            return el.text();
        }
        return null;
    }


    private String iconUrl;
    private String latest;
    public String getIconUrl() {
        if (TextUtils.isEmpty(iconUrl)) {
            parseIconAndLatest();
        }
        return iconUrl;
    }

    public String getLatest() {
        if (TextUtils.isEmpty(latest)) {
            parseIconAndLatest();
        }
        return latest;
    }

    private void parseIconAndLatest() {
        Elements iconDiv = document.select("li.imgdiv > div");
        for (Element el : iconDiv) {
            iconUrl = el.select("img").first().attr("src");
            latest = el.select("div").first().text();
            break;
        }
    }

    public interface OnHtmlPageLoadListener {
        void onHtmlPageLoadedFinished(HtmlJsoupHelper jsoupHelper);
    }
}
