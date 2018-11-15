package org.ffpy.easyspider.core.util;

import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.exception.EasyCrawlerException;
import org.ffpy.easyspider.core.mapper.ContentType;
import org.ffpy.easyspider.core.mapper.MapperManager;
import org.ffpy.easyspider.core.mapper.entity.Mapper;
import org.ffpy.easyspider.core.mapper.entity.Mappers;
import org.ffpy.easyspider.core.mapper.entity.Property;
import org.ffpy.easyspider.core.mapper.entity.Sub;
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
    private final Mappers mappers;
    private final Mapper mapper;
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
        this.mappers = MapperManager.getMappers(namespace);
        this.mapper = mappers.getMapper(id);
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
     * @param context 上下文
     * @return Bean实例
     */
    public T toObj(Context context) {
        return toObj(context.getHtml(), context.getUrl());
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
            return toObj(Jsoup.parse(html, UrlUtil.getBaseUri(url)));
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
    public T toObj(Element element) {
        try {
            if (type == List.class) {
                final Class<?> subType = parameterizedType == null ?
                        ObjectUtil.getGenericType(type) :
                        ObjectUtil.getGenericType(parameterizedType);
                //noinspection unchecked
                return (T) createList(mapper, subType, element);
            } else {
                final T obj = type.newInstance();
                fillProperties(obj, mapper, element);
                fillSubs(obj, mapper, element);
                return obj;
            }
        } catch (Exception e) {
            throw new EasyCrawlerException(e);
        }
    }

    /**
     * 填充Bean的属性
     *
     * @param obj     Bean实例
     * @param mapper  映射
     * @param element 页面文档
     */
    private void fillProperties(Object obj, Mapper mapper, Element element) {
        List<Property> propertyList = mapper.getPropertyList();
        if (propertyList == null) return;
        for (Property property : propertyList) {
            try {
                final Field field = type.getDeclaredField(property.getName());

                final Element e = element.selectFirst(property.getSelector());
                if (e == null) continue;

                final ContentType contentType = property.getContentType();
                if (contentType == null)
                    throw new EasyCrawlerException("无效的content: " + property.getContent() + "。");
                // 获取值
                String value = contentType.getValue(e);
                if (StringUtil.isNotEmpty(value) &&
                        StringUtil.isNotEmpty(property.getPattern()))
                    value = PatternHelper.of(property.getPattern()).matcher(value).group(1);
                // 设置值
                field.setAccessible(true);
                field.set(obj, ObjectUtil.convertStr(field.getType(), value));
            } catch (NoSuchFieldException ignored) {
            } catch (IllegalAccessException e) {
                throw new EasyCrawlerException("注入属性失败！", e);
            }
        }
    }

    /**
     * 填充Bean的子映射
     *
     * @param obj     Bean实例
     * @param mapper  映射
     * @param element 页面文档
     */
    private void fillSubs(Object obj, Mapper mapper, Element element) {
        List<Sub> subList = mapper.getSubList();
        if (subList == null) return;
        for (Sub sub : subList) {
            try {
                final Field field = type.getDeclaredField(sub.getName());
                final Element e = StringUtil.isEmpty(sub.getSelector()) ?
                        element : element.selectFirst(sub.getSelector());
                final Mapper subMapper = mappers.getMapper(sub.getMapper());

                field.setAccessible(true);
                // 列表
                if (field.getType() == List.class) {
                    Class<?> subType = ObjectUtil.getGenericType(field);
                    field.set(obj, createList(subMapper, subType, e));
                }
                // 单个
                else {
                    Object value = of(mappers.getNamespace(), subMapper.getId(),
                            field.getType()).toObj(e);
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
     * @param mapper  映射
     * @param type    类型
     * @param element 页面文档
     * @return Bean列表
     */
    private List<Object> createList(Mapper mapper, Class<?> type, Element element) {
        String itemSelector = mapper.getItemSelector();
        if (StringUtil.isEmpty(itemSelector))
            throw new EasyCrawlerException(mapper.getId() + "的itemSelector属性不能为空");

        Elements elements = element.select(itemSelector);
        List<Object> list = new ArrayList<>(elements.size());
        // 添加项
        elements.forEach(itemElement -> {
            Object value = of(mappers.getNamespace(), mapper.getId(), type)
                    .toObj(itemElement);
            list.add(value);
        });
        return list;
    }
}
