package org.ffpy.easyspider.core.scheduler;

import org.ffpy.easyspider.core.entity.Request;

/**
 * 调度器
 */
public interface Scheduler {

    /**
     * 获取请求
     */
    Request getRequest();

    /**
     * 添加请求
     */
    void addRequest(Request request);

    /**
     * 删除请求
     */
    void removeRequest(Request request);

    /**
     * 获取剩余请求数
     */
    int getRequestCount();
}
