package org.ffpy.easyspider.core.urlfinder;

import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.entity.Task;
import org.ffpy.easyspider.core.scheduler.Scheduler;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PatternUrlFinderTest {
    private String html = "<html>\n" +
            "<head>\n" +
            "\t<meta charset=\"utf8\">\n" +
            "</head>\n" +
            "<body>\n" +
            "\t<h1>test</h1>\n" +
            "\t<a href=\"/a.html\"/>\n" +
            "\t<a href=\"http://www.test.com/b.html\"/>\n" +
            "\t<img src=\"/a.png\"/>\n" +
            "\t<img src=\"http://www.test.com/b.png\"/>\n" +
            "</body>\n" +
            "</html>";
    private Context context = new Context(mock(Scheduler.class), new Task("https://www.tset.com", 1));


    @Test
    public void testFind() throws Exception {
        PatternUrlFinder finder = new PatternUrlFinder();
        context.setHtml(html);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        finder.find(context, urls -> {
            String[] expected = new String[]{
                    "https://www.tset.com/a.html", "http://www.test.com/b.html",
                    "https://www.tset.com/a.png", "http://www.test.com/b.png"};
            assertArrayEquals(expected, urls.toArray());
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }

    @Test
    public void testFind2() throws Exception {
        PatternUrlFinder finder = new PatternUrlFinder(".*\\.png");
        context.setHtml(html);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        finder.find(context, urls -> {
            String[] expected = new String[]{
                    "https://www.tset.com/a.png", "http://www.test.com/b.png"};
            assertArrayEquals(expected, urls.toArray());
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }
}