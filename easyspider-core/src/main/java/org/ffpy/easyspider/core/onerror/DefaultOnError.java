package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.scheduler.Scheduler;

/**
 * 默认的错误处理
 */
public class DefaultOnError implements OnError {
    @Override
    public void error(Scheduler scheduler, Context context, Throwable throwable) {
        System.err.println("爬取" + context.getUrl() + "出错：" + throwable.getMessage());
        throwable.printStackTrace();
    }
}
