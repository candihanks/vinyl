package com.carltaylordev.recordlisterandroidclient.Media;

import android.util.Base64;
import android.util.Base64OutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by carl on 09/06/2017.
 */

public class Base64Helpers {

    public static String getFileAsBase64(String path) throws IOException {
        File file = new File(path);
        InputStream inputStream;
        String encodedFile;

        inputStream = new FileInputStream(file.getAbsolutePath());
        byte[] buffer = new byte[10240];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream output64 = new Base64OutputStream(output,
                Base64.NO_WRAP);

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            output64.write(buffer, 0, bytesRead);
        }
        output64.close();
        encodedFile = output.toString();
        return encodedFile;
    }
}