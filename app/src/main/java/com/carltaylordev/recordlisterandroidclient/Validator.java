package com.carltaylordev.recordlisterandroidclient;

import android.content.Context;

import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by carl on 09/06/2017.
 */

public class Validator {

    public static boolean isValidUrl(String url) {
        return true;
//        Pattern pattern = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");
//        Matcher matcher = pattern.matcher(url);
//        return matcher.matches();
    }

    public static BoolResponse allDataPresentToEnableUpload(Context context) {
        boolean allDataPresent = true;
        String message = "Before we can upload you need to:";

        KeyValueStore keyValueStore = new KeyValueStore(context);
        if (keyValueStore.getStringForKey(KeyValueStore.KEY_BASE_SERVER_URL).isEmpty()) {
            allDataPresent = false;
            message += "\n* Set the Base URL in Settings";
        }
        if (keyValueStore.getStringForKey(KeyValueStore.KEY_SERVER_TOKEN).isEmpty()) {
            allDataPresent = false;
            message += "\n* Login with the server via Settings";
        }

        return new BoolResponse(allDataPresent, message);
    }
}
