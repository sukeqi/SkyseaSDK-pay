package com.skysea.service;

import java.io.IOException;
import java.util.concurrent.CancellationException;

import com.skysea.bean.LoginInfo;
import com.skysea.bean.OrderInfo;
import com.skysea.bean.RegisterInfo;
import com.skysea.exception.ResponseException;
import com.skysea.utils.BasicServiceParams;

public interface IPlatService extends IService<BasicServiceParams> {

	/**
	 * @param logininfo
	 *            登陆信息
	 * @return 登录用户实体 以下为发送请求时异常
	 * @throws CancellationException
	 *             取消服务异常
	 * @throws IllegalArgumentException
	 *             非法参数异常
	 * @throws IOException
	 * @throws ResponseException
	 *             返回异常
	 */
	public String toLogin(LoginInfo logininfo) throws CancellationException,
			IllegalArgumentException, IOException, ResponseException;

	public String toRegister(RegisterInfo registerinfo)
			throws CancellationException, IllegalArgumentException,
			IOException, ResponseException;

	public String toOrder(OrderInfo orderInfo) throws CancellationException,
			IllegalArgumentException, IOException, ResponseException;
	
	public String toPostResult(String resultInfo)throws CancellationException,
	IllegalArgumentException, IOException, ResponseException;
}
