package org.ffpy.easyspider.core.helper;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.exception.EasyCrawlerException;
import org.ffpy.easyspider.core.mapper.ContentType;
import org.ffpy.easyspider.core.mapper.MapperManager;
import org.ffpy.easyspider.core.mapper.node.MapperNode;
import org.ffpy.easyspider.core.mapper.node.MappersNode;
import org.ffpy.easyspider.core.mapper.node.PropertyNode;
import org.ffpy.easyspider.core.mapper.node.SubNode;
import org.ffpy.easyspider.core.utils.ObjectUtils;
import org.ffpy.easyspider.core.utils.UrlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 映射辅助类
 *
 * @param <T> bean类型
 */
public final class MapperHelper<T> {
    private final MappersNode mappersNode;
    private final MapperNode mapperNode;
    /** bean类型 */
    private final Class<T> type;
    /** bean类型的泛型 */
    private ParameterizedType parameterizedType;

    /**
     * 构造MapperHelper
     *
     * @param namespace 命名空间
     * @param id        mapper的ID
     * @param type      bean类型
     * @param <T>       bean类型
     * @return MapperHelper
     */
    public static <T> MapperHelper<T> of(String namespace, String id, Class<T> type) {
        return new MapperHelper<>(namespace, id, type);
    }

    /**
     * 构造MapperHelper
     *
     * @param namespace 命名空间
     * @param id        mapper的ID
     * @param type      bean类型
     * @param <T>       bean类型
     * @return MapperHelper
     */
    public static <T> MapperHelper<T> of(Class<?> namespace, String id, Class<T> type) {
        return new MapperHelper<>(namespace.getName(), id, type);
    }

    /**
     * @param namespace 命名空间
     * @param id        mapper的ID
     * @param type      bean类型
     * @return MapperHelper
     */
    private MapperHelper(String namespace, String id, Class<T> type) {
        this.mappersNode = MapperManager.getMappers(namespace);
        this.mapperNode = mappersNode.getMapper(id);
        this.type = type;
    }

    /**
     * 设置bean的泛型类型
     *
     * @param parameterizedType 泛型类型
     * @return this
     */
    public MapperHelper<T> parameterizedType(ParameterizedType parameterizedType) {
        this.parameterizedType = parameterizedType;
        return this;
    }

    /**
     * 创建Bean
     *
     * @param page 上下文
     * @return Bean实例
     */
    public T toObj(Page page) {
        return toObj(page.string(), page.url());
    }

    /**
     * 创建Bean
     *
     * @param html HTML页面
     * @param url  HTML的URL
     * @return Bean实例
     */
    public T toObj(String html, String url) {
        try {
            return toObj(Jsoup.parse(html, UrlUtils.getBaseUri(url)), url);
        } catch (MalformedURLException e) {
            throw new EasyCrawlerException(e);
        }
    }

    /**
     * 创建Bean
     *
     * @param element 页面文档
     * @return Bean实例
     */
    public T toObj(Element element, String url) {
        try {
            if (type == List.class) {
                final Class<?> subType = parameterizedType == null ?
                        ObjectUtils.getGenericType(type) :
                        ObjectUtils.getGenericType(parameterizedType);
                //noinspection unchecked
                return (T) createList(mapperNode, subType, element, url);
            } else {
                final T obj = type.newInstance();
                fillProperties(obj, mapperNode, element, url);
                fillSubs(obj, mapperNode, element, url);
                return obj;
            }
        } catch (Exception e) {
            throw new EasyCrawlerException(e);
        }
    }

    /**
     * 填充Bean的属性
     *  @param obj        Bean实例
     * @param mapperNode 映射
     * @param element    页面文档
     * @param url
     */
    private void fillProperties(Object obj, MapperNode mapperNode, Element element, String url) {
        List<PropertyNode> propertyNodeList = mapperNode.getPropertyNodeList();
        if (propertyNodeList == null) return;

        for (PropertyNode propertyNode : propertyNodeList) {
            try {
                final Field field = type.getDeclaredField(propertyNode.getName());

                final ContentType contentType = propertyNode.getContentType();
                if (contentType == null)
                    throw new EasyCrawlerException("无效的content: " + propertyNode.getContent() + "。");

                Element e = null;
                if (contentType != ContentType.URL) {
                    if (StringUtils.isEmpty(propertyNode.getSelector()))
                        throw new EasyCrawlerException("selector不能为空");
                    e = element.selectFirst(propertyNode.getSelector());
                    if (e == null) continue;
                }

                // 获取值
                String value = contentType.getValue(e, url);
                if (StringUtils.isNotEmpty(value) &&
                        StringUtils.isNotEmpty(propertyNode.getPattern())) {
                    value = PatternHelper.of(propertyNode.getPattern()).matcher(value).group(1);
                }
                // 设置值
                field.setAccessible(true);
                field.set(obj, ObjectUtils.format(field.getType(), value, propertyNode.getFormat()));
            } catch (NoSuchFieldException ignored) {
            } catch (IllegalAccessException e) {
                throw new EasyCrawlerException("注入属性失败！", e);
            }
        }
    }

    /**
     * 填充Bean的子映射
     *  @param obj        Bean实例
     * @param mapperNode 映射
     * @param element    页面文档
     * @param url
     */
    private void fillSubs(Object obj, MapperNode mapperNode, Element element, String url) {
        List<SubNode> subNodeList = mapperNode.getSubNodeList();
        if (subNodeList == null) return;
        for (SubNode subNode : subNodeList) {
            try {
                final Field field = type.getDeclaredField(subNode.getName());
                final Element e = StringUtils.isEmpty(subNode.getSelector()) ?
                        element : element.selectFirst(subNode.getSelector());
                final MapperNode subMapperNode = mappersNode.getMapper(subNode.getMapper());

                field.setAccessible(true);
                // 列表
                if (field.getType() == List.class) {
                    Class<?> subType = ObjectUtils.getGenericType(field);
                    field.set(obj, createList(subMapperNode, subType, e, url));
                }
                // 单个
                else {
                    Object value = of(mappersNode.getNamespace(), subMapperNode.getId(),
                            field.getType()).toObj(e, url);
                    field.set(obj, value);
                }
            } catch (NoSuchFieldException ignored) {
            } catch (IllegalAccessException e) {
                throw new EasyCrawlerException("注入属性失败！", e);
            }
        }
    }

    /**
     * 创建Bean列表
     *
     * @param mapperNode 映射
     * @param type       类型
     * @param element    页面文档
     * @param url
     * @return Bean列表
     */
    private List<Object> createList(MapperNode mapperNode, Class<?> type,
                                    Element element, String url) {
        String itemSelector = mapperNode.getItemSelector();
        if (StringUtils.isEmpty(itemSelector))
            throw new EasyCrawlerException(mapperNode.getId() + "的itemSelector属性不能为空");

        Elements elements = element.select(itemSelector);
        List<Object> list = new ArrayList<>(elements.size());
        // 添加项
        elements.forEach(itemElement -> {
            Object value = of(mappersNode.getNamespace(), mapperNode.getId(), type)
                    .toObj(itemElement, url);
            list.add(value);
        });
        return list;
    }
}
