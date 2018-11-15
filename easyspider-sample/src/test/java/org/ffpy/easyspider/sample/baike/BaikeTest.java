package org.ffpy.easyspider.sample.baike;

import org.ffpy.easyspider.core.downloader.HttpDownloader;
import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.processor.ObjectProcessor;
import org.ffpy.easyspider.core.scheduler.SchedulerBuilder;
import org.ffpy.easyspider.core.urlfinder.PatternUrlFinder;
import org.junit.Test;

public class BaikeTest {

    @Test
    public void testItem() {
        final String baikeUrlPattern = "https://baike.baidu.com/item/.*/.*";
        new SchedulerBuilder()
//                .addUrl("https://baike.baidu.com/item/%E8%AE%A1%E7%AE%97%E6%9C%BA/140338?fr=aladdin")
//                .addUrl("https://baike.baidu.com/item/%E4%BF%97%E7%A7%B0/6538506")
                .addUrl("https://baike.baidu.com/item/%E7%94%B5%E6%B0%94%E5%92%8C%E7%94%B5%E5%AD%90%E5%B7%A5%E7%A8%8B%E5%B8%88%E5%8D%8F%E4%BC%9A/7779533?fromtitle=IEEE&fromid=150905")
                .addDownloader(".*", new HttpDownloader())
                .addUrlFinder(baikeUrlPattern, new PatternUrlFinder("https?://baike.baidu.com/item/.*"))
                .addProcessor(baikeUrlPattern, new ObjectProcessor<Item>(Baike.class, "item") {
                    @Override
                    public void process(Context context, Item item) {
                        item.setIntro(item.getIntro().substring(0,
                                Math.min(20, item.getIntro().length())));
                        System.out.println(context.getTask().getDepth() + ": " + item);
                    }
                })
//                .addProcessor(".*", context ->
//                        System.out.println(context.getUrl() + ": " + context.getHtml()))
//                .setStopper((scheduler, task) ->
//                        scheduler.getProcessCount() >= 10 || task.getDepth() > 2)
                .setStopper((scheduler, task) -> task.getDepth() > 2)
                .build()
                .start();
    }
}
