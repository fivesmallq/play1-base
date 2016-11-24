package models.api;

import java.lang.annotation.*;

/**
 * 用于标识出字段（尤其是：枚举常量）的含义
 *
 * @author zhaoxy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Remark {
    String remark() default "";
}
