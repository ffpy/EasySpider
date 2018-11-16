package org.ffpy.easyspider.core.utils;

import org.ffpy.easyspider.core.mapper.ContentType;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumUtilsTest {

    @Test
    public void testFromStr() {
        assertEquals(ContentType.HTML, EnumUtils.fromStr("html", ContentType.class));
        assertEquals(ContentType.HTML, EnumUtils.fromStr("HTML", ContentType.class));
        assertNull(EnumUtils.fromStr("html_", ContentType.class));
    }
}