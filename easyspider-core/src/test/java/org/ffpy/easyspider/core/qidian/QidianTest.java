package org.ffpy.easyspider.core.qidian;

import org.ffpy.easyspider.core.mapper.MapperFactoryBuilder;
import org.junit.Test;

import java.util.List;

public class QidianTest {
    private Qidain qidain;

    public QidianTest() {
        this.qidain = new MapperFactoryBuilder()
                .setDownloader((url, callback) -> callback.callback(TestResponse.getResponse()))
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
