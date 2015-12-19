package com.ashye.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * Created by Administrator on 2015/12/9.
 */
public abstract class BaseStorage {

    private static final String defaultDataName = "data.pref";
    private static String dataName;
    private SharedPreferences preferences;


    protected String getSharePreferenceName() {
        return TextUtils.isEmpty(dataName) ? defaultDataName : dataName;
    }

    protected void setSharePreferenceName(String dataName) {
        BaseStorage.dataName = dataName;
        preferences = null;
    }

    protected SharedPreferences getSharedPreferences(Context context) {
        if (preferences == null) {
            context.getSharedPreferences(getSharePreferenceName(), Context.MODE_PRIVATE);
        }
        return preferences;
    }

    protected void destroy() {
        preferences = null;
    }

    protected abstract boolean save(Context context, String key, String value);
    protected abstract boolean save(Context context, String key, int value);

    protected abstract String loadString(Context context, String key);
    protected abstract int loadInt(Context context, String key);
}
