package org.ffpy.easyspider.core.spider;

import com.sun.istack.internal.Nullable;
import org.ffpy.easyspider.core.downloader.Downloader;
import org.ffpy.easyspider.core.entity.*;
import org.ffpy.easyspider.core.onerror.OnError;
import org.ffpy.easyspider.core.processor.Processor;
import org.ffpy.easyspider.core.scheduler.QueueScheduler;
import org.ffpy.easyspider.core.scheduler.Scheduler;
import org.ffpy.easyspider.core.stopper.Stopper;
import org.ffpy.easyspider.core.urlfinder.UrlFinder;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 爬虫
 */
public class Spider {
    /** 最小深度 */
    private static final int MIN_DEPTH = 1;
    /** 等待线程池结束时间（毫秒） */
    private static final int TIME_WAIT_TERMINATED = 50;

    /** 调度器 */
    private final Scheduler scheduler = new QueueScheduler();
    /** 下载器列表 */
    private final List<Worker<Downloader>> downloaderList;
    /** 处理器列表 */
    private final List<Worker<Processor>> processorList;
    /** URL查找器列表 */
    private final List<Worker<UrlFinder>> urlFinderList;
    /** 停止器 */
    private final Stopper stopper;
    /** 错误回调 */
    private final OnError onError;
    /** 重试次数 */
    private final int retryTimes;
    /** 爬取间隔（毫秒） */
    private final int crawlInterval;
    /** 正在爬取计数 */
    private final LongAdder workingCount = new LongAdder();
    /** 暂停锁*/
    private final Lock pauseLock = new ReentrantLock();
    /** 暂停器 */
    private final Condition pauseCondition = pauseLock.newCondition();
    /** 暂停标志 */
    private volatile boolean isPause;
    /** 结束标志 */
    private volatile boolean isStop;
    /** 允许开始标志 */
    private volatile boolean isAllowStart = true;

    Spider(List<String> urls,
           List<Worker<Downloader>> downloaderList,
           List<Worker<Processor>> processorList,
           List<Worker<UrlFinder>> urlFinderList,
           @Nullable Stopper stopper,
           @Nullable OnError onError,
           int retryTimes,
           int crawlInterval) {
        this.downloaderList = Objects.requireNonNull(downloaderList);
        this.processorList = Objects.requireNonNull(processorList);
        this.urlFinderList = Objects.requireNonNull(urlFinderList);
        this.stopper = stopper;
        this.onError = onError;
        this.retryTimes = retryTimes;
        this.crawlInterval = crawlInterval;
        addUrls(urls);
    }

    /**
     * 添加URL到队列中
     *
     * @param url 要添加的URL
     */
    public void addUrl(String url) {
        if (isStop)
            throw new IllegalStateException("Scheduler已停止！");
        Objects.requireNonNull(url);
        scheduler.addRequest(Request.Builder.of(url).build());
    }

    /**
     * 添加URL列表到队列中
     *
     * @param urls URL列表
     */
    public void addUrls(List<String> urls) {
        addUrls(urls, MIN_DEPTH);
    }

    /**
     * 添加URL列表
     *
     * @param urls  URL列表
     * @param depth 深度
     */
    private void addUrls(List<String> urls, int depth) {
        if (isStop)
            throw new IllegalStateException("Scheduler已停止！");
        Objects.requireNonNull(urls);
        for (String url : urls)
            scheduler.addRequest(Request.Builder.of(url).depth(depth).build());
    }

    public void addRequest(Request request) {
        scheduler.addRequest(request);
    }

    /**
     * 开始爬取
     */
    public void start() {
        // 只能启动一次检测
        if (!isAllowStart)
            throw new IllegalStateException("Scheduler只能启动一次");
        isAllowStart = false;
        // 遍历任务队列
        while (!isStop) {
            checkPause();
            // 取请求
            Request request = getRequest();
            if (request == null) break;
            // 停止判断
            if (stopper != null && stopper.isStop(this, request)) continue;
            // 处理请求
            processRequest(request);
            // 等待爬取间隔
            waitCrawlInterval();
        }
        // 结束标志为真
        isStop = true;
    }

    /**
     * 等待爬取间隔
     */
    private void waitCrawlInterval() {
        if (crawlInterval <= 0) return;
        try {
            Thread.sleep(crawlInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取请求，当前请求队列为空时会阻塞等待所有任务结束，
     * 如果有新的请求，则返回，否则返回null，表示没有更多请求了。
     */
    private Request getRequest() {
        while (true) {
            Request request = scheduler.getRequest();
            // 如果任务队列为空并且所有任务已结束，则结束爬取
            if (request == null) {
                if (workingCount.longValue() == 0) return null;
                try {
                    Thread.sleep(TIME_WAIT_TERMINATED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            return request;
        }
    }

    private void processRequest(Request request) {
        // 匹配下载器
        Downloader downloader = matchWorker(request, downloaderList);
        if (downloader == null) return;
        // 计数
        workingCount.increment();
        while (request.retryCount() <= retryTimes) {
            try {
                checkPause();
                // 下载页面
                downloader.download(request, response -> {
                    Page page = new Page(this, request, response);
                    try {
                        // 处理页面
                        Processor processor = matchWorker(request, processorList);
                        if (processor != null) {
                            checkPause();
                            processor.process(page);
                        }
                        // 关联URL
                        UrlFinder urlFinder = matchWorker(request, urlFinderList);
                        if (urlFinder != null) {
                            urlFinder.find(page, urls -> {
                                checkPause();
                                addUrls(urls, request.depth() + 1);
                                workingCount.decrement();
                            });
                        } else {
                            workingCount.decrement();
                        }
                    } catch (Exception e) {
                        workingCount.decrement();
                        error(request, e);
                    }
                });
                break;
            } catch (Exception e) {
                workingCount.decrement();
                error(request, e);
                request.incrementRetryCount();
            }
        }
    }

    /**
     * 暂停爬取
     */
    public void pause() {
        if (!isPause) {
            isPause = true;
        }
    }

    /**
     * 继续爬取
     */
    public void resume() {
        if (isPause) {
            isPause = false;
            pauseLock.lock();
            try {
                pauseCondition.signalAll();
            } finally {
                pauseLock.unlock();
            }
        }
    }

    /**
     * 是否已暂停爬取
     *
     * @return true为已暂停，false为未暂停
     */
    public boolean isPause() {
        return isPause;
    }

    /**
     * 停止爬取
     */
    public void stop() {
        this.isStop = true;
    }

    /**
     * 是否已停止爬取
     *
     * @return true为已停止，false为未停止
     */
    public boolean isStop() {
        return isStop;
    }

    /**
     * 获取正在爬取的页面数
     *
     * @return 正在爬取的页面数
     */
    public int getWorkingCount() {
        return workingCount.intValue();
    }

    public int getDownloadSuccessfulCount() {
        int count = 0;
        for (Worker<Downloader> downloaderWorker : downloaderList) {
            count += downloaderWorker.getWorker().counter().getSuccessfulCount();
        }
        return count;
    }

    public int getDownloadFailedCount() {
        int count = 0;
        for (Worker<Downloader> downloaderWorker : downloaderList) {
            count += downloaderWorker.getWorker().counter().getFailedCount();
        }
        return count;
    }

    public int getProcessSuccessfulCount() {
        int count = 0;
        for (Worker<Processor> processorWorker : processorList) {
            count += processorWorker.getWorker().counter().getSuccessfulCount();
        }
        return count;
    }

    public int getProcessFailedCount() {
        int count = 0;
        for (Worker<Processor> processorWorker : processorList) {
            count += processorWorker.getWorker().counter().getFailedCount();
        }
        return count;
    }

    private void error(Request request, Throwable e) {
        if (onError != null)
            onError.error(this, request, e);
    }

    /**
     * 匹配工作器
     */
    private <T> T matchWorker(Request request, List<Worker<T>> workerList) {
        for (Worker<T> worker : workerList) {
            if (request.url().matches(worker.getUrlPattern()))
                return worker.getWorker();
        }
        return null;
    }

    /**
     * 暂停检测，如果为暂停状态，则阻塞当前线程
     */
    private void checkPause() {
        if (isPause) {
            pauseLock.lock();
            try {
                pauseCondition.awaitUninterruptibly();
            } finally {
                pauseLock.unlock();
            }
        }
    }
}
