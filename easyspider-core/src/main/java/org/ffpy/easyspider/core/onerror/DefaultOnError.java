package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.scheduler.Scheduler;

/**
 * 默认的错误处理
 */
public class DefaultOnError implements OnError {
    @Override
    public void error(Scheduler scheduler, Page page, Throwable throwable) {
        System.err.println("爬取" + page.url() + "出错：" + throwable.getMessage());
        throwable.printStackTrace();
    }
}
