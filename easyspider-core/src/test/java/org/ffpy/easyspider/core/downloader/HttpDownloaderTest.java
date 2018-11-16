package org.ffpy.easyspider.core.downloader;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class HttpDownloaderTest {

    @Test
    public void testDownload() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new HttpDownloader().download("https://www.baidu.com/", response -> {
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
        new HttpDownloader().download("http12://www.baidu.com/", html ->
                countDownLatch.countDown());
        countDownLatch.await();
    }
}