package org.ffpy.easyspider.core.mapper.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 子映射
 */
@XStreamAlias("sub")
public class Sub {
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String selector;
    @XStreamAsAttribute
    private String mapper;

    public String getName() {
        return name;
    }

    public String getSelector() {
        return selector;
    }

    public String getMapper() {
        return mapper;
    }

    @Override
    public String toString() {
        return "Sub{" +
                "name='" + name + '\'' +
                ", selector='" + selector + '\'' +
                ", mapper='" + mapper + '\'' +
                '}';
    }
}
