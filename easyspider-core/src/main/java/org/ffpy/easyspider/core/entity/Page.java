package org.ffpy.easyspider.core.entity;

import com.sun.istack.internal.Nullable;
import org.ffpy.easyspider.core.spider.Spider;

import java.util.Objects;
import java.util.Optional;

/**
 * 页面
 */
public final class Page {
    /** 所属的调度器 */
    private final Spider spider;
    /** 请求 */
    private final Request request;
    /** 响应 */
    private final Response response;

    public Page(Spider spider, Request request, @Nullable Response response) {
        this.spider = Objects.requireNonNull(spider);
        this.request = Objects.requireNonNull(request);
        this.response = response;
    }

    public Spider scheduler() {
        return spider;
    }

    public Request request() {
        return request;
    }

    public String url() {
        return request.url();
    }

    public Response response() {
        return response;
    }

    public String string() {
        return Optional.ofNullable(response).map(Response::string).orElse(null);
    }
}
