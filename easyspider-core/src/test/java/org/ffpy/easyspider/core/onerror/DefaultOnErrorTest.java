package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.scheduler.Scheduler;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DefaultOnErrorTest {

    @Test
    public void testError() {
        Scheduler scheduler = mock(Scheduler.class);
        Page page = new Page(scheduler,
                Request.Builder.of("https://www.baidu.com").build(),
                null);
        new DefaultOnError().error(scheduler, page, new Exception("错误测试"));
    }
}