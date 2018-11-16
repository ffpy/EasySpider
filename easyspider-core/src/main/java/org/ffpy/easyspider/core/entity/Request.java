package org.ffpy.easyspider.core.entity;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.easyspider.core.utils.UrlUtils;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * 请求
 */
public final class Request {
    /** 最小深度 */
    public static final int MIN_DEPTH = 1;

    /** URL地址 */
    private final String url;
    /** 请求方法 */
    private final Method method;
    /** 请求深度 */
    private final int depth;
    /** 请求参数 */
    private final Map<String, String> params;
    /** 请求头 */
    private final Map<String, String> headers;
    /** Cookie信息 */
    private final Map<String, String> cookies;
    /** 额外信息 */
    private final Object extra;
    /** 优先级 */
    private final int priority;
    /** 重试次数 */
    private LongAdder retryCount = new LongAdder();

    private Request(String url, Method method, int depth,
                    @Nullable Map<String, String> params,
                    @Nullable Map<String, String> headers,
                    @Nullable Map<String, String> cookies,
                    @Nullable Object extra,
                    int priority) {
        this.url = Objects.requireNonNull(url);
        this.method = Objects.requireNonNull(method);
        this.depth = depth;
        this.params = params;
        this.headers = headers;
        this.cookies = cookies;
        this.extra = extra;
        this.priority = priority;
    }

    public String url() {
        return url;
    }

    public String decodeUrl() {
        return UrlUtils.decodeUrl(url);
    }

    public Method method() {
        return method;
    }

    public int depth() {
        return depth;
    }

    public Map<String, String> params() {
        return params;
    }

    public String param(String name) {
        return Optional.of(name)
                .map(params::get)
                .orElse(null);
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String header(String name) {
        if (headers == null) return null;
        return headers.get(name);
    }

    public Map<String, String> cookies() {
        return cookies;
    }

    public String cookie(String name) {
        if (cookies == null) return null;
        return cookies.get(name);
    }

    public boolean hasCookie(String name) {
        if (cookies == null) return false;
        return cookies.containsKey(name);
    }

    public boolean hasAnyCookie() {
        if (cookies == null) return false;
        return !cookies.isEmpty();
    }

    public <T> T extra() {
        //noinspection unchecked
        return (T) extra;
    }

    public int prority() {
        return priority;
    }

    public int retryCount() {
        return retryCount.intValue();
    }

    public void incrementRetryCount() {
        retryCount.increment();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return depth == request.depth &&
                Objects.equals(url, request.url) &&
                method == request.method &&
                Objects.equals(params, request.params) &&
                Objects.equals(headers, request.headers) &&
                Objects.equals(cookies, request.cookies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, method, depth, params, headers, cookies);
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + decodeUrl() + '\'' +
                ", method=" + method +
                ", depth=" + depth +
                ", params=" + params +
                ", headers=" + headers +
                ", cookies=" + cookies +
                ", extra=" + extra +
                ", priority=" + priority +
                ", retryCount=" + retryCount +
                '}';
    }

    /**
     * 请求方法
     */
    public enum Method {
        GET, POST, PUT, DELETE;

        public String getValue() {
            return toString();
        }
    }

    /**
     * 请求建造者
     */
    public static class Builder {
        private static final String COOKIE_HEADER_NAME = "Cookie";
        private String url;
        private Method method = Method.GET;
        private int depth = MIN_DEPTH;
        private Map<String, String> params;
        private Map<String, String> headers;
        private Map<String, String> cookies;
        private Object extra;
        private int priority;

        public static Builder of(String url) {
            return new Builder(url);
        }

        public static Builder of(Request request) {
            return of(request.url)
                    .method(request.method)
                    .depth(request.depth)
                    .params(request.params)
                    .headers(request.headers)
                    .cookies(request.cookies)
                    .extra(request.extra)
                    .priority(request.priority);
        }

        private Builder(String url) {
            this.url = Objects.requireNonNull(url);
        }

        public Builder method(Method method) {
            this.method = Objects.requireNonNull(method);
            return this;
        }

        public Builder depth(int depth) {
            if (depth < MIN_DEPTH)
                throw new IllegalArgumentException("depth不能小于" + MIN_DEPTH);
            this.depth = depth;
            return this;
        }

        public Builder param(String name, @Nullable Object value) {
            Objects.requireNonNull(name);
            initParams();
            params.put(name, value + "");
            return this;
        }

        public Builder params(Map<String, String> params) {
            Objects.requireNonNull(params);
            initParams();
            this.params.putAll(params);
            return this;
        }

        public Builder header(String name, @Nullable String value) {
            Objects.requireNonNull(name);
            initHeaders();
            headers.put(name, value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            if (headers != null) {
                initHeaders();
                this.headers.putAll(headers);
            }
            return this;
        }

        public Builder cookies(Map<String, String> cookies) {
            if (cookies != null) {
                initCookies();
                this.cookies.putAll(cookies);
            }
            return this;
        }

        public Builder cookie(String name, @Nullable String value) {
            Objects.requireNonNull(name);
            initCookies();
            this.cookies.put(name, value);
            return this;
        }

        public Builder extra(Object extra) {
            this.extra = extra;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Request build() {
            if (cookies != null) {
                initHeaders();
                headers.put(COOKIE_HEADER_NAME, getCookieHeader());
            }
            return new Request(url, method, depth, params,
                    headers, cookies, extra, priority);
        }

        private void initParams() {
            if (params == null)
                params = new HashMap<>();
        }

        private void initHeaders() {
            if (headers == null)
                headers = new HashMap<>();
        }

        private void initCookies() {
            if (cookies == null)
                cookies = new HashMap<>();
        }

        private String getCookieHeader() {
            return Optional.ofNullable(cookies)
                    .map(cookies -> cookies.entrySet()
                            .toArray(new Map.Entry[0]))
                    .map(cookies -> Arrays.stream(cookies)
                            .map(entry -> entry.getKey() + "=" + entry.getValue())
                            .collect(Collectors.toList()))
                    .map(cookies -> StringUtils.join(cookies, "; "))
                    .orElse(null);
        }
    }
}
