package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.entity.Task;
import org.ffpy.easyspider.core.qidian.BookInfo;
import org.ffpy.easyspider.core.qidian.Qidain;
import org.ffpy.easyspider.core.scheduler.Scheduler;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.mockito.Mockito.*;

public class ObjectProcessorTest {


    @Test
    public void process() throws Exception {
        ObjectProcessor<BookInfo> processor = new ObjectProcessor<BookInfo>(Qidain.class, "getBookInfo") {

            @Override
            public void process(Context context, BookInfo obj) throws Exception {
                System.out.println(obj);
            }
        };
        Context context = new Context(mock(Scheduler.class), new Task("http://www.baidu.com", 1));
        context.setHtml(getHtml());
        processor.process(context);
    }

    private String getHtml() throws IOException {
        Reader reader = new FileReader(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "\\qidian.html");
        BufferedReader br = new BufferedReader(reader);
        StringBuilder html = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            html.append(s);
        }
        return html.toString();
    }
}