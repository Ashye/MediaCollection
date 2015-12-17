package com.ashye.rest.demo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/17.
 */
public class TypeCheckDemo {

    public Map<String, Object> map = new HashMap<>();

    public <T> void checkType(T t) {
        System.out.println(t.getClass().getCanonicalName());
        System.out.println(Map.class.getCanonicalName());
        System.out.println(HashMap.class.getCanonicalName());

        System.out.println(HashMap.class.getName());
    }

    public static void main(String[] args) {
        TypeCheckDemo demo = new TypeCheckDemo();
        demo.checkType(demo.map);
    }
}
