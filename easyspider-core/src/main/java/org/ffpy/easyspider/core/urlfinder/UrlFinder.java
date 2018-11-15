package org.ffpy.easyspider.core.urlfinder;

import org.ffpy.easyspider.core.entity.Context;

import java.util.List;

/**
 * URL查找器。
 * <p>通过此接口来获取HTML中关联的URL。
 */
public interface UrlFinder {

    /**
     * 在下载到页面后会调用此方法以获取关联URL
     */
    void find(Context context, Callback callback) throws Exception;

    interface Callback {
        void callback(List<String> urls);
    }
}
