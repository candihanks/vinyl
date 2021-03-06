package com.carltaylordev.recordlisterandroidclient;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by carl on 09/06/2017.
 */

public class KeyValueStore {

    public static final String KEY_BASE_SERVER_URL = "KEY_BASE_SERVER_URL";
    public static final String KEY_SERVER_TOKEN = "KEY_SERVER_TOKEN";

    Context mContext;
    SharedPreferences mPrefs;

    public KeyValueStore(Context context) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences("com.carltaylordev.record.lister", mContext.MODE_PRIVATE);
    }

    public void setStringForKey(String key, String value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value).commit();
    }

    public String getStringForKey(String key) {
        String string = mPrefs.getString(key, "");
        return string;
    }
}
