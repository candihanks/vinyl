package com.carltaylordev.recordlisterandroidclient.mock.data;

import android.content.Context;

import com.carltaylordev.recordlisterandroidclient.models.EbayCategory;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by ct on 20/05/17.
 */

public class MockData {

    private Map<String, String> catsMap = new HashMap<>();

    private Realm mRealm;

    public MockData(Realm realm) {
        mRealm = realm;
    }

    public void setUp() {
        mRealm.beginTransaction();
        cats();
        mRealm.commitTransaction();
    }

    private void cats() {
        if (EbayCategory.anyExist()) {
            return;
        }

        catsMap.put("Northern Soul 7\"", "123456");
        catsMap.put("Northern Soul 12\"", "23456");
        catsMap.put("Funk 7\"", "45678");
        catsMap.put("Funk 12\"", "56789");

        for (Map.Entry<String, String> entry : catsMap.entrySet()) {
            EbayCategory newCat = new EbayCategory();
            newCat.setName(entry.getKey());
            newCat.setNumber(entry.getValue());
            mRealm.copyToRealm(newCat);
        }
    }
}
