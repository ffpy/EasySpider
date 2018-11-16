package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.entity.Page;
import org.ffpy.easyspider.core.helper.MapperHelper;

import java.lang.reflect.ParameterizedType;

public abstract class ModelProcessor<T> implements Processor {
    private final Class<?> namespace;
    private final String id;
    private final Class<T> type;

    public ModelProcessor(Class<?> namespace, String id) {
        this.namespace = namespace;
        this.id = id;
        //noinspection unchecked
        this.type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public abstract void process(Page page, T obj) throws Exception;

    @Override
    public void process(Page page) throws Exception {
        T obj = MapperHelper.of(namespace, id, type).toObj(page);
        process(page, obj);
    }
}
