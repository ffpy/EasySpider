package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.scheduler.Scheduler;

public interface OnError {

    void error(Scheduler scheduler, Page page, Throwable throwable);
}
