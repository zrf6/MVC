# MVC

[![License](https://camo.githubusercontent.com/8cb994f6c4a156c623fe057fccd7fb7d7d2e8c9b/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c6963656e73652d417061636865253230322d3445423142412e737667)](https://www.apache.org/licenses/LICENSE-2.0.html)

模仿SpringMVC写的mvc小框架，使用方法如下：

### 注解

------

- Controller：标记于类上，表示此类含有Servlet方法

  - 标记注解，无属性

- RequestMapping：标记于方法上，表示此方法为一个Servlet，需要req，resp参数

  - 有value属性，值为Servlet名

- ResponseBody：标记于方法上，框架可根据 return 值做不同处理

  - 标记注解，无属性
    - return 	html，jsp，其他Servlet，的String，MVC直接进行跳转	
    - return     普通String，直接写出到前端

- P：标记于方法的参数上，框架会将前端传回的值进行类型转换

  - 有value属性，值为前端传回的参数名

    *如 标签中的 name， 或者 ajax中 data的键*

    

------

### Properties文件

- 文件需要放在项目src根目录下
- 内容为controller包的相对路径

```
packageName=com.xxx.xxx.controller
```



------

### XML文件

- 配置参考，可根据自己需求添加修改

```xml
<!-- 配置不拦截静态资源 -->
	<servlet>
		<servlet-name>default</servlet-name>
		<servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
		<init-param>
			<param-name>debug</param-name>
			<param-value>0</param-value>
		</init-param>
		<init-param>
			<param-name>listings</param-name>
			<param-value>false</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>default</servlet-name>
		<url-pattern>*.js</url-pattern>
		<url-pattern>*.css</url-pattern>
		<url-pattern>*.gif</url-pattern>
		<url-pattern>*.jpg</url-pattern>
		<url-pattern>*.html</url-pattern> 
	</servlet-mapping>

	<!-- MVC的DispatcherServlet配置 -->
  	<servlet>
	    <servlet-name>DispatcherServlet</servlet-name>
	    <servlet-class>com.mvc.controller.DispatcherServlet</servlet-class>
        <!-- 服务器启动时初始化，完成对Servlet的映射 -->
	    <load-on-startup>1</load-on-startup>
  	</servlet>
 	<servlet-mapping>
    	<servlet-name>DispatcherServlet</servlet-name>
        <!-- 拦截所有请求 -->
    	<url-pattern>/</url-pattern>
  	</servlet-mapping>
```

------

### jar包

fastjson-1.2.59.jar

[https://mvnrepository.com/artifact/com.alibaba/fastjson/1.2.59]

javax.servlet-api-3.1.0.jar

[https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api/3.1.0]





