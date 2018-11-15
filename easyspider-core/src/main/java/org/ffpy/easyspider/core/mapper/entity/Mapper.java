package org.ffpy.easyspider.core.mapper.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * 映射
 */
@XStreamAlias("mapper")
public class Mapper {
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String request;
    @XStreamAsAttribute
    private String itemSelector;
    @XStreamImplicit
    private List<Property> propertyList;
    @XStreamImplicit
    private List<Sub> subList;

    public String getId() {
        return id;
    }

    public String getRequest() {
        return request;
    }

    public String getItemSelector() {
        return itemSelector;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public List<Sub> getSubList() {
        return subList;
    }

    @Override
    public String toString() {
        return "Mapper{" +
                "id='" + id + '\'' +
                ", request='" + request + '\'' +
                ", itemSelector='" + itemSelector + '\'' +
                ", propertyList=" + propertyList +
                ", subList=" + subList +
                '}';
    }
}
