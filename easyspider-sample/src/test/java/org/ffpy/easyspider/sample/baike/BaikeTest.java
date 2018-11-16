package org.ffpy.easyspider.sample.baike;

import org.ffpy.easyspider.core.downloader.HttpDownloader;
import org.ffpy.easyspider.core.downloader.SeleniumDownloader;
import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.processor.ModelProcessor;
import org.ffpy.easyspider.core.spider.SpiderBuilder;
import org.ffpy.easyspider.core.urlfinder.PatternUrlFinder;
import org.junit.Test;

public class BaikeTest {

    @Test
    public void testItem() {
        final String baikeUrlPattern = "https?://baike\\.baidu\\.com/item/.*";
        new SpiderBuilder()
                .addUrl("https://baike.baidu.com/item/%E8%AE%A1%E7%AE%97%E6%9C%BA/140338?fr=aladdin")
//                .addDownloader(HttpDownloader.Builder.of().threads(3).build())
                .addDownloader(SeleniumDownloader.Builder.of(
                        SeleniumDownloader.Driver.CHROME,
                        "D:\\Tool\\Driver\\chromedriver.exe")
                        .threads(3).build())
                .addUrlFinder(new PatternUrlFinder(baikeUrlPattern))
                .addProcessor(new ModelProcessor<Item>(Baike.class, "item") {

                    @Override
                    public void process(Page page, Item item) {
                        item.setIntro(item.getIntro().substring(0,
                                Math.min(20, item.getIntro().length())));
                        System.out.println(page.request() + ": \n" + item);
                    }
                })
                .setStopper((scheduler, task) -> task.depth() > 2)
                .build()
                .start();
    }
}
