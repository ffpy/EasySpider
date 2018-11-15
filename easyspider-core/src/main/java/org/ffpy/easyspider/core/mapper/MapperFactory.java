package org.ffpy.easyspider.core.mapper;

import org.ffpy.easyspider.core.downloader.Downloader;

import java.util.Objects;

/**
 * 映射工厂
 */
public class MapperFactory {
    private final Downloader downloader;

    MapperFactory(Downloader downloader) {
        this.downloader = Objects.requireNonNull(downloader);
    }

    public Downloader getDownloader() {
        return downloader;
    }

    public <T> T create(Class<T> mapperType) {
        return MapperProxy.of(this, mapperType);
    }
}
