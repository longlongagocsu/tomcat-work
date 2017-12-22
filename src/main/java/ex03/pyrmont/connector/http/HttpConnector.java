package ex03.pyrmont.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
* @author 董龙君
* @date 创建时间: 2017年12月15日 下午4:40:33
**/

public class HttpConnector implements Runnable{
	
	boolean stopped;
	
	private String scheme="http";
	
	public String getScheme() {
		return scheme;
	}

	@Override
	public void run() {
		ServerSocket serverSocket=null;
		int port=8080;
		try {
			serverSocket=new ServerSocket(port,1,InetAddress.getByName("127.0.0.1"));
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		while(!stopped) {
			Socket socket=null;
			try {
				socket=serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
	}
	
	public void start() {
		Thread thread=new Thread(this);
		thread.run();
	}

}
