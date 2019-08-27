/**
 * @Title: P.java
 * @Description: TODO
 * @author: bryant
 * @date: 2019年8月27日 下午11:47:19
 * @version: v1.0
 */
package com.mvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @ClassName: P
 * @Description: 参数注解，此注解value与request的parameter保持一致
 * @author: bryant
 *
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface P {
	//值为parameter的名
	String value();
}
