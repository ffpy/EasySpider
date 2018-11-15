package org.ffpy.easyspider.core.mapper;

import org.ffpy.easyspider.core.entity.Wrap;
import org.ffpy.easyspider.core.exception.EasyCrawlerException;
import org.ffpy.easyspider.core.mapper.entity.Mapper;
import org.ffpy.easyspider.core.mapper.entity.Mappers;
import org.ffpy.easyspider.core.mapper.entity.Request;
import org.ffpy.easyspider.core.util.MapperHelper;
import org.ffpy.easyspider.core.util.UrlUtil;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MapperProxy<T> implements InvocationHandler {
    private final MapperFactory mapperFactory;
    private final Mappers mappers;

    public MapperProxy(MapperFactory mapperFactory, Mappers mappers) {
        this.mapperFactory = mapperFactory;
        this.mappers = mappers;
    }

    public static <T> T of(MapperFactory mapperFactory, Class<T> mapperType) {
        Mappers mappers = MapperManager.getMappers(mapperType);
        Class<?>[] interfaces = new Class[]{mapperType};
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(mapperType.getClassLoader(), interfaces,
                new MapperProxy<>(mapperFactory, mappers));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String mapperId = method.getName();
        Mapper mapper = mappers.getMapper(mapperId);
        Request request = mappers.getRequest(mapper.getRequest());
        String url = UrlUtil.parseUrl(request.getUrl(), getParams(method, args));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Wrap<Object> wrap = new Wrap<>();
        mapperFactory.getDownloader().download(url, html -> {
            MapperHelper<?> mapperHelper = MapperHelper.of(mappers.getNamespace(),
                    mapperId, method.getReturnType());
            if (method.getReturnType() == List.class)
                mapperHelper.parameterizedType((ParameterizedType) method.getGenericReturnType());
            wrap.value = mapperHelper.toObj(html, url);
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
