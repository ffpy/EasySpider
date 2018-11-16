package org.ffpy.easyspider.core.urlfinder;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.utils.UrlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.LinkedList;
import java.util.List;

/**
 * 模式URL查找器
 */
public class PatternUrlFinder implements UrlFinder {
    private final String pattern;

    public PatternUrlFinder() {
        this(".*");
    }

    /**
     * @param urlPattern URL模式
     */
    public PatternUrlFinder(String urlPattern) {
        this.pattern = urlPattern;
    }

    @Override
    public void find(Page page, Callback callback) throws Exception {
        Document doc = Jsoup.parse(page.string(),
                UrlUtils.getBaseUri(page.url()));
        List<String> urls = new LinkedList<>();
        doc.select("a, img").forEach(element -> {
            String s = element.absUrl("href");
            if (StringUtils.isEmpty(s))
                s = element.absUrl("src");
            if (StringUtils.isEmpty(s) || s.startsWith("javascript"))
                return;
            if (s.matches(pattern))
                urls.add(s);
        });
        callback.callback(urls);
    }
}
