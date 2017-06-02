package com.carltaylordev.recordlisterandroidclient.Media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.carltaylordev.recordlisterandroidclient.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by carl on 31/05/2017.
 */

public class FileManager {

    private Context mContext;

    public FileManager(Context mContext) {
        this.mContext = mContext;
    }

    public static String getRootExternalPath() {
        return Environment.getExternalStorageDirectory().toString() + "/record_lister/";
    }

    public static String getRootPicturesPath() {
        return getRootExternalPath() + "/pictures/";
    }

    public static String getRootSoundClipsPath() {
        return getRootExternalPath() + "/sound_clips/";
    }

    public static String getRootTempPath() {
        return getRootExternalPath() + "/temp/";
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        boolean success = file.delete();
        return success;
    }

    public static File createDirectory(String path) {
        File appDir = new File(path);
        appDir.mkdirs();
        return appDir;
    }

    public static Bitmap decodeImageFromPath(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public static File createTempFileOnDisc(String fileExtension) throws IOException {
        File storageDir = FileManager.createDirectory(getRootTempPath());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "temp_file_" + timeStamp + "_" + fileExtension;
        String path = storageDir.getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file;
    }

    public File writeJpegToDisc(Bitmap bitmap, String appDirPath, String imageName) throws Exception {
        Random generator = new Random();
        int randNumber = 10000;
        randNumber = generator.nextInt(randNumber);

        File file = new File (FileManager.createDirectory(appDirPath), "image_" + imageName + "_" + randNumber + ".jpg");
        if (file.exists ()) {
            throw new Exception("Duplicate file found");
        }

        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        out.flush();
        out.close();

        updateMediaScannerWithFile(file);
        return file;
    }

    private void updateMediaScannerWithFile(File file) {
        MediaScannerConnection.scanFile(mContext, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Logger.logMessage("ExternalStorage: Scanned " + path);
                        Logger.logMessage("ExternalStorage: uri: " + uri);
                    }
                });
    }
}
