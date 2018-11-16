package org.ffpy.easyspider.core.onerror;

import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.spider.Spider;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class DefaultOnErrorTest {

    @Test
    public void testError() {
        Spider spider = mock(Spider.class);
        new DefaultOnError().error(spider,
                Request.Builder.of("https://www.baidu.com").build(),
                new Exception("错误测试"));
    }
}