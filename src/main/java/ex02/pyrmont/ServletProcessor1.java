package ex02.pyrmont;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
* @author 董龙君
* @date 创建时间: 2017年12月13日 下午5:49:20
**/

public class ServletProcessor1 {
	
	public void process(Request request,Response  response) {
		String uri=request.getUri();
		String servletName=uri.substring(uri.lastIndexOf("/")+1);
		URLClassLoader loader=null;
		try {
			URL[] urls=new URL[1];
			URLStreamHandler streamHandler=null;
			File classPath=new File(this.getClass().getResource("").getPath());
			String repository=(new URL("file",null,classPath.getCanonicalPath()+File.separator)).toString();
			urls[0]=new URL(null,repository,streamHandler);
			loader=new URLClassLoader(urls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Class myClass=null;
		try {
			myClass=loader.loadClass(servletName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Servlet servlet=null;
		try {
			servlet=(Servlet)myClass.newInstance();
			servlet.service((ServletRequest)request, (ServletResponse)response);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
