package org.ffpy.easyspider.core.downloader;

import org.ffpy.easyspider.core.Counter;
import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.entity.Response;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 简单下载器
 */
public class HttpDownloader implements Downloader {
    /** 请求方法 */
    private static final String HTTP_METHOD = "GET";
    /** 超时时间（毫秒） */
    private static final int HTTP_TIMEOUT = 5000;
    /** UA头名称 */
    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    /** 代理验证头名称 */
    private static final String PROXY_HEADER_NAME = "Proxy-Authorization";
    /** 请求的UA */
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";

    /** 线程池 */
    private final Executor executor;
    /** 代理 */
    private final Proxy proxy;
    /** 代理验证 */
    private final byte[] proxyAuth;
    private final Counter counter = new Counter();

    private HttpDownloader(Executor executor, Proxy proxy, byte[] proxyAuth) {
        this.executor = executor;
        this.proxy = proxy;
        this.proxyAuth = proxyAuth;
    }

    @Override
    public void download(Request request, Callback callback) throws Exception {
        executor.execute(() -> {
            try {
                downloadAction(request, callback);
            } catch (Exception e) {
                counter.incrementFailed();
                e.printStackTrace();
            }
        });
    }

    @Override
    public Counter counter() {
        return counter;
    }

    private void downloadAction(Request request, Callback callback) throws Exception {
        // 创建连接
        URL url = new URL(request.url());
        HttpURLConnection conn = (HttpURLConnection) (proxy == null ?
                url.openConnection() : url.openConnection(proxy));
        // 设置连接信息
        conn.setRequestMethod(HTTP_METHOD);
        conn.setConnectTimeout(HTTP_TIMEOUT);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(true);
        conn.setRequestProperty(USER_AGENT_HEADER_NAME, USER_AGENT);
        // 设置代理验证头
        if (proxy != null && proxyAuth != null) {
            String proxyAuthHeaderValue = "Base " +
                    Base64.getEncoder().encodeToString(proxyAuth);
            conn.setRequestProperty(PROXY_HEADER_NAME, proxyAuthHeaderValue);
        }
        // 设置请求头
        Optional.ofNullable(request.headers())
                .ifPresent(headers -> headers.forEach(conn::setRequestProperty));
        // 发送请求
        conn.connect();
        // 构造响应体
        Response response = Response.Builder
                .of(conn.getResponseCode(), conn.getInputStream())
                .headers(conn.getHeaderFields())
                .build();
        // 计数
        counter.incrementSuccess();
        // 回调
        callback.callback(response);
    }

    public static class Builder {
        private int threads;
        private Proxy proxy;
        private byte[] proxyAuth;

        public static Builder of() {
            return new Builder();
        }

        private Builder() {
        }

        public Builder threads(int n) {
            if (n < 1)
                throw new IllegalArgumentException("线程数不能小于1");
            this.threads = n;
            return this;
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = Objects.requireNonNull(proxy);
            return this;
        }

        public Builder proxy(String host, int port) {
            Objects.requireNonNull(host);
            proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
            return this;
        }

        public Builder proxyAuth(String username, String password) {
            Objects.requireNonNull(username);
            Objects.requireNonNull(password);
            this.proxyAuth = (username + ":" + password).getBytes();
            return this;
        }

        public HttpDownloader build() {
            Executor executor = threads < 1 ?
                    Executors.newSingleThreadExecutor() :
                    Executors.newFixedThreadPool(threads);
            return new HttpDownloader(executor, proxy, proxyAuth);
        }
    }
}
