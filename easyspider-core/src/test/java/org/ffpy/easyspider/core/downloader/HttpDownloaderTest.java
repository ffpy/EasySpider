package org.ffpy.easyspider.core.downloader;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class HttpDownloaderTest {

    @Test
    public void testDownload() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new HttpDownloader().download("https://www.baidu.com/", html -> {
//            System.out.println(html);
            assertNotNull(html);
            assertFalse(html.isEmpty());
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }

    @Test(expected = Exception.class)
    public void testDownloadError() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new HttpDownloader().download("http://www.baidu.com/", html -> {
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }
}