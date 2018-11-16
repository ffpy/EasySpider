package org.ffpy.easyspider.core.mapper;

import org.ffpy.easyspider.core.entity.Wrap;
import org.ffpy.easyspider.core.exception.EasyCrawlerException;
import org.ffpy.easyspider.core.mapper.node.MapperNode;
import org.ffpy.easyspider.core.mapper.node.MappersNode;
import org.ffpy.easyspider.core.mapper.node.RequestNode;
import org.ffpy.easyspider.core.helper.MapperHelper;
import org.ffpy.easyspider.core.utils.UrlUtils;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MapperProxy<T> implements InvocationHandler {
    private final MapperFactory mapperFactory;
    private final MappersNode mappersNode;

    public MapperProxy(MapperFactory mapperFactory, MappersNode mappersNode) {
        this.mapperFactory = mapperFactory;
        this.mappersNode = mappersNode;
    }

    public static <T> T of(MapperFactory mapperFactory, Class<T> mapperType) {
        MappersNode mappersNode = MapperManager.getMappers(mapperType);
        Class<?>[] interfaces = new Class[]{mapperType};
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(mapperType.getClassLoader(), interfaces,
                new MapperProxy<>(mapperFactory, mappersNode));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String mapperId = method.getName();
        MapperNode mapperNode = mappersNode.getMapper(mapperId);
        RequestNode requestNode = mappersNode.getRequest(mapperNode.getRequest());
        String url = UrlUtils.parseUrl(requestNode.getUrl(), getParams(method, args));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Wrap<Object> wrap = new Wrap<>();
        mapperFactory.getDownloader().download(url, response -> {
            MapperHelper<?> mapperHelper = MapperHelper.of(mappersNode.getNamespace(),
                    mapperId, method.getReturnType());
            if (method.getReturnType() == List.class)
                mapperHelper.parameterizedType((ParameterizedType) method.getGenericReturnType());
            wrap.value = mapperHelper.toObj(response.string(), url);
            countDownLatch.countDown();
        });
        countDownLatch.await();
        return wrap.value;
    }

    private Map<String, Object> getParams(Method method, Object[] args) {
        Map<String, Object> params = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Param param = parameters[i].getAnnotation(Param.class);
            if (param == null)
                throw new EasyCrawlerException(method.getName() + "方法的第" + (i + 1) +
                        "个参数没有@Param注解");
            params.put(param.value(), args[i]);
        }
        return params;
    }
}
