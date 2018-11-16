package org.ffpy.easyspider.core.entity;

/**
 * 请求队列
 */
public interface RequestQueue {

    Request get();

    void add(Request request);
}
