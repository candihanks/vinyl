package com.carltaylordev.recordlisterandroidclient.models;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
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

    public static List<EbayCategory> getAllSortedByFavourite() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<EbayCategory> favourites = realm.where(EbayCategory.class).equalTo("isFavourite", true).findAllSorted("name");
        final RealmResults<EbayCategory> NonFavourites = realm.where(EbayCategory.class).equalTo("isFavourite", false).findAllSorted("name");
        List<EbayCategory> copied = realm.copyFromRealm(favourites);
        copied.addAll(realm.copyFromRealm(NonFavourites));
        realm.close();
        return copied;
    }

    public static Boolean anyExist() {
        Realm realm = Realm.getDefaultInstance();
        final RealmObject cat = realm.where(EbayCategory.class).findFirst();
        realm.close();
        return cat != null;
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
