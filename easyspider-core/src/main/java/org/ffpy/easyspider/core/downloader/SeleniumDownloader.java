package org.ffpy.easyspider.core.downloader;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.entity.Response;
import org.ffpy.easyspider.core.exception.EasyCrawlerException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class SeleniumDownloader implements Downloader {
    private final BlockingQueue<WebDriver> webDriverQueue = new LinkedBlockingQueue<>();
    private final int threads;
    private final Executor executor;

    /**
     * @param driver 驱动类型
     * @param path   驱动路径
     * @param threads 线程数
     */
    private SeleniumDownloader(Driver driver, String path, int threads) {
        this.threads = threads;
        executor = threads == 1 ? Executors.newSingleThreadExecutor() :
                Executors.newFixedThreadPool(threads);
        System.setProperty(driver.propertyName(), path);
        for (int i = 0; i < threads; i++) {
            webDriverQueue.add(driver.create());
        }
    }

    @Override
    public void download(Request request, Callback callback) throws Exception {
        if (request.method() != Request.Method.GET)
            throw new EasyCrawlerException("SeleniumDownloader只支持GET方法");
        // 获取浏览器
        WebDriver webDriver = webDriverQueue.take();
        executor.execute(() -> {
            // 设置Cookie
            if (request.hasAnyCookie()){
                webDriver.manage().deleteAllCookies();
                request.cookies().forEach((k, v) ->
                        webDriver.manage().addCookie(new Cookie(k, v)));
            }
            // 打开页面
            webDriver.get(request.url());
            String pageSource = webDriver.getPageSource();
            // 释放浏览器
            webDriverQueue.add(webDriver);

            Response response = Response.Builder.of(Response.OK, pageSource).build();
            callback.callback(response);
        });
    }

    public static class Builder {
        private Driver driver;
        private String path;
        private int threads = 1;

        public static Builder of(Driver driver, String path) {
            return new Builder(driver, path);
        }

        private Builder(Driver driver, String path) {
            this.driver = driver;
            this.path = path;
        }

        public Builder threads(int n) {
            this.threads = n;
            return this;
        }

        public SeleniumDownloader build() {
            return new SeleniumDownloader(driver, path, threads);
        }
    }

    public enum Driver {
        CHROME("webdriver.chrome.driver", (String proxyFilename) -> {
            ChromeOptions options = new ChromeOptions();
            // 设置为headless模式（必须）
            options.addArguments("--headless");
            // 设置浏览器窗口打开大小（非必须）
            options.addArguments("--window-size=1920,1080");
            // 设置代理
            if (StringUtils.isNotEmpty(proxyFilename))
                options.addExtensions(new File(proxyFilename));
            return new ChromeDriver(options);
        });

        private final String propertyName;
        private final Creator creator;

        Driver(String propertyName, Creator creator) {
            this.propertyName = propertyName;
            this.creator = creator;
        }

        public String propertyName() {
            return propertyName;
        }

        public WebDriver create() {
            return creator.create(null);
        }

        public WebDriver create(String proxyFilename) {
            return creator.create(proxyFilename);
        }

        private interface Creator {
            WebDriver create(String proxyFilename);
        }
    }
}
