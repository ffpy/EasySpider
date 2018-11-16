package org.ffpy.easyspider.core.mapper.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 子映射
 */
@XStreamAlias("sub")
public class SubNode {
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
        return "SubNode{" +
                "name='" + name + '\'' +
                ", selector='" + selector + '\'' +
                ", mapper='" + mapper + '\'' +
                '}';
    }
}
