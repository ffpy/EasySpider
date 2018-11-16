package org.ffpy.easyspider.core.mapper.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.ffpy.easyspider.core.mapper.ContentType;
import org.ffpy.easyspider.core.utils.EnumUtils;

/**
 * 属性配置
 */
@XStreamAlias("property")
public class PropertyNode {
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String selector;
    @XStreamAsAttribute
    private String content;
    @XStreamAsAttribute
    private String pattern;
    @XStreamAsAttribute
    private String format;

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
        return EnumUtils.fromStr(content, ContentType.class);
    }

    public String getPattern() {
        return pattern;
    }

    public String getFormat() {
        return format;
    }
    @Override
    public String toString() {
        return "PropertyNode{" +
                "name='" + name + '\'' +
                ", selector='" + selector + '\'' +
                ", content='" + content + '\'' +
                ", pattern='" + pattern + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
