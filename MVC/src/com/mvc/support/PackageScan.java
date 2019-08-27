/**
 * @Title: PackageScan.java
 * @Description: TODO
 * @author: bryant
 * @date: 2019年8月27日 下午11:46:04
 * @version: v1.0
 */
package com.mvc.support;

import com.mvc.annotation.RequestMapping;
import com.mvc.exception.SupportException;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
/**
 * @ClassName: PackageScan
 * @Description: 扫描Controller包类，将所有带有注解
 * @author: bryant
 *
 */
public class PackageScan {
	
	//servlet映射，将每一个Servlet名字和对应方法放入map中
	private static Map<String, Method> servletMapping = new HashMap();
	private static String packageName = PropertiesScan.PackageName();
	
	/**
	 * 初始化方法，统一调用初始化，需要class文件夹的根目录，与包名拼接为要遍历的地址
	 * @param url
	 */
	public static void init(String url) {
		packageName = packageName.replace(".", "/");
		url = String.valueOf(url) + packageName;
		getFiles(new File(url));
	}
	
	/**
	 * 遍历文件方法，将Controller包下所有class文件找出，再调用ScanMethod()找出此class文件加了注解的方法
	 * @param file
	 */
	private static void getFiles(File file) {
		File[] files = file.listFiles();
		for(File f : files) {
			if(f.isDirectory()) {
				//如果为多层，将文件夹添加
				packageName = String.valueOf(packageName) + "/" + f.getName();
				getFiles(f);
				//上层遍历完，移除上层文件夹名
				packageName = packageName.replace(f.getName(), "");
	
			} else if (f.isFile() && f.getAbsolutePath().endsWith(".class")) {
				//拼接为className用于反射获取class对象
				String className = String.valueOf(packageName.replace("/", ".")) + "." + f.getName().replace(".class", "");
				Class<?> clazz = null;
				try {
					clazz = Class.forName(className);
					scanMethod(clazz);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					throw new SupportException("find no className " + className);
					} 
				}
			}
		}

	/**
	 * 遍历class对象，将methods带有Controller注解类且方法有RequestMapping注解的写入map映射
	 * @param class<T>
	 */
	private static <T> void scanMethod(Class<T> cls) {
		//判断该class是否含Controller注解
		if (cls.isAnnotationPresent(com.mvc.annotation.Controller.class)) {
			//获取所有方法
			Method[] methods = cls.getMethods(); 
			RequestMapping rm;
			for(Method m : methods) {
				//如果方法上有RequestMapping注解，写入map映射
				if ((rm = (RequestMapping)m.getAnnotation(RequestMapping.class)) != null) {
					servletMapping.put(rm.value(), m);
				}
			}
		} 
	}
  
	/**
	 * 返回servlet映射供其他类使用
	 * @return map
	 */
	public static Map<String, Method> getServletMapping() {
		return servletMapping;
		}
}

