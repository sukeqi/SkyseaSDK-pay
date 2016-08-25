package com.skysea.utils;

import com.skysea.service.IPlatService;
import com.skysea.service.impl.PlatServiceAgent;


public class ServiceFactory {
	public static ServiceFactory get() {
		return cokeServiceFactory;
	}
	//生成普通请求服务
	public IPlatService createPlatService() {
		return new PlatServiceAgent();
	}
	 
	//静态工厂实体
	private static ServiceFactory cokeServiceFactory = new ServiceFactory();
}
