package org.ffpy.easyspider.core.mapper;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    /**
     * 参数名
     */
    String value();
}
