package org.ffpy.easyspider.core;

import java.util.concurrent.atomic.LongAdder;

public class Counter {
    private final LongAdder successfulCount = new LongAdder();
    private final LongAdder failedCount = new LongAdder();

    public int getSuccessfulCount() {
        return successfulCount.intValue();
    }

    public int getFailedCount() {
        return failedCount.intValue();
    }

    public void incrementSuccess() {
        successfulCount.increment();
    }

    public void incrementFailed() {
        failedCount.increment();
    }

    @Override
    public String toString() {
        return "Counter{" +
                "successfulCount=" + successfulCount +
                ", failedCount=" + failedCount +
                '}';
    }
}
