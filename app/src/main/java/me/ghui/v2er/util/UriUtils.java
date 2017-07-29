package me.ghui.v2er.util;

import android.net.Uri;

import me.ghui.v2er.general.PreConditions;

/**
 * Created by ghui on 02/06/2017.
 */

public class UriUtils {
    public static String getLastSegment(String url) {
        if (url.contains("#")) {
            url = url.substring(0, url.indexOf("#"));
        }
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    public static String getParamValue(String url, String paramName) {
        if (PreConditions.isEmpty(url)) return null;
        return Uri.parse(url).getQueryParameter(paramName);
    }


    public static String checkSchema(String url) {
        if (PreConditions.isEmpty(url)) return null;
        if (!url.startsWith("http") && !url.startsWith("https")) {
            if (url.startsWith("//")) {
                url = "http:" + url;
            } else {
                url = "http://" + url;
            }
        }
        return url;
    }
}
