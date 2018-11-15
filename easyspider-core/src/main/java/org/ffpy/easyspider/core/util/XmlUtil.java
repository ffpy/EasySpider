package org.ffpy.easyspider.core.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.ffpy.easyspider.core.mapper.entity.*;

import java.io.File;

public class XmlUtil {

    public static <T> T parseMappers(File file, Class<T> type) {
        XStream xStream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xStream);
        Class[] classes = {Mappers.class, Mapper.class, Property.class,
                Request.class, Sub.class};
        xStream.allowTypes(classes);
        xStream.processAnnotations(classes);
        return (T) xStream.fromXML(file);
    }
}
