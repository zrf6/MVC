/**
 * @Title: RequestMapping.java
 * @Description: TODO
 * @author: bryant
 * @date: 2019年8月27日 下午11:47:28
 * @version: v1.0
 */
package com.mvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * @ClassName: RequestMapping
 * @Description: serlvet注解，标注在方法上，表示此方法为一个servlet
 * @author: bryant
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface RequestMapping {
	//value值为servlet名
	String value();
}
