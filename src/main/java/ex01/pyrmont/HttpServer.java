package ex01.pyrmont;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
* @author 董龙君
* @date 创建时间: 2017年12月13日 上午11:01:19
**/

public class HttpServer {
	
	public static final String WEB_ROOT=System.getProperty("user.dir")+File.separator+
			"src"+File.separator+"main"+File.separator+"webapp";
	
	private static final String SHUTDOWN_COMMAND="/SHUTDOWN";
	
	private boolean shutdown=false;

	public static void main(String[] args) {
		HttpServer server=new HttpServer();
		server.await();
	}
	
	public void await() {
		ServerSocket serverSocket=null;
		int port=8080;
		try {
			serverSocket=new ServerSocket(port,10,InetAddress.getByName("127.0.0.1"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		while(!shutdown) {
			Socket socket=null;
			InputStream input=null;
			OutputStream output=null;
			try {
				socket=serverSocket.accept();
				input=socket.getInputStream();
				output=socket.getOutputStream();
				Request request=new Request(input);
				request.parse();
				Response response=new Response(output);
				response.setRequest(request);
				response.sendStaticResource();
				socket.close();
				shutdown=request.getUri().equals(SHUTDOWN_COMMAND);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
