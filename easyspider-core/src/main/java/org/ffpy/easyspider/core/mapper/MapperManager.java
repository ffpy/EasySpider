package org.ffpy.easyspider.core.mapper;

import org.ffpy.easyspider.core.exception.EasyCrawlerException;
import org.ffpy.easyspider.core.mapper.node.MapperNode;
import org.ffpy.easyspider.core.mapper.node.MappersNode;
import org.ffpy.easyspider.core.mapper.node.RequestNode;
import org.ffpy.easyspider.core.utils.XmlUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperManager {
    private static final String FILENAME_PREFIX = "easyspider-";
    private static final String FILENAME_SUFFIX = ".xml";

    private static final Map<String, MappersNode> mappersMap = new ConcurrentHashMap<>();

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
                MappersNode mappersNode = XmlUtils.parseMappers(f, MappersNode.class);
                MappersNode m = mappersMap.get(mappersNode.getNamespace());
                if (m != null) {
                    for (RequestNode requestNode : mappersNode.getRequestNodeList()) {
                        m.putRequest(requestNode);
                    }
                    for (MapperNode mapperNode : mappersNode.getMapperNodeList()) {
                        m.putMapper(mapperNode);
                    }
                } else {
                    mappersNode.initMap();
                    mappersMap.put(mappersNode.getNamespace(), mappersNode);
                }
            }
        }
    }

    public static MappersNode getMappers(Class<?> namespace) {
        return getMappers(namespace.getName());
    }

    public static MappersNode getMappers(String namespace) {
        MappersNode mappersNode = mappersMap.get(namespace);
        if (mappersNode == null)
            throw new EasyCrawlerException("没有找到namespace为" + namespace + "对应的mappers");
        return mappersNode;
    }
}
