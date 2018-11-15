package org.ffpy.easyspider.core.mapper.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.ffpy.easyspider.core.exception.EasyCrawlerException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射集合
 */
@XStreamAlias("mappers")
public class Mappers {
    @XStreamAsAttribute
    private String namespace;
    @XStreamImplicit
    private List<Request> requestList;
    @XStreamImplicit()
    private List<Mapper> mapperList;
    private Map<String, Request> requestMap;
    private Map<String, Mapper> mapperMap;

    public String getNamespace() {
        return namespace;
    }

    public Request getRequest(String id) {
        if (requestMap == null)
            initRequestMap();
        Request request = requestMap.get(id);
        if (request == null)
            throw new EasyCrawlerException("在" + namespace + "中没有找到id为" + id + "的request");
        return request;
    }

    public Mapper getMapper(String id) {
        if (mapperMap == null)
            initMapperMap();
        Mapper mapper = mapperMap.get(id);
        if (mapper == null)
            throw new EasyCrawlerException("在" + namespace + "中没有找到id为" + id + "的mapper");
        return mapper;
    }

    private void initRequestMap() {
        requestMap = new HashMap<>();
        if (requestList == null) return;
        for (Request request : requestList) {
            requestMap.put(request.getId(), request);
        }
        requestList = null;
    }

    private void initMapperMap() {
        mapperMap = new HashMap<>();
        if (mapperList == null) return;
        for (Mapper mapper : mapperList) {
            mapperMap.put(mapper.getId(), mapper);
        }
        mapperList = null;
    }

    @Override
    public String toString() {
        return "Mappers{" +
                "namespace='" + namespace + '\'' +
                ", requestList=" + requestList +
                ", mapperList=" + mapperList +
                '}';
    }
}
