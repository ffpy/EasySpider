package org.ffpy.easyspider.core.processor;

import org.ffpy.easyspider.core.entity.Context;
import org.ffpy.easyspider.core.util.MapperHelper;

import java.lang.reflect.ParameterizedType;

public abstract class ObjectProcessor<T> implements Processor {
    private final Class<?> namespace;
    private final String id;
    private final Class<T> type;

    public ObjectProcessor(Class<?> namespace, String id) {
        this.namespace = namespace;
        this.id = id;
        //noinspection unchecked
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public abstract void process(Context context, T obj) throws Exception;

    @Override
    public void process(Context context) throws Exception {
        T obj = MapperHelper.of(namespace, id, type).toObj(context);
        process(context, obj);
    }
}
