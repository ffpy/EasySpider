package org.ffpy.easyspider.core.mapper;

import org.jsoup.nodes.Element;

public enum ContentType {
    TEXT(Element::text),
    HTML(Element::html),
    SRC(e -> e.attr("src")),
    ABS_SRC(e -> e.absUrl("src")),
    HREF(e -> e.attr("href")),
    ABS_HREF(e -> e.absUrl("href")),
    VALUE(Element::val);

    private final Action action;

    ContentType(Action action) {
        this.action = action;
    }

    public String getValue(Element e) {
        return action.getValue(e);
    }

    private interface Action {
        String getValue(Element e);
    }
}
