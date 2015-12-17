package com.ashye.rest.converter;

import com.ashye.rest.converter.BaseConverter;

/**
 * Created by Administrator on 2015/12/16.
 */
public class StringConverter extends BaseConverter<String, String> {

    @Override
    protected String converterFromBody(String body) {
        return body;
    }

    @Override
    protected String converterToBody(String postData) {
        return postData;
    }
}
