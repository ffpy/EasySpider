package org.ffpy.easyspider.core.mapper.node;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * 映射
 */
@XStreamAlias("mapper")
public class MapperNode {
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String request;
    @XStreamAsAttribute
    private String itemSelector;
    @XStreamImplicit
    private List<PropertyNode> propertyNodeList;
    @XStreamImplicit
    private List<SubNode> subNodeList;

    public String getId() {
        return id;
    }

    public String getRequest() {
        return request;
    }

    public String getItemSelector() {
        return itemSelector;
    }

    public List<PropertyNode> getPropertyNodeList() {
        return propertyNodeList;
    }

    public List<SubNode> getSubNodeList() {
        return subNodeList;
    }

    @Override
    public String toString() {
        return "MapperNode{" +
                "id='" + id + '\'' +
                ", request='" + request + '\'' +
                ", itemSelector='" + itemSelector + '\'' +
                ", propertyNodeList=" + propertyNodeList +
                ", subNodeList=" + subNodeList +
                '}';
    }
}
