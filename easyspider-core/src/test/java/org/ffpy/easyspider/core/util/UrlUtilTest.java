package org.ffpy.easyspider.core.util;

import org.junit.Test;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UrlUtilTest {

    @Test
    public void testGetBaseUri() throws MalformedURLException {
        assertEquals("http://www.baidu.com", UrlUtil.getBaseUri("http://www.baidu.com/a.html"));
        assertEquals("https://www.baidu.com", UrlUtil.getBaseUri("https://www.baidu.com/a.html"));
        assertEquals("https://www.baidu.com", UrlUtil.getBaseUri("https://www.baidu.com"));

    }

    @Test
    public void testParseUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("name", "jack");
        assertEquals("www.a.com/jack/1",
                UrlUtil.parseUrl("www.a.com/{name}/{id}", params));
    }
}