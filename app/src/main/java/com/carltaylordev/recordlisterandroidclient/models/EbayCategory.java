package com.carltaylordev.recordlisterandroidclient.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ct on 20/05/17.
 */

public class EbayCategory extends RealmObject {

    public static final String FORMAT_7 = "7";
    public static final String FORMAT_12 = "12";
    public static final String FORMAT_LP = "LP";

    @PrimaryKey
    private String number;
    private String name;
    private String format;
    private Boolean isFavourite = false;

    public static RealmResults getAll() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<EbayCategory> cats = realm.where(EbayCategory.class).findAllSorted("name");
        return cats;
    }

    public static RealmResults getAllFavourites() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<EbayCategory> cats = realm.where(EbayCategory.class).equalTo("isFavourite", true).findAll().sort("name");
        return cats;
    }

    public static Boolean anyExist() {
        Realm realm = Realm.getDefaultInstance();
        final RealmObject cat = realm.where(EbayCategory.class).findFirst();
        return cat != null;
    }

    public static void deleteAll() {
        RealmResults results = EbayCategory.getAll();
        results.deleteAllFromRealm();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isFavourite() {
        return isFavourite;
    }

    public void setAsFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return name;
    }
}
