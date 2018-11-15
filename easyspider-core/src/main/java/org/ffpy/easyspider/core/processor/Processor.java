package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.entity.Context;

/**
 * 页面处理器
 */
public interface Processor {

    /**
     * 处理页面
     *
     * @param context 上下文
     */
    void process(Context context) throws Exception;
}
