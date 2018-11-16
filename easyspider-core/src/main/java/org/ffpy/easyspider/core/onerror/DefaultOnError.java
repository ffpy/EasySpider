package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.spider.Spider;

/**
 * 默认的错误处理
 */
public class DefaultOnError implements OnError {
    @Override
    public void error(Spider spider, Request request, Throwable throwable) {
        System.err.println("爬取" + request.url() + "出错：" + throwable.getMessage());
        throwable.printStackTrace();
    }
}
