package com.carltaylordev.recordlisterandroidclient;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by carl on 09/06/2017.
 */

public class KeyValueStore {

    public static final String KEY_BASE_SERVER_URL = "KEY_BASE_SERVER_URL";

    Context mContext;
    SharedPreferences mPrefs;

    public KeyValueStore(Context context) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences("com.carltaylordev.record.lister", mContext.MODE_PRIVATE);
    }

    public void setStringForKey(String value, String key) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value).commit();
    }

    public String getStringForKey(String key) {
        String string = mPrefs.getString(key, null);
        return string;
    }
}
