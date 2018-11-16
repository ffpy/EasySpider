package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.entity.Page;

/**
 * 页面处理器
 */
public interface Processor {

    /**
     * 处理页面
     *
     * @param page 上下文
     */
    void process(Page page) throws Exception;
}
