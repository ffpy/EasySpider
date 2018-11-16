package org.ffpy.easyspider.core.utils;

import org.junit.Test;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UrlUtilsTest {

    @Test
    public void testGetBaseUri() throws MalformedURLException {
        assertEquals("http://www.baidu.com", UrlUtils.getBaseUri("http://www.baidu.com/a.html"));
        assertEquals("https://www.baidu.com", UrlUtils.getBaseUri("https://www.baidu.com/a.html"));
        assertEquals("https://www.baidu.com", UrlUtils.getBaseUri("https://www.baidu.com"));

    }

    @Test
    public void testParseUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("name", "jack");
        assertEquals("www.a.com/jack/1",
                UrlUtils.parseUrl("www.a.com/{name}/{id}", params));
    }
}