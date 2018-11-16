package org.ffpy.easyspider.core.downloader;

import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.entity.Response;

/**
 * 页面下载器
 */
public interface Downloader {

    default void download(String url, Callback callback) throws Exception {
        download(Request.Builder.of(url).build(), callback);
    }

    /**
     * 下载页面
     *
     * @param request 请求
     * @param callback 回调
     */
    void download(Request request, Callback callback) throws Exception;

    interface Callback {
        void callback(Response response);
    }
}
