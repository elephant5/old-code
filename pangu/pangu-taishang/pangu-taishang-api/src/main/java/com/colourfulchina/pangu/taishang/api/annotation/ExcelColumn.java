package com.colourfulchina.pangu.taishang.api.annotation;

import java.lang.annotation.*;

/**
 * 功能描述: 自定义实体类所需要的bean(Excel属性标题、位置等)<br>
 * @since: 1.0.0
 * @Author:zhaojun2
 * @Date: 2019/9/10 14:18
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    /**
     * Excel标题
     *
     * @return
     * @author zhaojun2
     */
    String value() default "";

    /**
     * Excel从左往右排列位置
     *
     * @return
     * @author zhaojun2
     */
    int col() default 0;
}
