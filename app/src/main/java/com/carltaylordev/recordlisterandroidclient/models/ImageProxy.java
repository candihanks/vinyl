package com.carltaylordev.recordlisterandroidclient.models;

/**
 * Created by carl on 31/05/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.carltaylordev.recordlisterandroidclient.R;

public class ImageProxy {
    private Bitmap image;
    // add thumb

    private RealmImage backingData;
    private Boolean isDirty;

    private String title;
    private Boolean isPlaceHolder;
    public String path;

    public ImageProxy(Bitmap image, String title, Boolean isPlaceHolder, String path) {
        super();
        this.image = image;
        this.title = title;
        this.isPlaceHolder = isPlaceHolder;
        this.path = path;
    }

    private void createThumb() {

    }

    public static ImageProxy placeHolderImage(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.magic_wand);
        return new ImageProxy(bitmap, "Tap To Add", true, null);
    }

    public void delete() {
        // can instantly delete when user long presses OR wait for save()?
        // delete backing data
        // delete self?
    }

    public void save() {
        // save backing data
    }

    public String getPath() {
        return path;
    }

    public Boolean isPlaceHolder() {
        return isPlaceHolder;
    }
    public Bitmap getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
