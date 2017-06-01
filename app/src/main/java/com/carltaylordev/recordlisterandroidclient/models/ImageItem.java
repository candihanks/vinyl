package com.carltaylordev.recordlisterandroidclient.models;

/**
 * Created by carl on 31/05/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.carltaylordev.recordlisterandroidclient.R;

public class ImageItem {
    private Bitmap image;
    private String title;
    private Boolean isPlaceHolder;
    public String path;

    public ImageItem(Bitmap image, String title, Boolean isPlaceHolder, String path) {
        super();
        this.image = image;
        this.title = title;
        this.isPlaceHolder = isPlaceHolder;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static ImageItem placeHolderImage(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.magic_wand);
        return new ImageItem(bitmap, "Tap To Add", true, null);
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
