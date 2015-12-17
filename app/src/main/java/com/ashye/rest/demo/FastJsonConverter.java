package com.ashye.rest.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ashye.rest.converter.BaseConverter;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FastJsonConverter extends BaseConverter<JSONObject> {

    @Override
    protected JSONObject converterBody(String body) {
        JSONObject json = null;
        try {
            json = JSON.parseObject(body);
        } catch (Exception e) {
            throw new RuntimeException("FastJsonConverter --> convert to Json failed!!!");
        }
        return json;
    }
}
