package org.ffpy.easyspider.core.mapper;

import org.ffpy.easyspider.core.exception.EasyCrawlerException;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public enum ContentType {
    TEXT((e, url) -> e.text()),
    HTML((e, url) -> e.html()),
    SRC((e, url) -> e.attr("src")),
    ABS_SRC((e, url) -> e.absUrl("src")),
    HREF((e, url) -> e.attr("href")),
    ABS_HREF((e, url) -> e.absUrl("href")),
    VALUE((e, url) -> e.val()),
    URL((e, url) -> {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }
    });

    private final Action action;

    ContentType(Action action) {
        this.action = action;
    }

    public String getValue(Element e, String url) {
        return action.getValue(e, url);
    }

    private interface Action {
        String getValue(Element e, String url);
    }
}
