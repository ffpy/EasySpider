package org.ffpy.easyspider.core.downloader;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class HttpDownloaderTest {

    @Test
    public void testDownload() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        HttpDownloader.Builder.of().build()
                .download("https://www.baidu.com/", response -> {
                    System.out.println(response);
                    assertNotNull(response);
                    assertFalse(response.string().isEmpty());
                    assertNotNull(response.headers());
                    assertFalse(response.headers().isEmpty());
                    countDownLatch.countDown();
                });
        countDownLatch.await();
    }

    @Test(expected = Exception.class)
    public void testDownloadError() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        HttpDownloader.Builder.of().build()
                .download("http12://www.baidu.com/", html ->
                        countDownLatch.countDown());
        countDownLatch.await();
    }
}