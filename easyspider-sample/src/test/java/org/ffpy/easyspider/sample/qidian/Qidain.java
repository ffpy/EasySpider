package org.ffpy.easyspider.sample.qidian;

import org.ffpy.easyspider.core.mapper.Param;

import java.util.List;

public interface Qidain {

    /**
     * 获取图书信息
     *
     * @param id 图书ID
     * @return 图书信息
     */
    BookInfo getBookInfo(@Param("id") String bookId);

    /**
     * 获取图书目录
     *
     * @param id 图书ID
     * @return 图书目录
     */
    List<BookCatalog> getBookCatalog(@Param("id") String id);

    BookScore getBookScore(@Param("id") String id);
}
