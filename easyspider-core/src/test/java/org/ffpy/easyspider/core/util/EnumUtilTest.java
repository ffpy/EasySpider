package org.ffpy.easyspider.core.util;

import org.ffpy.easyspider.core.mapper.ContentType;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnumUtilTest {

    @Test
    public void testFromStr() {
        assertEquals(ContentType.HTML, EnumUtil.fromStr("html", ContentType.class));
        assertEquals(ContentType.HTML, EnumUtil.fromStr("HTML", ContentType.class));
        assertNull(EnumUtil.fromStr("html_", ContentType.class));
    }
}