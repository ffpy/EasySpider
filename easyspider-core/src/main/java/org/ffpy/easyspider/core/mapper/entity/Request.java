package org.ffpy.easyspider.core.mapper.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 请求
 */
@XStreamAlias("request")
public class Request {
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String url;
    @XStreamAsAttribute
    private String charset;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", charset='" + charset + '\'' +
                '}';
    }
}
