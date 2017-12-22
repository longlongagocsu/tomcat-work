package ex03.pyrmont.io;

import java.io.IOException;
import java.io.InputStream;

import ex03.pyrmont.request.http.HttpHeader;
import ex03.pyrmont.request.http.HttpRequestLine;

/**
* @author 董龙君
* @date 创建时间: 2017年12月15日 下午5:14:03
**/

public class SocketInputStream extends InputStream{
	
	public SocketInputStream(InputStream inputStream,int size) {
	
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void  readRequestLine(String requestLine) {
		
	}

	public void readRequestLine(HttpRequestLine requestLine) {
		// TODO Auto-generated method stub
		
	}
	
	public void setHeader(HttpHeader header) {
		
	}

}
