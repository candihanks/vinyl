package com.carltaylordev.recordlisterandroidclient.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

import com.carltaylordev.recordlisterandroidclient.Logger;
import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by carl on 31/05/2017.
 */

public class RealmImage extends RealmObject {
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String title;
    private String path;

    @Ignore
    private Bitmap image;
    @Ignore
    private Bitmap thumb;
    @Ignore
    private Boolean isDirty = false;
    @Ignore
    private Boolean isPlaceHolder = false;

    public RealmImage() {}

    public RealmImage(String title, String tempPath) {
        this.title = title;
        image = BitmapFactory.decodeFile(tempPath);
        thumb = convertToThumb(image);
        if (tempPath != null) {
            isDirty = true;
        }
    }

    public static void rehydrateList(RealmList<RealmImage> list) {
        for (RealmImage image : list) {
            image.rehydrate();
        }
    }

    public void rehydrate() {
        image = BitmapFactory.decodeFile(path);
        thumb = convertToThumb(image);
    }

    public static RealmImage placeHolderImage(Context context) {
        RealmImage realmImage = new RealmImage("Tap To Add", null);
        realmImage.thumb = convertToThumb(BitmapFactory.decodeResource(context.getResources(), R.drawable.magic_wand));
        realmImage.isPlaceHolder = true;
        return realmImage;
    }

    private static Bitmap convertToThumb(Bitmap bitmap) {
        return ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
    }

    public void delete() {
        // can instantly delete when user long presses OR wait for save()?
        // check if exists in realm
        // if so delete from there, if not just remove from array
        // delete self?
    }

    public Bitmap getImage() {
        // needed for uploads to server
        return image;
    }

    public boolean isPlaceHolder() {
        return isPlaceHolder;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isDirty() {
        return isDirty;
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
