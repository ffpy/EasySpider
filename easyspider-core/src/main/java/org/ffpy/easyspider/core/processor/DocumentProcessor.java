package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.entity.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class DocumentProcessor implements Processor {

    public abstract void process(Context context, Document doc) throws Exception;

    @Override
    public void process(Context context) throws Exception {
        process(context, Jsoup.parse(context.getHtml()));
    }
}
