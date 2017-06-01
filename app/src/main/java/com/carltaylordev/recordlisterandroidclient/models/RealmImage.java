package com.carltaylordev.recordlisterandroidclient.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by carl on 31/05/2017.
 */

public class RealmImage extends RealmObject {
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String title;
    private String path;

    public String getUuid() {
        return uuid;
    }

    public ImageItem convertToImageItem(Context context) throws FileNotFoundException {
        Uri uri = Uri.parse("file://" + path);
        InputStream imageStream = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        return new ImageItem(bitmap, title, false, path);
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
