package org.ffpy.easyspider.core.entity;

public class Worker<T> {
    private String urlPattern;
    private T worker;

    public Worker(String urlPattern, T worker) {
        this.urlPattern = urlPattern;
        this.worker = worker;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public T getWorker() {
        return worker;
    }

    public void setWorker(T worker) {
        this.worker = worker;
    }
}
