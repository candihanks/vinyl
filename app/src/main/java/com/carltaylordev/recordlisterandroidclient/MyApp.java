package com.carltaylordev.recordlisterandroidclient;

import android.app.Application;

import com.carltaylordev.recordlisterandroidclient.mock.data.MockData;

import io.realm.Realm;

/**
 * Created by ct on 20/05/17.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        MockData mockDataSetUp = new MockData(this);
        mockDataSetUp.setUp();
    }
}
