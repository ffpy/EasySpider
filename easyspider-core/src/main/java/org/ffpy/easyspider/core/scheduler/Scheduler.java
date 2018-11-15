package org.ffpy.easyspider.core.scheduler;

import com.sun.istack.internal.Nullable;
import org.ffpy.easyspider.core.downloader.Downloader;
import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.entity.Task;
import org.ffpy.easyspider.core.entity.Worker;
import org.ffpy.easyspider.core.onerror.OnError;
import org.ffpy.easyspider.core.processor.Processor;
import org.ffpy.easyspider.core.stopper.Stopper;
import org.ffpy.easyspider.core.urlfinder.UrlFinder;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;

/**
 * 调度器
 */
public class Scheduler {
    /** 最小深度 */
    private static final int MIN_DEPTH = 1;
    /** 等待线程池结束时间（毫秒） */
    private static final int TIME_WAIT_TERMINATED = 50;

    /** 任务队列 */
    private final Queue<Task> taskQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> crawledUrlSet = new HashSet<>();
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
    /** 爬取计数 */
    private int crawlCount;
    /** 处理计数 */
    private int processCount;
    /** 正在爬取计数 */
    private final LongAdder workingCount = new LongAdder();
    /** 结束标志 */
    private volatile boolean isStop;
    /** 允许开始标志 */
    private volatile boolean isAllowStart = true;

    Scheduler(List<String> urls,
              List<Worker<Downloader>> downloaderList,
              List<Worker<Processor>> processorList,
              List<Worker<UrlFinder>> urlFinderList,
              @Nullable Stopper stopper,
              @Nullable OnError onError) {
        this.downloaderList = Objects.requireNonNull(downloaderList);
        this.processorList = Objects.requireNonNull(processorList);
        this.urlFinderList = Objects.requireNonNull(urlFinderList);
        this.stopper = stopper;
        this.onError = onError;
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
        taskQueue.add(new Task(url, MIN_DEPTH));
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
            taskQueue.add(new Task(url, depth));
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
            // 取任务
            Task task = taskQueue.poll();
            // 如果任务队列为空并且所有任务已结束，则结束爬取
            if (task == null) {
                if (0 == workingCount.longValue()) break;
                try {
                    Thread.sleep(TIME_WAIT_TERMINATED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            // 判断是否已爬取过
            if (crawledUrlSet.contains(task.getUrl())) continue;
            crawledUrlSet.add(task.getUrl());

            // 匹配下载器
            Downloader downloader = matchDownloader(task);
            if (downloader == null) continue;
            // 停止判断
            if (stopper != null && stopper.isStop(this, task)) continue;
            // 计数
            crawlCount++;
            workingCount.increment();
            // 构建上下文
            Context context = new Context(this, task);
            try {
                // 下载页面
                downloader.download(task.getUrl(), html -> {
                    // 填充上下文
                    context.setHtml(html);
                    try {
                        // 处理页面
                        Processor processor = matchProcess(task);
                        if (processor != null) {
                            processCount++;
                            processor.process(context);
                        }
                        // 关联URL
                        UrlFinder urlFinder = matchUrlFinder(task);
                        if (urlFinder != null) {
                            urlFinder.find(context, urls -> {
                                addUrls(urls, task.getDepth() + 1);
                                workingCount.decrement();
                            });
                        } else {
                            workingCount.decrement();
                        }
                    } catch (Exception e) {
                        workingCount.decrement();
                        // 错误处理
                        if (onError != null)
                            onError.error(this, context, e);
                    }
                });
            } catch (Exception e) {
                workingCount.decrement();
                // 错误处理
                if (onError != null)
                    onError.error(this, context, e);
            }
            System.out.println("taskQueue size: " + taskQueue.size() +
                    ", processCount: " + processCount +
                    ", crawlCount: " + crawlCount +
                    ", workingCount: " + workingCount);
        }
        // 结束标志为真
        isStop = true;
    }

    /**
     * 停止爬取
     */
    public void stop() {
        this.isStop = true;
    }

    /**
     * 是否已停止
     *
     * @return true为已停止，false为未停止
     */
    public boolean isStop() {
        return isStop;
    }

    /**
     * 获取爬取页面的总数
     *
     * @return 爬取页面的总数
     */
    public int getCrawlCount() {
        return crawlCount;
    }

    /**
     * 获取处理计数
     *
     * @return 处理计数
     */
    public int getProcessCount() {
        return processCount;
    }

    /**
     * 获取正在爬取的页面数
     *
     * @return 正在爬取的页面数
     */
    public int getWorkingCount() {
        return workingCount.intValue();
    }

    /**
     * 匹配下载器
     *
     * @param task 要匹配的任务
     * @return 匹配的下载器，没有匹配则返回null
     */
    private Downloader matchDownloader(Task task) {
        for (Worker<Downloader> worker : downloaderList) {
            if (task.getUrl().matches(worker.getUrlPattern()))
                return worker.getWorker();
        }
        return null;
    }

    /**
     * 匹配处理器
     *
     * @param task 要匹配的任务
     * @return 匹配的处理器，没有匹配则返回null
     */
    private Processor matchProcess(Task task) {
        for (Worker<Processor> worker : processorList) {
            if (task.getUrl().matches(worker.getUrlPattern()))
                return worker.getWorker();
        }
        return null;
    }

    /**
     * 匹配URL查找器
     *
     * @param task 要匹配的任务
     * @return 匹配的URL查找器，没有匹配则返回null
     */
    private UrlFinder matchUrlFinder(Task task) {
        for (Worker<UrlFinder> worker : urlFinderList) {
            if (task.getUrl().matches(worker.getUrlPattern()))
                return worker.getWorker();
        }
        return null;
    }
}
