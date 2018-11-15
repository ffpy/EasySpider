package org.ffpy.easyspider.core.mapper.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.ffpy.easyspider.core.mapper.ContentType;
import org.ffpy.easyspider.core.util.EnumUtil;

/**
 * 属性配置
 */
@XStreamAlias("property")
public class Property {
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String selector;
    @XStreamAsAttribute
    private String content;
    @XStreamAsAttribute
    private String pattern;

    public String getName() {
        return name;
    }

    public String getSelector() {
        return selector;
    }

    public String getContent() {
        return content;
    }

    public ContentType getContentType() {
        return EnumUtil.fromStr(content, ContentType.class);
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", selector='" + selector + '\'' +
                ", content='" + content + '\'' +
                ", pattern='" + pattern + '\'' +
                '}';
    }
}
