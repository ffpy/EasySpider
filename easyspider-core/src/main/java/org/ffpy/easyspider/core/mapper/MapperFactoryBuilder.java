package org.ffpy.easyspider.core.mapper;

import org.ffpy.easyspider.core.downloader.Downloader;
import org.ffpy.easyspider.core.downloader.HttpDownloader;

public class MapperFactoryBuilder {
    private Downloader downloader;

    public MapperFactoryBuilder setDownloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public MapperFactory build() {
        return new MapperFactory(downloader == null ?
                downloader = HttpDownloader.Builder.of().build() :
                downloader);
    }
}
