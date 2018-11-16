package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.spider.Spider;

public interface OnError {

    void error(Spider spider, Request request, Throwable throwable);
}
