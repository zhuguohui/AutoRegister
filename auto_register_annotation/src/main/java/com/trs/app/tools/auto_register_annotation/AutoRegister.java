package com.trs.app.tools.auto_register_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * Created by zhuguohui
 * Date: 2024/4/26
 * Time: 10:38
 * Desc:使用这个注解放置在类上面，可以将类名自动收集到
 * value对应的class中，字段为CLASSES
 * </pre>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoRegister {

    /**
     * 配置要生成类的全类名，注意对应的包要存在于系统之中。
     * @return
     */
    String value() default "AutoRegisteredClasses";
}