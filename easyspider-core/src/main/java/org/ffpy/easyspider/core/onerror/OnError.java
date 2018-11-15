package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.scheduler.Scheduler;

public interface OnError {

    void error(Scheduler scheduler, Context context, Throwable throwable);
}
