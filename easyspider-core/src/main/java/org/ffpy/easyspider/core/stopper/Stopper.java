package org.ffpy.easyspider.core.stopper;

import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.scheduler.Scheduler;

/**
 * 停止器
 */
public interface Stopper {

    /**
     * 是否停止
     *
     * @param scheduler 爬虫调度器
     * @param request 请求
     * @return true表示停止，false表示不停止
     */
    boolean isStop(Scheduler scheduler, Request request);
}
