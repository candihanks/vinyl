package com.carltaylordev.recordlisterandroidclient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by carl on 09/06/2017.
 */

public class Validator {

    public static boolean isValidUrl(String url) {
        Pattern pattern = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
