package com.carltaylordev.recordlisterandroidclient.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
        // create when user presses save()
    }

    public static RealmImage placeHolderImage(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.magic_wand);
        // ^ create thumb size
        RealmImage realmImage = new RealmImage("Tap To Add", null);
        realmImage.image = bitmap;
        realmImage.isPlaceHolder = true;
        return realmImage;
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

    public ImageProxy convertToImageItem() throws FileNotFoundException {
        Bitmap bitmap = FileManager.decodeImageFromPath(path);
        return new ImageProxy(bitmap, title, false, path);
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
