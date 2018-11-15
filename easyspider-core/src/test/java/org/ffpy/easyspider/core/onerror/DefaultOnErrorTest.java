package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.entity.Task;
import org.ffpy.easyspider.core.scheduler.Scheduler;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class DefaultOnErrorTest {

    @Test
    public void testError() {
        Scheduler scheduler = mock(Scheduler.class);
        Context context = new Context(scheduler, new Task("https://www.baidu.com", 1));
        new DefaultOnError().error(scheduler, context, new Exception("错误测试"));
    }
}