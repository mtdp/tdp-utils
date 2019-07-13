package com.github.mtdp.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ExcelField {
	//导出的表头字段名称
	String filedName() default "";
	//排序
	int sort() default 99;

	boolean isKey() default false;
	//默认FIELD 如果是OBJECT 类型 会发射到相应对象取字段
	Type type() default Type.FIELD;
	//字段类型
	FormatType formatType() default FormatType.STRING;
	//正则表达式 格式话字段值
	String pattern() default "";
}
