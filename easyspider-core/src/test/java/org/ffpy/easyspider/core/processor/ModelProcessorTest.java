package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.Counter;
import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.qidian.BookInfo;
import org.ffpy.easyspider.core.qidian.Qidain;
import org.ffpy.easyspider.core.qidian.TestResponse;
import org.ffpy.easyspider.core.spider.Spider;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ModelProcessorTest {


    @Test
    public void process() throws Exception {
        ModelProcessor<BookInfo> processor = new ModelProcessor<BookInfo>(Qidain.class, "getBookInfo") {
            private final Counter counter = new Counter();

            @Override
            public void process(Page context, BookInfo obj) throws Exception {
                System.out.println(obj);
                counter.incrementSuccess();
            }

            @Override
            public Counter counter() {
                return counter;
            }
        };
        Page page = new Page(mock(Spider.class),
                Request.Builder.of("http://www.baidu.com").build(),
                TestResponse.getResponse());
        processor.process(page);
    }


}