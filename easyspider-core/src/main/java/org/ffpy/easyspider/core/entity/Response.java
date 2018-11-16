package org.ffpy.easyspider.core.entity;

import com.sun.istack.internal.Nullable;
import org.ffpy.easyspider.core.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class Response {
    public static final int OK = 200;

    private final int responseCode;
    private final byte[] data;
    private Map<String, List<String>> headers;

    private Response(int responseCode, byte[] data, @Nullable Map<String, List<String>> headers) {
        this.responseCode = responseCode;
        this.data = data;
        this.headers = headers;
    }

    public int responseCode() {
        return responseCode;
    }

    public byte[] bytes() {
        return data;
    }

    public String string() {
        return new String(data);
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(data);
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public String header(String name) {
        return Optional.ofNullable(headers)
                .map(headers::get)
                .map(list -> list.get(0))
                .orElse(null);
    }

    public List<String> listHeader(String name) {
        if (headers == null) return null;
        return headers.get(name);
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseCode=" + responseCode +
                ", data=" + string() +
                ", headers=" + headers +
                '}';
    }

    public static class Builder {
        private int responseCode;
        private byte[] data;
        private Map<String, List<String>> headers;

        public static Builder of(
                int responseCode, @Nullable byte[] data) {
            return new Builder(responseCode, data);
        }

        public static Builder of(
                int responseCode, @Nullable String data) {
            return new Builder(responseCode, data == null ? null : data.getBytes());
        }

        public static Builder of(
                int responseCode, @Nullable InputStream inputStream) throws IOException {
            return of(responseCode, inputStream == null ? null :
                    IOUtils.inputStream2Bytes(inputStream));
        }

        private Builder(int responseCode, @Nullable byte[] data) {
            this.responseCode = responseCode;
            this.data = data;
        }

        public Builder headers(Map<String, List<String>> headers) {
            Objects.requireNonNull(headers);
            initHeaders();
            this.headers.putAll(headers);
            return this;
        }

        public Builder header(String name, @Nullable String value) {
            Objects.requireNonNull(name);
            initHeaders();
            List<String> list = new ArrayList<>(1);
            list.add(value);
            this.headers.put(name, list);
            return this;
        }

        public Builder header(String name, @Nullable List<String> value) {
            Objects.requireNonNull(name);
            initHeaders();
            this.headers.put(name, value);
            return this;
        }

        public Response build() {
            return new Response(responseCode, data, headers);
        }

        private void initHeaders() {
            if (headers == null)
                headers = new HashMap<>();
        }
    }
}
