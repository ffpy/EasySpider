package org.ffpy.easyspider.core.scheduler;

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

/**
 * 调度器建造者
 */
public class SchedulerBuilder {
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

    public SchedulerBuilder addUrl(String url) {
        urlList.add(Objects.requireNonNull(url));
        return this;
    }

    public SchedulerBuilder addUrls(List<String> urls) {
        urlList.addAll(Objects.requireNonNull(urls));
        return this;
    }

    public SchedulerBuilder addDownloader(String urlPattern, Downloader downloader) {
        downloaderList.add(new Worker<>(Objects.requireNonNull(urlPattern),
                Objects.requireNonNull(downloader)));
        return this;
    }

    public SchedulerBuilder addProcessor(String urlPattern, Processor processor) {
        processorList.add(new Worker<>(Objects.requireNonNull(urlPattern),
                Objects.requireNonNull(processor)));
        return this;
    }

    public SchedulerBuilder addUrlFinder(String urlPattern, UrlFinder urlFinder) {
        urlFinderList.add(new Worker<>(Objects.requireNonNull(urlPattern),
                Objects.requireNonNull(urlFinder)));
        return this;
    }

    public SchedulerBuilder setStopper(@Nullable Stopper stopper) {
        this.stopper = stopper;
        return this;
    }

    public SchedulerBuilder onError(@Nullable OnError onError) {
        this.onError = onError;
        return this;
    }

    public Scheduler build() {
        return new Scheduler(urlList, downloaderList, processorList, urlFinderList
                , stopper, onError);
    }
}
