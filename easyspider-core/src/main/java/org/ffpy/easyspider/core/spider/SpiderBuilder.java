package org.ffpy.easyspider.core.spider;

import com.sun.istack.internal.Nullable;
import org.ffpy.easyspider.core.downloader.Downloader;
import org.ffpy.easyspider.core.entity.Worker;
import org.ffpy.easyspider.core.onerror.DefaultOnError;
import org.ffpy.easyspider.core.onerror.OnError;
import org.ffpy.easyspider.core.processor.Processor;
import org.ffpy.easyspider.core.stopper.Stopper;
import org.ffpy.easyspider.core.urlfinder.UrlFinder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 调度器建造者
 */
public class SpiderBuilder {
    /** 任务队列 */
    private final List<String> urlList = new LinkedList<>();
    /** 下载器列表 */
    private final List<Worker<Downloader>> downloaderList = new LinkedList<>();
    /** 处理器列表 */
    private final List<Worker<Processor>> processorList = new LinkedList<>();
    /** URL查找器列表 */
    private final List<Worker<UrlFinder>> urlFinderList = new LinkedList<>();
    /** 停止器 */
    private Stopper stopper;
    /** 错误回调 */
    private OnError onError = new DefaultOnError();
    /** 重试次数 */
    private int retryTimes;
    /** 爬取间隔（毫秒） */
    private int crawlInterval;

    public SpiderBuilder addUrl(String url) {
        urlList.add(Objects.requireNonNull(url));
        return this;
    }

    public SpiderBuilder addUrls(List<String> urls) {
        urlList.addAll(Objects.requireNonNull(urls));
        return this;
    }

    public SpiderBuilder addDownloader(Downloader downloader) {
        addDownloader(".*", downloader);
        return this;
    }

    public SpiderBuilder addDownloader(String urlPattern, Downloader downloader) {
        downloaderList.add(new Worker<>(Objects.requireNonNull(urlPattern),
                Objects.requireNonNull(downloader)));
        return this;
    }

    public SpiderBuilder addProcessor(Processor processor) {
        addProcessor(".*", processor);
        return this;
    }

    public SpiderBuilder addProcessor(String urlPattern, Processor processor) {
        processorList.add(new Worker<>(Objects.requireNonNull(urlPattern),
                Objects.requireNonNull(processor)));
        return this;
    }

    public SpiderBuilder addUrlFinder(UrlFinder urlFinder) {
        addUrlFinder(".*", urlFinder);
        return this;
    }

    public SpiderBuilder addUrlFinder(String urlPattern, UrlFinder urlFinder) {
        urlFinderList.add(new Worker<>(Objects.requireNonNull(urlPattern),
                Objects.requireNonNull(urlFinder)));
        return this;
    }

    public SpiderBuilder setStopper(@Nullable Stopper stopper) {
        this.stopper = stopper;
        return this;
    }

    public SpiderBuilder onError(@Nullable OnError onError) {
        this.onError = onError;
        return this;
    }

    public SpiderBuilder setRetryTimes(int retryTimes) {
        if (retryTimes < 0)
            throw new IllegalArgumentException("重试次数不能小于0");
        this.retryTimes = retryTimes;
        return this;
    }

    public SpiderBuilder setCrawlInterval(int crawlInterval) {
        if (crawlInterval < 0)
            throw new IllegalArgumentException("爬取间隔不能小于0毫秒");
        this.crawlInterval = crawlInterval;
        return this;
    }

    public SpiderBuilder setCrawlInterval(int crawlInterval, TimeUnit unit) {
        if (crawlInterval < 0)
            throw new IllegalArgumentException("爬取间隔不能小于0" + unit);
        this.crawlInterval = (int) unit.toMillis(crawlInterval);
        return this;
    }

    public Spider build() {
        return new Spider(urlList, downloaderList, processorList, urlFinderList
                , stopper, onError, retryTimes, crawlInterval);
    }
}
