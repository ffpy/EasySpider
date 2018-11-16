package org.ffpy.easyspider.sample.baike;

import org.ffpy.easyspider.core.Counter;
import org.ffpy.easyspider.core.downloader.HttpDownloader;
import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.processor.ModelProcessor;
import org.ffpy.easyspider.core.spider.SpiderBuilder;
import org.ffpy.easyspider.core.urlfinder.PatternUrlFinder;
import org.junit.Test;

public class BaikeTest {

    @Test
    public void testItem() {
        final String baikeUrlPattern = "https://baike.baidu.com/item/.*/.*";
        new SpiderBuilder()
                .addUrl("https://baike.baidu.com/item/%E8%AE%A1%E7%AE%97%E6%9C%BA/140338?fr=aladdin")
//                .addUrl("https://baike.baidu.com/item/%E4%BF%97%E7%A7%B0/6538506")
//                .addUrl("https://baike.baidu.com/item/%E7%94%B5%E6%B0%94%E5%92%8C%E7%94%B5%E5%AD%90%E5%B7%A5%E7%A8%8B%E5%B8%88%E5%8D%8F%E4%BC%9A/7779533?fromtitle=IEEE&fromid=150905")
                .addDownloader(".*", HttpDownloader.Builder.of().threads(10).build())
                .addUrlFinder(baikeUrlPattern, new PatternUrlFinder("https?://baike.baidu.com/item/.*"))
                .addProcessor(baikeUrlPattern, new ModelProcessor<Item>(Baike.class, "item") {
                    private final Counter counter = new Counter();

                    @Override
                    public void process(Page page, Item item) {
                        item.setIntro(item.getIntro().substring(0,
                                Math.min(20, item.getIntro().length())));
                        System.out.println(page.request() + ": \n" + item);
                        counter.incrementSuccess();
                    }

                    @Override
                    public Counter counter() {
                        return counter;
                    }
                })
//                .addProcessor(".*", context ->
//                        System.out.println(context.getUrl() + ": " + context.getHtml()))
//                .setStopper((spider, task) ->
//                        spider.getProcessCount() >= 10 || task.getDepth() > 2)
                .setStopper((scheduler, task) -> task.depth() > 2)
                .build()
                .start();
    }
}
