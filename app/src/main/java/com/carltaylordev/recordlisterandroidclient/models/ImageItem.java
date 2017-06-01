package com.carltaylordev.recordlisterandroidclient.models;

/**
 * Created by carl on 31/05/2017.
 */

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String title;
    private Boolean isPlaceHolder;
    public String uri;

    public ImageItem(Bitmap image, String title, Boolean isPlaceHolder, String uri) {
        super();
        this.image = image;
        this.title = title;
        this.isPlaceHolder = isPlaceHolder;
        this.uri = uri;
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