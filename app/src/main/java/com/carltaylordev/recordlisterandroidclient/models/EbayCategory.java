package com.carltaylordev.recordlisterandroidclient.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ct on 20/05/17.
 */

public class EbayCategory extends RealmObject {
    @PrimaryKey
    private String number;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static RealmResults getAll() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<EbayCategory> cats = realm.where(EbayCategory.class).findAll();
        return cats;
    }

    public static Boolean anyExist() {
        Realm realm = Realm.getDefaultInstance();
        final RealmObject cat = realm.where(EbayCategory.class).findFirst();
        return cat != null;
    }

    @Override
    public String toString() {
        return name;
    }
}
