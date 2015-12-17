package com.ashye.rest.demo;

import com.ashye.rest.converter.BaseConverter;

/**
 * Created by Administrator on 2015/12/16.
 */
public class StringConverter extends BaseConverter<String> {

    @Override
    protected String converterBody(String body) {
        return body;
    }
}
