package com.ashye.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/12/10.
 */
public class DefaultStorage extends BaseStorage {

//    private static Context CONTEXT;
//
//    public DefaultStorage(Context context) {
//        CONTEXT = context;
//    }



    @Override
    protected boolean save(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(getDataName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    @Override
    protected boolean save(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(getDataName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     *
     * @param context
     * @param key key to stored value
     * @return return the String value of the key stored, or null if the key does not existed
     */
    @Override
    protected String loadString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(getDataName(), Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    /**
     *
     * @param context
     * @param key key to stored value
     * @return return the int value of the key stored, or -1 if the key does not existed
     */
    @Override
    protected int loadInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(getDataName(), Context.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

}
