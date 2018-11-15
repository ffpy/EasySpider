package org.ffpy.easyspider.core.util;

import org.ffpy.easyspider.core.exception.EasyCrawlerException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class ObjectUtil {

    public static boolean isEmpty(Object obj) {
        boolean isEmpty = false;

        if (obj == null) {                              // null
            isEmpty = true;
        } else if (obj instanceof String) {             // String
            isEmpty = ((String) obj).isEmpty();
        } else if (obj instanceof Object[]) {           // Object[]
            isEmpty = ((Object[]) obj).length == 0;
        } else if (obj instanceof Collection) {         // Collection
            isEmpty = ((Collection) obj).isEmpty();
        } else if (obj instanceof Map) {                // Map
            isEmpty = ((Map) obj).isEmpty();
        }

        return isEmpty;
    }

    /**
     * 获取类的泛型参数
     */
    public static <T> Class<T> getGenericType(Class<?> cls) {
        return getGenericType((ParameterizedType) cls.getGenericSuperclass());
    }

    /**
     * 获取字段类型的泛型参数
     */
    public static <T> Class<T> getGenericType(Field field) {
        return getGenericType((ParameterizedType) (field.getGenericType()));
    }

    public static <T> Class<T> getGenericType(ParameterizedType pt) {
        if (pt != null) {
            Type[] args = pt.getActualTypeArguments();
            if (args != null && args.length > 0) {
                //noinspection unchecked
                return (Class<T>) args[0];
            }
            throw new RuntimeException("获取" + pt.getTypeName() + "的泛型类型失败");
        }
        throw new RuntimeException("获取泛型类型失败");
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertStr(Class<T> type, String s) {
        if (type == String.class) return (T) s;

        if (StringUtil.isEmpty(s)) {
            if (type == byte.class) return (T) Byte.valueOf((byte) 0);
            if (type == int.class) return (T) Integer.valueOf(0);
            if (type == long.class) return (T) Long.valueOf(0);
            if (type == float.class) return (T) Float.valueOf(0f);
            if (type == double.class) return (T) Double.valueOf(0);
            if (type == boolean.class) return (T) Boolean.FALSE;
            return null;
        }

        if (type == byte.class || type == Byte.class){
            return (T) Byte.valueOf(s);
        } else if (type == short.class || type == Short.class){
            return (T) Short.valueOf(s);
        } else if (type == int.class || type == Integer.class){
            return (T) Integer.valueOf(s);
        } else if (type == long.class || type == Long.class){
            return (T) Long.valueOf(s);
        } else if (type == float.class || type == Float.class){
            return (T) Float.valueOf(s);
        } else if (type == double.class || type == Double.class){
            return (T) Double.valueOf(s);
        } else if (type == boolean.class || type == Boolean.class){
            return (T) Boolean.valueOf(s);
        }

        throw new EasyCrawlerException("不支持类型" + type + "，仅支持String, byte, short, int, long, float, double, boolean及其包装类");
    }
}
