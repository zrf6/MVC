/**
 * @Title: SupportException.java
 * @Description: TODO
 * @author: bryant
 * @date: 2019年8月27日 下午11:46:48
 * @version: v1.0
 */
package com.mvc.exception;

/**
 * @ClassName: SupportException
 * @Description: TODO
 * @author: bryant
 *
 */
public class SupportException extends MVCException {

	private static final long serialVersionUID = 1L;

	public SupportException() {
		super();
	}

	public SupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SupportException(String message, Throwable cause) {
		super(message, cause);
	}

	public SupportException(String message) {
		super(message);
	}

	public SupportException(Throwable cause) {
		super(cause);
	}
}
