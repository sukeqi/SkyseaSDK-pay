package com.skysea.service;

import com.skysea.utils.BasicServiceParams;

/**
 * 
 * @author sky sea
 *
 * @param <SerParam>
 */

public interface IService<SerParam extends BasicServiceParams> {
	/**
	 * 设置参数
	 * @param params
	 */
	public void setParam(SerParam params);
	/**
	 * 注册参数
	 * @param params
	 */
	public void commitParam(SerParam params);
	/**
	 * 重置参数
	 * @param params
	 */
	public void resetParam(SerParam params);
	/**
	 * 中止服务
	 */
	public void abortService();
	/**
	 * @return
	 * 	 	       是否中止
	 */
	public boolean isAborted();
}
