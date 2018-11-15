package org.ffpy.easyspider.core.entity;

import java.util.Objects;

/**
 * 任务
 */
public class Task {
    /** URL地址 */
    private String url;
    /** 深度 */
    private int depth;

    public Task(String url, int depth) {
        this.url = Objects.requireNonNull(url);

        if (depth < 1) throw new IllegalArgumentException("depth不能小于1");
        this.depth = depth;
    }

    public String getUrl() {
        return url;
    }

    public int getDepth() {
        return depth;
    }
}
