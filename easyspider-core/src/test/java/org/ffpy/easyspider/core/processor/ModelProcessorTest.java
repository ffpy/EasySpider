package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.qidian.BookInfo;
import org.ffpy.easyspider.core.qidian.Qidain;
import org.ffpy.easyspider.core.qidian.TestResponse;
import org.ffpy.easyspider.core.scheduler.Scheduler;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ModelProcessorTest {


    @Test
    public void process() throws Exception {
        ModelProcessor<BookInfo> processor = new ModelProcessor<BookInfo>(Qidain.class, "getBookInfo") {

            @Override
            public void process(Page context, BookInfo obj) throws Exception {
                System.out.println(obj);
            }
        };
        Page page = new Page(mock(Scheduler.class),
                Request.Builder.of("http://www.baidu.com").build(),
                TestResponse.getResponse());
        processor.process(page);
    }
}