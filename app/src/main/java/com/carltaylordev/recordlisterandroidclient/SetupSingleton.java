package com.carltaylordev.recordlisterandroidclient;

import android.content.Context;

import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.TestData.DebugData;
import com.carltaylordev.recordlisterandroidclient.TestData.MockData;

import java.io.File;

import io.realm.Realm;

/**
 * Created by carl on 06/06/2017.
 */

public class SetupSingleton {

    private static SetupSingleton instance = null;

    private SetupSingleton(Context context) {}

    public static SetupSingleton setup(Context context) {
        if (instance == null) {
            instance = new SetupSingleton(context);
            Realm realm = setupRealm(context);
            if (BuildConfig.DEBUG) {
                setupMockData(realm);
                setupDebugData(context);
            }
            clearTempDir(context);
        }
        return instance;
    }

    /**
     * Initial Setup
     */

    private static Realm setupRealm(Context context) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        return realm;
    }

    private static void setupMockData(Realm realm) {
        MockData mockDataSetUp = new MockData(realm);
        mockDataSetUp.setUp();
    }

    private static void setupDebugData(Context context) {
        DebugData debugData = new DebugData(context);
        debugData.setUp();
    }

    private static void clearTempDir(Context context) {
        FileManager manager = new FileManager(context);
        try {
            manager.recursivelyDeleteFolderAndChildren(new File(FileManager.getRootTempPath()));
        } catch (NullPointerException e) {}
    }
}
