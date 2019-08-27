package com.mvc.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mvc.annotation.P;
import com.mvc.annotation.ResponseBody;
import com.mvc.exception.MVCException;
import com.mvc.support.PackageScan;
import com.mvc.support.PropertiesScan;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @ClassName: DispatcherServlet
 * @Description: MVC主Servlet，提供Servlet分发，参数类型转换，Servlet方法返回值处理功能
 * @author: bryant
 *
 */
public class DispatcherServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 初始化，应在load-on-startup中设置1，在主Servlet,init时完成对其他Servlet的映射
	 */
	public void init() {
		//根据此类的class文件找到编译后的class文件夹根目录
		String url = DispatcherServlet.class.getResource("/").getPath();
		if (url != null) {
			PropertiesScan.init(url);
			PackageScan.init(url);
		} 
	}
	/**
	 * 重写service方法，完成Servlet的分发
	 */
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) {
		//统一设置编码
		try {
			req.setCharacterEncoding("utf-8");
			resp.setContentType("text/html;charset=utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			throw new MVCException("fail to set encoding " + req);
		}
		//获取Servlet的映射
		Map<String, Method> servletMapping = PackageScan.getServletMapping();
		
		//获取requestURI,并截取最后部分，即Servlet名
		String url = req.getRequestURI();
		url = url.replace(String.valueOf(req.getContextPath()) + "/", "");
		
		//在Servlet映射中找到对应方法
		Method method = (Method)servletMapping.get(url);
		
		if (method == null) {
			return;
		}
		//可变参数组，用于接收method的参数列表
		Object[] params = null;
		Class<?> controllerClass = null;
		try {
			//根据方法获取该方法的类class
			controllerClass = method.getDeclaringClass();
			//反射创建该类的实例，用于反射调用方法
			Object obj = controllerClass.newInstance();
			/**
			 * 获取方法实参的数组，供invoke调用
			 */
			params = methodParams(method, req, resp);

			Object returnValue = method.invoke(obj, params);
			
			//方法为null时，service方法resp无响应，可由使用者自行resp响应
			if (returnValue != null){
				/**
				 * 根据方法返回值returnValue，进行ResponseBody注解处理
				 */
				responseBodyHandler(method, req, resp, returnValue);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new MVCException("fail to invoke " + method.getName());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new MVCException("fail to newInstance " + controllerClass.getName());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new MVCException("fail to invoke " + method.getName());
		} 
	}
	
	/**
	 * 获取m方法实参的数组方法，通过req和P注解，获取前端传入的值parameter，如ajax, json格式，data的值
	 */
	private Object[] methodParams(Method m, HttpServletRequest req, HttpServletResponse resp) {
		//方法的形参列表
		Parameter[] ps = m.getParameters();
		//用于存放实参的list，转为数组
		List<Object> list = new ArrayList<Object>();
		//单个接收实参的Object容器
		Object obj = null;
		for(Parameter p : ps) {
			//获取该参数的Class类型，进行判断，先判断三种基本类型
			Class<?> type = p.getType();
			if (type == HttpServletRequest.class) {
				obj = req;
			}
			else if (type == HttpServletResponse.class) {
				obj = resp;
			}
			else if (type == HttpSession.class) {
				obj = req.getSession();
			} else {
				P annotation = (P)p.getAnnotation(P.class);
				//如果没有P注解，会抛异常
				if (annotation == null)
					throw new MVCException("no annotation @P in method " + m.getName()); 
				//获取P注解的value，即前端传入data的name
				String paramName = annotation.value();
				//前端传入的值默认为String类型
				obj = req.getParameter(paramName);
				/**
				 * 标有P注解的时候，进行类型转换
				 */
				obj = typeParse(type, obj, req, paramName);
			} 
			list.add(obj);
		}
    return list.toArray();
   }
	
	/**
	 *  处理@ResponseBody注解方法，	根据返回值处理，转发servlet,jsp,html，或者print String到前端
	 * @param m		需要处理的方法method
	 * @param req	
	 * @param resp
	 * @param returnValue	反射调用方法后的返回值
	 */
	private void responseBodyHandler(Method m, HttpServletRequest req, HttpServletResponse resp, Object returnValue) {

		//判断有无ResponseBody注解，据反映加此注解太繁琐，所以直接用返回值判断
//		ResponseBody respBody = m.getAnnotation(ResponseBody.class);
//		if(respBody != null)
		//如果为字符串
		if (returnValue instanceof String) {
			/**
			 * 返回值为html，jsp，或者其他servlet，则转发
			 */
			if (((String)returnValue).matches(".*(.jsp|.html)$") || PackageScan.getServletMapping().containsKey(returnValue)) {
				try {
					req.getRequestDispatcher((String)returnValue).forward(req, resp);
				} catch (ServletException|IOException e) {
					e.printStackTrace();
					throw new MVCException("fail to dispatcher " + returnValue);
				} 
			} else {	//返回值为普通字符串时，直接写出
				try {
					resp.getWriter().print((String)returnValue);
				} catch (IOException e) {
					e.printStackTrace();
					throw new MVCException("fail to write response " + returnValue);
				}
			}
			//如果为Object对象，则转为json对象再写出print(obj)
		} else if (returnValue instanceof Object) {
			JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
			String json = JSONObject.toJSONString(returnValue, SerializerFeature.WriteDateUseDateFormat);
			try {
				resp.getWriter().print(json);
			} catch (IOException e) {
				e.printStackTrace();
				throw new MVCException("fail to print response, request: " + req);
			} 
		} 
	}
	
	/**
	 * 参数类型转换的方法，以method的形参为标准，将前端传入的实参进行强转
	 * @param cls	形参的Class
	 * @param obj	Object容器，用于包装强转后的值
	 * @param req	req请求
	 * @param paramName		P注解的value,即前端出入的参数键
	 * @return		返回强转之后的单个值
	 */
	private Object typeParse(Class<?> cls, Object obj, HttpServletRequest req, String paramName) {
		//前端传回的值默认为String类型
		String param = (String)obj;
		if (cls == Byte.class || cls == byte.class) {
			obj = Byte.valueOf(param);
		} else if (cls == Short.class || cls == short.class) {
			obj = Short.valueOf(param);
		} else if (cls == Integer.class || cls == int.class) {
			obj = Integer.valueOf(param);
		} else if (cls == Long.class || cls == long.class) {
			obj = Long.valueOf(param);
		} else if (cls == Float.class || cls == float.class) {
			obj = Float.valueOf(param);
		} else if (cls == Double.class || cls == double.class) {
			obj = Double.valueOf(param);
		} else if (cls == Character.class || cls == char.class) {
			obj = Character.valueOf(((Character)obj).charValue());
		} else if (cls == Boolean.class || cls == boolean.class) {
			obj = Boolean.valueOf(param);
		} else if (cls == java.util.Date.class) {
			try {
				obj = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(param);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new MVCException("fail to parse Date " + param);
			}
		} else if (cls == String[].class) {
			obj = req.getParameterValues(paramName);
		}
		return obj;
	}
}
