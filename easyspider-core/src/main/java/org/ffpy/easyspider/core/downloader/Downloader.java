package org.ffpy.easyspider.core.downloader;

/**
 * 页面下载器
 */
public interface Downloader {

    /**
     * 下载页面
     *
     * @param url URL地址
     */
    void download(String url, Callback callback) throws Exception;

    interface Callback {
        void callback(String html);
    }
}
