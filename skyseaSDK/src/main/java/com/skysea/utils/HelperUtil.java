package com.skysea.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;


public class HelperUtil {
	/**
	 * @param params 服务参数
	 * @param httpClient http客户
	 */
	public static void applyServiceParams(BasicServiceParams params,
			HttpClient httpClient) {
		int connTimeout = params.getDefaultConnTimeout();
		int sendTimeout = params.getDefaultSendTimeout();
		int recvTimeout = params.getDefaultRecvTimeout();
		HttpParams httpParams = httpClient.getParams();
		//连接超时时间
		httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				connTimeout);
		//scoket超时时间，为发送和接收中的较大者
		httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT,
				(sendTimeout > recvTimeout) ? sendTimeout : recvTimeout);
	}
}
