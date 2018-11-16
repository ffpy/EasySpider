package org.ffpy.easyspider.core.utils;

import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.easyspider.core.exception.EasyCrawlerException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class ObjectUtils {

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

    /**
     * 将字符串转换为type类型。
     * <p>支持的类型有：
     * <p>1. byte, int, long, float, double, boolean, char及其包装类。
     * <p>2. String(即不转换)。
     * <p>3. Date(日期类型，需要传入日期格式)。
     *
     * @param type 要转换的类型
     * @param value 要转换的字符串
     * @param format 日期格式，转换日期类型的时候要用
     * @param <T> 转换的类型
     * @return 转换后的类型
     * @throws EasyCrawlerException 转换失败
     */
    @SuppressWarnings("unchecked")
    public static <T> T format(Class<T> type, String value, @Nullable String format) {
        // 不转换
        if (type == String.class) return (T) value;
        // 当value为空时，如果type为基本类型，则返回对应的零值，否则返回null
        if (StringUtils.isEmpty(value)) {
            if (type == byte.class) return (T) Byte.valueOf((byte) 0);
            if (type == int.class) return (T) Integer.valueOf(0);
            if (type == long.class) return (T) Long.valueOf(0);
            if (type == float.class) return (T) Float.valueOf(0f);
            if (type == double.class) return (T) Double.valueOf(0);
            if (type == boolean.class) return (T) Boolean.FALSE;
            if (type == char.class) return (T) Character.valueOf('\0');
            return null;
        }
        // 类型转换
        if (type == byte.class || type == Byte.class) {
            return (T) Byte.valueOf(value);
        } else if (type == short.class || type == Short.class) {
            return (T) Short.valueOf(value);
        } else if (type == int.class || type == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (type == long.class || type == Long.class) {
            return (T) Long.valueOf(value);
        } else if (type == float.class || type == Float.class) {
            return (T) Float.valueOf(value);
        } else if (type == double.class || type == Double.class) {
            return (T) Double.valueOf(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (type == char.class || type == Character.class) {
            return (T) Character.valueOf(value.charAt(0));
        } else if (type == Date.class){
            if (StringUtils.isEmpty(format))
                throw new EasyCrawlerException("format不能为空");
            try {
                return (T) new SimpleDateFormat(format).parse(value);
            } catch (ParseException e) {
                throw new EasyCrawlerException("转换Date类型失败", e);
            }
        }

        throw new EasyCrawlerException("不支持类型" + type);
    }
}
