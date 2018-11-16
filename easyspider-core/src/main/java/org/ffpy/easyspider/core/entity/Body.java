package org.ffpy.easyspider.core.entity;

import java.nio.charset.Charset;

/**
 * 响应体
 */
public final class Body {
    /** 响应数据 */
    private final byte[] data;
    /** 字符编码 */
    private final Charset charset;


    public Body(byte[] data, Charset charset) {
        this.data = data;
        this.charset = charset;
    }

    public byte[] bytes() {
        return data;
    }

    public String string() {
        return new String(data, charset);
    }

    public Charset charset() {
        return charset;
    }

    @Override
    public String toString() {
        return string();
    }
}
