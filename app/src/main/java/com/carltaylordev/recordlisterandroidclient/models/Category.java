package com.carltaylordev.recordlisterandroidclient.models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ct on 20/05/17.
 */

public class Category extends RealmObject {
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String name;
    private String number;

    public String getId() {
        return uuid;
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
