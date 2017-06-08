package com.carltaylordev.recordlisterandroidclient.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

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
        thumb = convertToThumb(BitmapFactory.decodeFile(tempPath));
        if (tempPath != null) {
            path = tempPath;
            isDirty = true;
        }
    }

    public static void rehydrateList(RealmList<RealmImage> list) {
        for (RealmImage image : list) {
            image.rehydrate();
        }
    }

    public void rehydrate() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 100, 100);
        // Decode bitmap with inSampleSize now set
        options.inJustDecodeBounds = false;
        thumb = convertToThumb(BitmapFactory.decodeFile(path, options));
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void freeUpMemory() {
        image.recycle();
        image = null;
    }

    public static RealmImage placeHolderImage(Context context) {
        RealmImage realmImage = new RealmImage("", null);
        realmImage.thumb = convertToThumb(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_camera));
        realmImage.isPlaceHolder = true;
        return realmImage;
    }

    private static Bitmap convertToThumb(Bitmap bitmap) {
        return ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
    }

    public Bitmap getImage() {
        if (image == null) {
            image = BitmapFactory.decodeFile(path);
        }
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
