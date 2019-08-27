/**
 * @Title: PropertiesScan.java
 * @Description: TODO
 * @author: bryant
 * @date: 2019年8月27日 下午11:46:18
 * @version: v1.0
 */
package com.mvc.support;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
/**
 * @ClassName: PropertiesScan
 * @Description: 扫描Properties文件类，文件名必须为mvc.properties
 * @author: bryant
 *
 */
public class PropertiesScan {
	//Controller包名
	private static String packageName;
	
	/**
	 * 提供初始化方法，可以统一初始化
	 * @param url
	 */
	public static void init(String url) { 
		readProperties(url); 
	}
	
	/**
	 * 私有的读取properties文件方法
	 * @param url
	 */
	private static void readProperties(String url) {
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(String.valueOf(url) + "mvc.properties");
			p.load(fis);
		} catch (FileNotFoundException e) {
			System.err.println("读取配置文件失败");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("加载配置文件失败");
			e.printStackTrace();
		} 
		packageName = p.getProperty("packageName");
		}
	
	public static String PackageName() { 
		return packageName; 
		}
}


