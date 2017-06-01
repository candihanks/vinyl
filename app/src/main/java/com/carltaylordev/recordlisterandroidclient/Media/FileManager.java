package com.carltaylordev.recordlisterandroidclient.Media;

import android.content.Context;
import android.graphics.Bitmap;
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

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public File createTempFileOnDisc(String fileExtention) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "temp_file_" + timeStamp + "_";
        File storageDir = mContext.getFilesDir();
        File tempFile = File.createTempFile(
                fileName,
                fileExtention,
                storageDir
        );
        return tempFile;
    }

    public File writeJpegToExternalStorage(Bitmap bitmap, String appDirPath, String imageName) throws Exception {
        String root = Environment.getExternalStorageDirectory().toString();
        File appDir = new File(root + appDirPath);
        appDir.mkdirs();

        Random generator = new Random();
        int randNumber = 10000;
        randNumber = generator.nextInt(randNumber);

        File file = new File (appDir, "image_" + imageName + "_" + randNumber + ".jpg");
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
                        Logger.logMessage("ExternalStorage: Scanned " + path + ":");
                        Logger.logMessage("ExternalStorage: -> uri=" + uri);
                    }
                });
    }
}
