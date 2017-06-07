package com.carltaylordev.recordlisterandroidclient.Media;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import com.carltaylordev.recordlisterandroidclient.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    /**
     * Paths
     */

    public static String getRootExternalPath() { return Environment.getExternalStorageDirectory().toString() + "/record_lister/"; }
    public static String getRootPicturesPath() {
        return getRootExternalPath() + "/pictures/";
    }
    public static String getRootAudioClipsPath() {
        return getRootExternalPath() + "/sound_clips/";
    }
    public static String getRootTempPath() {
        return getRootExternalPath() + "/temp/";
    }

    public boolean fileExistsAtPath(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * Deletion
     */

    public static boolean deleteFileAtPath(String filePath) throws NullPointerException {
        File file = new File(filePath);
        return file.delete();
    }

    public void recursivelyDeleteFolderAndChildren(File file) throws NullPointerException {
        if (file.isDirectory())
            for (String child : file.list()) {
                recursivelyDeleteFolderAndChildren(new File(file, child));
            }
        file.delete();
    }

    /**
     * Creation
     */

    public static File createDirectory(String path) {
        File appDir = new File(path);
        appDir.mkdirs();
        return appDir;
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
        File file = new File (FileManager.createDirectory(appDirPath), "image_" + imageName + ".jpg");

        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        out.flush();
        out.close();

        updateMediaScannerWithFile(file);
        return file;
    }

    public File copyFileFromPathToDirectory(String path, String appDirPath, String fileName) throws Exception {
        File file = new File (FileManager.createDirectory(appDirPath), "" + randomNumber() + "_" + fileName);

        InputStream is = new FileInputStream(path);
        OutputStream os = new FileOutputStream(file.getAbsolutePath(), false);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();

        return file;
    }

    /**
     * Helpers
     */

    private int randomNumber() {
        long seconds = System.currentTimeMillis() / 1000l;
        Random generator = new Random(seconds);
        int randNumber = 10000000;
        int next = generator.nextInt(randNumber);
        Logger.logMessage("Random Num Generated: " + next);
        return next;
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
