/**
 * @Title: Controller.java
 * @Description: TODO
 * @author: bryant
 * @date: 2019年8月27日 下午11:47:10
 * @version: v1.0
 */
package com.mvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @ClassName: Controller
 * @Description: Controller类注解，标记此注解的类会进行扫描
 * @author: bryant
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Controller {
	
}

