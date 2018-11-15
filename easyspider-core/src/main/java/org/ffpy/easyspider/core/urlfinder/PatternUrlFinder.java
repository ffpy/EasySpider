package org.ffpy.easyspider.core.urlfinder;

import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.util.StringUtil;
import org.ffpy.easyspider.core.util.UrlUtil;
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
    public void find(Context context, Callback callback) throws Exception {
        Document doc = Jsoup.parse(context.getHtml(),
                UrlUtil.getBaseUri(context.getUrl()));
        List<String> urls = new LinkedList<>();
        doc.select("a, img").forEach(element -> {
            String s = element.absUrl("href");
            if (StringUtil.isEmpty(s))
                s = element.absUrl("src");
            if (StringUtil.isEmpty(s) || s.startsWith("javascript"))
                return;
            if (s.matches(pattern))
                urls.add(s);
        });
        callback.callback(urls);
    }
}
