package com.carltaylordev.recordlisterandroidclient;

import android.content.Context;

import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.mock.data.MockData;

import java.io.File;

import io.realm.Realm;

/**
 * Created by carl on 06/06/2017.
 */

public class SetupSingleton {

    private static SetupSingleton instance = null;

    private SetupSingleton() {}
    public static SetupSingleton setup(Context context) {
        if (instance == null) {
            instance = new SetupSingleton();
            Realm realm = setupRealm(context);
            setupMockData(realm);
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

    private static void clearTempDir(Context context) {
        FileManager manager = new FileManager(context);
        try {
            manager.recursivelyDeleteFolderAndChildren(new File(FileManager.getRootTempPath()));
        } catch (NullPointerException e) {}
    }
}
