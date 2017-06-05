package com.carltaylordev.recordlisterandroidclient.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.carltaylordev.recordlisterandroidclient.Media.FileManager;
import com.carltaylordev.recordlisterandroidclient.R;

import java.io.FileNotFoundException;
import java.util.UUID;

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
    private Boolean isDirty;
    @Ignore
    private Boolean isPlaceHolder;

    public RealmImage() {}

    public RealmImage(String title, String path) {
        this.title = title;
        // // TODO: 05/06/2017 do we need to add full size image here?
        thumb = convertToThumb(BitmapFactory.decodeFile(path));
        isPlaceHolder = false;
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
        // delete backing data
        // delete self?
    }

    public void save() {
        // save backing data
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isPlaceHolder() {
        return isPlaceHolder;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public Boolean getDirty() {
        return isDirty;
    }

    public void setDirty(Boolean dirty) {
        isDirty = dirty;
    }

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
