package com.ashye.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * Created by Administrator on 2015/12/9.
 */
public abstract class BaseStorage {

    private static final String defaultDataName = "data.pref";



    protected String getDataName() {
        return defaultDataName;
    }

    protected abstract boolean save(Context context, String key, String value);
    protected abstract boolean save(Context context, String key, int value);

    protected abstract String loadString(Context context, String key);
    protected abstract int loadInt(Context context, String key);
}
