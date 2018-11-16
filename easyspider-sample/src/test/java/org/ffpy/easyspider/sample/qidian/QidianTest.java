package org.ffpy.easyspider.sample.qidian;

import org.ffpy.easyspider.core.downloader.HttpDownloader;
import org.ffpy.easyspider.core.downloader.SeleniumDownloader;
import org.ffpy.easyspider.core.mapper.MapperFactoryBuilder;
import org.junit.Test;

import java.util.List;

public class QidianTest {
    private Qidain qidain;

    public QidianTest() {
        this.qidain = new MapperFactoryBuilder()
//                .setDownloader(HttpDownloader.Builder.of().build())
                .setDownloader(new SeleniumDownloader(
                        SeleniumDownloader.Driver.CHROME,
                        "D:\\Tool\\Driver\\chromedriver.exe"))
                .build()
                .create(Qidain.class);
    }

    @Test
    public void testBookInfo() {
        BookInfo bookInfo = qidain.getBookInfo("1013258708");
        System.out.println(bookInfo);
    }

    @Test
    public void testBookCatalog() {
        List<BookCatalog> bookCatalog = qidain.getBookCatalog("1013258708");
        System.out.println(bookCatalog);
    }

    @Test
    public void testBookScore() {
        BookScore bookScore = qidain.getBookScore("1013258708");
        System.out.println(bookScore);
    }
}
