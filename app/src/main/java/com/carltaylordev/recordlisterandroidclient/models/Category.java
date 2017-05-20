package com.carltaylordev.recordlisterandroidclient.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ct on 20/05/17.
 */

public class Category extends RealmObject {
    @PrimaryKey
    private long id;
    private  String name;
    private String number;

    public long getId() {
        return id;
    }

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
}
