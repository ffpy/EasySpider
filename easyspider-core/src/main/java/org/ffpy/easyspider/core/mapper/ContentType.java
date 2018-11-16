package org.ffpy.easyspider.core.mapper;

import org.ffpy.easyspider.core.utils.UrlUtils;
import org.jsoup.nodes.Element;

public enum ContentType {
    TEXT((e, url) -> e.text()),
    HTML((e, url) -> e.html()),
    SRC((e, url) -> e.attr("src")),
    ABS_SRC((e, url) -> e.absUrl("src")),
    HREF((e, url) -> e.attr("href")),
    ABS_HREF((e, url) -> e.absUrl("href")),
    VALUE((e, url) -> e.val()),
    URL((e, url) -> UrlUtils.decodeUrl(url));

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
