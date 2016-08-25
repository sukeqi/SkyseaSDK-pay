package com.skysea.utils;

import java.util.HashMap;

/**
 * 基础服务参数 
 * 
 * @author sky sea
 */
public class BasicServiceParams {
	/**
	 * 连接超时参数标记
	 */
	private final static Integer KEY_DEFAULT_CONN_TIMEOUT = 1;
	/**
	 * 发送超时参数标记
	 */
	private final static Integer KEY_DEFAULT_SEND_TIMEOUT = 2;
	/**
	 * 接收超时参数标记
	 */
	private final static Integer KET_DEFAULT_RECV_TIMEOUT = 3;
	
	/**
	 * 哈希曼表封装的参数对象
	 */
	protected HashMap<Integer, Object> mParams;

	/**
	 * 初始化参数
	 */
	public BasicServiceParams() {
		mParams = new HashMap<Integer, Object>();
	}

	/**
	 * 清空参数
	 */
	public void clear() {
		mParams.clear();
	}

	/**
	 * @return 获得默认连接超时
	 */
	public int getDefaultConnTimeout() {
		Integer value = (Integer) mParams.get(KEY_DEFAULT_CONN_TIMEOUT);
		if (value != null) {
			return value;
		}
		return 0;
	}

	/**
	 * @param seconds
	 *            多少秒
	 */
	public void setDefaultConnTimeout(int seconds) {
		mParams.put(KEY_DEFAULT_CONN_TIMEOUT, seconds);
	}

	/**
	 * @return 获得默认发送超时时间
	 */
	public int getDefaultSendTimeout() {
		Integer value = (Integer) mParams.get(KEY_DEFAULT_SEND_TIMEOUT);
		if (value != null) {
			return value;
		}
		return 0;
	}

	/**
	 * @param seconds
	 *            多少秒
	 */
	public void setDefaultSendTimeout(int seconds) {
		mParams.put(KEY_DEFAULT_SEND_TIMEOUT, seconds);
	}

	/**
	 * @return 获得默认接收超时时间
	 */
	public int getDefaultRecvTimeout() {
		Integer value = (Integer) mParams.get(KET_DEFAULT_RECV_TIMEOUT);
		if (value != null) {
			return value;
		}
		return 0;
	}

	/**
	 * @param seconds
	 *            秒
	 */
	public void setDefaultRecvTimeout(int seconds) {
		mParams.put(KET_DEFAULT_RECV_TIMEOUT, seconds);
	}

	/**
	 * 将服务参数应用到服务中
	 * 
	 * @param params
	 */
	public void applyTo(BasicServiceParams params) {
		mParams.putAll(params.mParams);
	}

}
