package com.carltaylordev.recordlisterandroidclient.models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by carl on 03/06/2017.
 */

public class RealmAudioClip extends RealmObject {

    @Ignore
    public static final String PRIMARY_KEY = "uuid";

    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String title;
    private String path;

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
