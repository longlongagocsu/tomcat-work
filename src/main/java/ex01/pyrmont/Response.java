package ex01.pyrmont;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
* @author 董龙君
* @date 创建时间: 2017年12月13日 上午11:37:48
**/

public class Response {
	
	private static final int BUFFER_SIZE=1024;
	
	OutputStream output;
	
	Request request;
	
	public Response(OutputStream output) {
		this.output=output;
	}
	
	public void setRequest(Request request) {
		this.request=request;
	}
	
	public void sendStaticResource() throws IOException {
		byte[] bytes=new byte[BUFFER_SIZE];
		FileInputStream fis=null;
		try {
			File file=new File(HttpServer.WEB_ROOT,request.getUri());
			if(file.exists()) {
				fis=new FileInputStream(file);
				int ch=fis.read(bytes,0,BUFFER_SIZE);
				StringBuffer sb=new StringBuffer();
				String head="HTTP/1.1 200\r\n"+
						"Content-Type: text/html\r\n"+
						"Content-Length: "+ch+"\r\n"+
						"\r\n";
				output.write(head.getBytes());
				while(ch!=-1) {
					output.write(bytes,0,ch);
					for(int i=0;i<ch;i++) {
						sb.append((char)bytes[i]);
					}
					ch=fis.read(bytes,0,BUFFER_SIZE);
				}
//				System.out.println(sb.toString());
			}else {
				String errorMessage="HTTP/1.1 404 File Not Found\r\n"+
						"Content-Type: text/html\r\n"+
						"Content-Length: 23\r\n"+
						"\r\n"+
						"<h1>File Not Found</h1>";
				output.write(errorMessage.getBytes());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
				if(fis!=null) {
					fis.close();
				}
		}
		
	}

}
