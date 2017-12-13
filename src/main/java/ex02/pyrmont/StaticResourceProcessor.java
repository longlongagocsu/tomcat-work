package ex02.pyrmont;

import java.io.IOException;

/**
* @author 董龙君
* @date 创建时间: 2017年12月13日 下午5:57:29
**/

public class StaticResourceProcessor {
	public void processor(Request request,Response response) {
		try {
			response.sendStaticResource();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
