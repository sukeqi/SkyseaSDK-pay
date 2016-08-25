package com.skysea.service.impl;

import java.io.IOException;
import java.util.concurrent.CancellationException;

import com.skysea.bean.LoginInfo;
import com.skysea.bean.OrderInfo;
import com.skysea.bean.RegisterInfo;
import com.skysea.exception.ResponseException;
import com.skysea.request.impl.LoginReuqest;
import com.skysea.request.impl.OrderRequest;
import com.skysea.request.impl.PostResultRequest;
import com.skysea.request.impl.RegisterRequest;
import com.skysea.service.IPlatService;
import com.skysea.utils.BasicServiceParams;

/**
 * 请求服务实现
 * 
 * @author sky sea
 * 
 */
public class PlatServiceAgent extends AbstractService<BasicServiceParams>
		implements IPlatService {

	/**
	 * 连接时间
	 */
	private final static int DEFAULT_CONN_TIME_OUT = 15000;
	/**
	 * 发送时间
	 */
	private final static int DEFAULT_SEND_TIME_OUT = 20000;
	/**
	 * 接收时间
	 */
	private final static int DEFAULT_RECV_TIME_OUT = 30000;

	/**
	 * 初始化服务相关参数
	 */
	public PlatServiceAgent() {
		super();
		mParams = new BasicServiceParams();
		mParams.setDefaultConnTimeout(DEFAULT_CONN_TIME_OUT);
		mParams.setDefaultSendTimeout(DEFAULT_SEND_TIME_OUT);
		mParams.setDefaultRecvTimeout(DEFAULT_RECV_TIME_OUT);
		applyServiceParams();
	}

	@Override
	public String toLogin(LoginInfo logininfo) throws CancellationException,
			IllegalArgumentException, IOException, ResponseException {
		// TODO Auto-generated method stub
		return Send(new LoginReuqest(logininfo));
	}

	@Override
	public String toRegister(RegisterInfo registerinfo)
			throws CancellationException, IllegalArgumentException,
			IOException, ResponseException {
		// TODO Auto-generated method stub
		return Send(new RegisterRequest(registerinfo));
	}

	@Override
	public String toOrder(OrderInfo orderinfo) throws CancellationException,
			IllegalArgumentException, IOException, ResponseException {
		// TODO Auto-generated method stub
		return Send(new OrderRequest(orderinfo));
	}

	@Override
	public String toPostResult(String resultInfo) throws CancellationException,
			IllegalArgumentException, IOException, ResponseException {
		// TODO Auto-generated method stub
		return Send(new PostResultRequest(resultInfo));
	}

}
