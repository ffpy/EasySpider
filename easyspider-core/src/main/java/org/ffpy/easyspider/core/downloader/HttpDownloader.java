package org.ffpy.easyspider.core.downloader;

import org.ffpy.easyspider.core.util.IOUtil;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 简单下载器
 */
public class HttpDownloader implements Downloader {
    /** 成功响应码 */
    private static final int HTTP_OK = 200;
    /** 请求方法 */
    private static final String HTTP_METHOD = "GET";
    /** 超时时间（毫秒） */
    private static final int HTTP_TIMEOUT = 5000;
    /** 请求的UA */
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";

    @Override
    public void download(String url, Callback callback) throws Exception {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setRequestMethod(HTTP_METHOD);
        conn.setConnectTimeout(HTTP_TIMEOUT);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(true);
        conn.addRequestProperty("user-agent", USER_AGENT);
        conn.connect();
        if (HTTP_OK == conn.getResponseCode()) {
            String html = IOUtil.inputStream2String(conn.getInputStream());
            callback.callback(html);
        } else {
            throw new DownloadException(conn.getResponseCode());
        }
    }

    /**
     * 下载出错的异常
     */
    public static class DownloadException extends Exception {

        public DownloadException(int responseCode) {
            super("下载失败，响应码为" + responseCode);
        }
    }
}
