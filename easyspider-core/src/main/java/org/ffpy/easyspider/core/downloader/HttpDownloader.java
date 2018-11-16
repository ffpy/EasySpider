package org.ffpy.easyspider.core.downloader;

import com.sun.istack.internal.Nullable;
import org.ffpy.easyspider.core.entity.Request;
import org.ffpy.easyspider.core.entity.Response;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

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

    /** 代理 */
    private final Proxy proxy;
    /** 代理验证 */
    private final byte[] proxyAuth;

    public HttpDownloader() {
        this.proxy = null;
        this.proxyAuth = null;
    }

    public HttpDownloader(String proxyHost, int proxyPort) {
        this(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
    }

    public HttpDownloader(Proxy proxy) {
        this.proxy = Objects.requireNonNull(proxy);
        this.proxyAuth = null;
    }

    public HttpDownloader(String proxyHost, int proxyPort, String proxyUsername,
                          String proxyPassword) {
        this(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)),
                proxyUsername, proxyPassword);
    }

    public HttpDownloader(Proxy proxy, @Nullable String proxyUsername,
                          @Nullable String proxyPassword) {
        this.proxy = Objects.requireNonNull(proxy);
        this.proxyAuth = (proxyUsername + ":" + proxyPassword).getBytes();
    }

    @Override
    public void download(Request request, Callback callback) throws Exception {
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
        // 回调
        callback.callback(response);
    }
}
