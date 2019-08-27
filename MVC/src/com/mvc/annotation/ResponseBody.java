/**
 * @Title: ResponseBody.java
 * @Description: TODO
 * @author: bryant
 * @date: 2019年8月27日 下午11:47:35
 * @version: v1.0
 */
package com.mvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * @ClassName: ResponseBody
 * @Description: 返回值注解，标记此注解表示进行返回值处理
 * @author: bryant
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface ResponseBody {
	//空注解
}