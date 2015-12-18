package com.ashye.rest.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ashye.rest.converter.BaseConverter;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FastJsonConverter extends BaseConverter<String, JSONObject> {

    @Override
    protected JSONObject convertResponseBody(String body) {
        JSONObject json = null;
        try {
            json = JSON.parseObject(body);
        } catch (Exception e) {
            throw new RuntimeException("FastJsonConverter --> convert to Json failed!!!");
        }
        return json;
    }

    @Override
    protected String convertRequestParameter(String postData) {
        return encode(postData);
    }
}
