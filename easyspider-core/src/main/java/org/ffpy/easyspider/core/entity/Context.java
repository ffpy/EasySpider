package org.ffpy.easyspider.core.entity;

import org.ffpy.easyspider.core.scheduler.Scheduler;

import java.util.Objects;

public class Context {
    private Scheduler scheduler;
    private Task task;
    private String html;

    public Context(Scheduler scheduler, Task task) {
        this.scheduler = Objects.requireNonNull(scheduler);
        this.task = Objects.requireNonNull(task);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Task getTask() {
        return task;
    }

    public String getUrl() {
        return task.getUrl();
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
