package org.ffpy.easyspider.core.mapper;

import org.ffpy.easyspider.core.exception.EasyCrawlerException;
import org.ffpy.easyspider.core.mapper.entity.Mappers;
import org.ffpy.easyspider.core.util.XmlUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperManager {
    private static final String FILENAME_PREFIX = "easyspider-";
    private static final String FILENAME_SUFFIX = ".xml";

    private static final Map<String, Mappers> mappersMap = new ConcurrentHashMap<>();

    static {
        init();
    }

    private static void init() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File classpathFile = new File(path);
        File[] files = classpathFile.listFiles();
        if (files == null) return;

        for (File f : files) {
            String filename = f.getName();
            if (f.isFile() && filename.startsWith(FILENAME_PREFIX) &&
                    filename.endsWith(FILENAME_SUFFIX)) {
                Mappers mappers = XmlUtil.parseMappers(f, Mappers.class);
                mappersMap.put(mappers.getNamespace(), mappers);
            }
        }
    }

    public static Mappers getMappers(Class<?> namespace) {
        return getMappers(namespace.getName());
    }

    public static Mappers getMappers(String namespace) {
        Mappers mappers = mappersMap.get(namespace);
        if (mappers == null)
            throw new EasyCrawlerException("没有找到namespace为" + namespace + "对应的mappers");
        return mappers;
    }
}
