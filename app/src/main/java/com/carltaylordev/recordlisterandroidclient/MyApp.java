package com.carltaylordev.recordlisterandroidclient;

import android.app.Application;

import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.mock.data.MockData;

import java.io.File;

import io.realm.Realm;

/**
 * Created by ct on 20/05/17.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm realm = setupRealm();
        setupMockData(realm);
        clearTempDir();
    }

    private Realm setupRealm() {
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        return realm;
    }

    private void setupMockData(Realm realm) {
        MockData mockDataSetUp = new MockData(realm);
        mockDataSetUp.setUp();
    }

    private void clearTempDir() {
        FileManager manager = new FileManager(this);
        manager.recursivelyDeleteFileAndChildren(new File(FileManager.getRootTempPath()));
    }
}
