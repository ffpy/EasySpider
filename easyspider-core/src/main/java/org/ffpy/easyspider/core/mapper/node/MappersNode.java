package org.ffpy.easyspider.core.mapper.node;

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
public class MappersNode {
    @XStreamAsAttribute
    private String namespace;
    @XStreamImplicit
    private List<RequestNode> requestNodeList;
    @XStreamImplicit()
    private List<MapperNode> mapperNodeList;
    private Map<String, RequestNode> requestMap;
    private Map<String, MapperNode> mapperMap;

    public String getNamespace() {
        return namespace;
    }

    public List<RequestNode> getRequestNodeList() {
        return requestNodeList;
    }

    public List<MapperNode> getMapperNodeList() {
        return mapperNodeList;
    }

    public RequestNode getRequest(String id) {
        RequestNode requestNode = requestMap.get(id);
        if (requestNode == null)
            throw new EasyCrawlerException("在" + namespace + "中没有找到id为" + id + "的request");
        return requestNode;
    }

    public void putRequest(RequestNode requestNode) {
        RequestNode r = requestMap.get(requestNode.getId());
        if (r != null)
            throw new EasyCrawlerException("id为" + requestNode.getId()
                    + "的request出现重复");
        requestMap.put(requestNode.getId(), requestNode);
    }

    public MapperNode getMapper(String id) {
        MapperNode mapperNode = mapperMap.get(id);
        if (mapperNode == null)
            throw new EasyCrawlerException("在" + namespace + "中没有找到id为" + id + "的mapper");
        return mapperNode;
    }

    public void putMapper(MapperNode mapperNode) {
        MapperNode m = mapperMap.get(mapperNode.getId());
        if (m != null)
            throw new EasyCrawlerException("id为" + mapperNode.getId()
                    + "的mapper出现重复");
        mapperMap.put(mapperNode.getId(), mapperNode);
    }

    public void initMap() {
        initRequestMap();
        initMapperMap();
    }

    private void initRequestMap() {
        requestMap = new HashMap<>();
        if (requestNodeList == null) return;
        for (RequestNode requestNode : requestNodeList) {
            requestMap.put(requestNode.getId(), requestNode);
        }
        requestNodeList = null;
    }

    private void initMapperMap() {
        mapperMap = new HashMap<>();
        if (mapperNodeList == null) return;
        for (MapperNode mapperNode : mapperNodeList) {
            mapperMap.put(mapperNode.getId(), mapperNode);
        }
        mapperNodeList = null;
    }

    @Override
    public String toString() {
        return "MappersNode{" +
                "namespace='" + namespace + '\'' +
                ", requestNodeList=" + requestNodeList +
                ", mapperNodeList=" + mapperNodeList +
                '}';
    }
}
