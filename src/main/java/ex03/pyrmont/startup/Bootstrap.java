package ex03.pyrmont.startup;

import ex03.pyrmont.connector.http.HttpConnector;

/**
* @author 董龙君
* @date 创建时间: 2017年12月15日 下午4:38:03
**/

public class Bootstrap {

	public static void main(String[] args) {
		HttpConnector connector=new HttpConnector();
		connector.start();
	}

}
