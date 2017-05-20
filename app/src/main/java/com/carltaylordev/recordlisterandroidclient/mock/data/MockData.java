package com.carltaylordev.recordlisterandroidclient.mock.data;

import android.content.Context;
import android.util.Log;

import com.carltaylordev.recordlisterandroidclient.models.Category;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by ct on 20/05/17.
 */

public class MockData {

    private Map<String, String> catsMap = new HashMap<>();

    private Realm mRealm;
    private Context mContext;

    public MockData(Context context) {
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    public void setUp() {
        mRealm.beginTransaction();
        cats();
        mRealm.commitTransaction();
    }

    private void cats() {
        catsMap.put("Northern Soul 7", "123456");
        catsMap.put("Northern Soul 12", "23456");
        catsMap.put("Funk 7", "45678");
        catsMap.put("Funk 12", "56789");

        for (Map.Entry<String, String> entry : catsMap.entrySet()) {
            Category newCat = new Category();
            newCat.setName(entry.getKey());
            newCat.setNumber(entry.getValue());
            mRealm.copyToRealm(newCat);
            Log.d("", "");
        }
    }
}
