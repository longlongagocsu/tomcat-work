package ex03.pyrmont.processor.http;

import java.io.OutputStream;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import ex03.pyrmont.io.SocketInputStream;
import ex03.pyrmont.request.http.HttpHeader;
import ex03.pyrmont.request.http.HttpRequest;
import ex03.pyrmont.request.http.HttpRequestLine;
import ex03.pyrmont.response.http.HttpResponse;
import ex03.pyrmont.util.ParameterMap;
import ex03.pyrmont.util.RequestUtil;

/**
* @author 董龙君
* @date 创建时间: 2017年12月15日 下午4:56:36
**/

public class HttpProcessor {
	
	private HttpRequest request;
	
	private HttpResponse response;
	
	private HttpRequestLine requestLine;
	
	boolean parse;
	
	public void process(Socket socket) {
		SocketInputStream input=null;
		OutputStream output=null;
		try {
			input=new SocketInputStream(socket.getInputStream(),2048);
			output=socket.getOutputStream();
			request=new HttpRequest(input);
			response=new HttpResponse(output);
			response.setRequest(request);
			response.setHeader("Servcer", "Pyrmont Servlet Container");
			parseRequest(input,output);
			parseHeaders(input);
			if(request.getRequestURI().startsWith("/servlet/")) {
				ServletProcessor processor=new ServletProcessor();
				processor.process(request, response);
			}else {
				StaticResourceProcessor processor=new StaticResourceProcessor();
				processor.process(request, response);
			}
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void parseRequest(SocketInputStream input,OutputStream output) throws ServletException {
		input.readRequestLine(requestLine);
		String method=new String(requestLine.method,0,requestLine.methodEnd);
		String uri=null;
		String protocol=new String(requestLine.protocol,0,requestLine.protocolEnd);
		if(method.length()<1) {
			throw new ServletException("Missing HTTP request method");
		}
		else if(requestLine.uriEnd<1){
			throw new ServletException("Missiing HTTP request URI");
		}
		int question=requestLine.indexOf("?");
		if(question>=0) {
			request.setQueryString(new String(requestLine.uri,question+1,requestLine.uriEnd-question-1));
			uri=new String(requestLine.uri,0,question);
		}else {
			request.setQueryString(null);
			uri=new String(requestLine.uri,0,requestLine.uriEnd);
		}
		if(!uri.startsWith("/")) {
			int pos=uri.indexOf("://");
			if(pos!=-1) {
				pos=uri.indexOf("/",pos+3);
				if(pos==-1) {
					uri="";
				}else {
					uri=uri.substring(pos);
				}
			}
		}
		String match="";
		String jessionid="";
		int semicolon=uri.indexOf(match);
		if(semicolon>=0) {
			String rest=uri.substring(semicolon+match.length());
			int semicolon2=rest.indexOf(":");
			if(semicolon2>=0) {
				request.setRequestedSessionId(rest.substring(0,semicolon2));
				rest=rest.substring(semicolon2);
			}else {
				request.setRequestedSessionId(rest);
				rest=null;
			}
			request.setRequestedSessionURL(true);
			uri=uri.substring(0, semicolon)+rest;
		}else {
			request.setRequestedSessionId(null);
			request.setRequestedSessionURL(false);
		}
		String normalizedUri=normalize(uri);
		request.setMethod(method);
		request.setProtocol(protocol);
		if(normalizedUri!=null) {
			request.setRequestUri(normalizedUri);
		}else {
			request.setRequestUri(uri);
		}
		if(normalizedUri==null) {
			throw new ServletException("Invalid URI:"+uri);
		}
	}
	
	private String normalize(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	private void parseHeaders(SocketInputStream input) throws ServletException {
		HttpHeader header=new HttpHeader();
		input.setHeader(header);
		if(header.nameEnd==0) {
			if(header.valueEnd==0) {
				return;
			}else {
				throw new ServletException("HttpProcessor.parseHeaders.colon");
			}
		}
		String name=new String(header.name,0,header.nameEnd);
		String value=new String(header.name,0,header.valueEnd);
		request.addHeader(name, value);
		if(name.equals("cookie")) {
			Cookie cookies[]=RequestUtil.parseCookieHeader(value);
			for(int i=0;i<cookies.length;i++) {
				if(cookies[i].getName().equals("jsessionid")) {
					if(!request.isRequestedSessionIdFromCookie()) {
						request.setRequestedSessionId(cookies[i].getValue());
						request.setRequestedSessionCookie(true);
						request.setRequestedSessionURL(false);
					}
				}
			}
		}else if(name.equals("content-length")) {
			int n=-1;
			n=Integer.parseInt(value);
			request.setContentLength(n);
		}else if(name.equals("content-type")) {
			request.setContentType(value);
		}
	}
	
	public void parseParameter(ParameterMap parameters) {
		if(parse) {
			return;
		}
		ParameterMap results=parameters;
		if(results==null) {
			results=new ParameterMap();
		}
	}
}
