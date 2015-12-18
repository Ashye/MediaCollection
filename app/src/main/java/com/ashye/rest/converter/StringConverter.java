package com.ashye.rest.converter;


/**
 * Created by Administrator on 2015/12/16.
 */
public class StringConverter extends BaseConverter<String, String> {

    @Override
    protected String convertResponseBody(String body) {
        return body;
    }

    @Override
    protected String convertRequestParameter(String postData) {
        return postData;
    }
}
