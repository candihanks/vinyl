package com.carltaylordev.recordlisterandroidclient.Media;

import android.util.Base64;
import android.util.Base64OutputStream;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by carl on 09/06/2017.
 */

public class Base64Helpers {

    public static String getFileAsBase64(String path) throws IOException {
        InputStream inputStream = new FileInputStream(path);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output, Base64.DEFAULT);

        byte[] buffer = new byte[8192];
        int bytesRead;
        try {
            while ((bytesRead = inputStream.read(buffer)) > -1) {
                base64OutputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            inputStream.close();
            base64OutputStream.close();
        }
        return base64OutputStream.toString();
    }
}