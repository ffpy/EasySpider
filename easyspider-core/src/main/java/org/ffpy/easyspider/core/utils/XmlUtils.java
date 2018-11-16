package org.ffpy.easyspider.core.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.ffpy.easyspider.core.mapper.node.*;

import java.io.File;

public class XmlUtils {

    public static <T> T parseMappers(File file, Class<T> type) {
        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        Class[] classes = {MappersNode.class, MapperNode.class, PropertyNode.class,
                RequestNode.class, SubNode.class};
        xStream.allowTypes(classes);
        xStream.processAnnotations(classes);
        return (T) xStream.fromXML(file);
    }
}
