package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.entity.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class DocumentProcessor implements Processor {

    public abstract void process(Page page, Document doc) throws Exception;

    @Override
    public void process(Page page) throws Exception {
        process(page, Jsoup.parse(page.string()));
    }
}
