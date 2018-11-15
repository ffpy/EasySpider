package org.ffpy.easyspider.core.mapper;

import org.junit.Test;

public class MapperManagerTest {

    @Test
    public void testInit() throws ClassNotFoundException {
        Class.forName(MapperManager.class.getName());
    }
}