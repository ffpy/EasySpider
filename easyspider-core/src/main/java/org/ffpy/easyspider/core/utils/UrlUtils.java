package org.ffpy.easyspider.core.utils;

import org.ffpy.easyspider.core.helper.PatternHelper;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

public class UrlUtils {
    private static final PatternHelper URL_PARAM_PATTERN = PatternHelper.of("\\{(.+?)\\}");

    public static String getBaseUri(String url) throws MalformedURLException {
        URL u = new URL(url);
        return u.getProtocol() + "://" + u.getHost();
    }

    public static String parseUrl(String url, Map<String, Object> params) {
        URL_PARAM_PATTERN.matcher(url);
        while (URL_PARAM_PATTERN.isFind()) {
            String name = URL_PARAM_PATTERN.group(1);
            URL_PARAM_PATTERN.next();
            Object value = params.get(name);
            if (value == null) continue;
            url = url.replace("{" + name + "}", value.toString());
        }
        return url;
    }

    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
