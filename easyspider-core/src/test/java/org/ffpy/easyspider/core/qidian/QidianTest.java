package org.ffpy.easyspider.core.qidian;

import org.ffpy.easyspider.core.mapper.MapperFactoryBuilder;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class QidianTest {
    private Qidain qidain;

    public QidianTest() {
        this.qidain = new MapperFactoryBuilder()
                .setDownloader((url, callback) -> callback.callback(getHtml()))
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

    private String getHtml() throws IOException {
        Reader reader = new FileReader(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "\\qidian.html");
        BufferedReader br = new BufferedReader(reader);
        StringBuilder html = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            html.append(s);
        }
        return html.toString();
    }
}
