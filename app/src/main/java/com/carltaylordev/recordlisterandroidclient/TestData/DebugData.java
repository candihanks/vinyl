package com.carltaylordev.recordlisterandroidclient.TestData;

import android.content.Context;

import com.carltaylordev.recordlisterandroidclient.KeyValueStore;
import com.carltaylordev.recordlisterandroidclient.R;

/**
 * Created by carl on 12/06/2017.
 */

public class DebugData {

    private Context mContext;

    public DebugData(Context context) {
        mContext = context;
    }

    public void setUp() {
        keyValueStore();
    }

    private void keyValueStore() {
        KeyValueStore keyValueStore = new KeyValueStore(mContext);
        keyValueStore.setStringForKey(KeyValueStore.KEY_BASE_SERVER_URL, mContext.getString(R.string.base_url));
    }
}
