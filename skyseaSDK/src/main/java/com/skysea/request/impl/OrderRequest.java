package com.skysea.request.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CancellationException;

import org.apache.http.client.ClientProtocolException;

import com.skysea.bean.OrderInfo;
import com.skysea.config.Constants;
import com.skysea.exception.ResponseException;
import com.skysea.request.AbstractRequest;
import com.skysea.utils.StringHelper;

public class OrderRequest extends AbstractRequest<String> {

	public static final String REQUEST_URL = Constants.OrderInfo_URL; 

	public OrderRequest(OrderInfo info) {
		super(METHOD_POST);
		// TODO Auto-generated constructor stub
		setRequestParam("userid", info.getUserid());
		setRequestParam("gameid", info.getGameid());
		setRequestParam("gameserverid", info.getGameserverid());
		setRequestParam("xb_orderid", info.getXb_orderid());
		setRequestParam("amount", info.getAmount());
		setRequestParam("payment_mode", info.getPayment_mode());
		
	}

	@Override
	public String send() throws ClientProtocolException, IOException,
			CancellationException, IllegalArgumentException, ResponseException {
		// TODO Auto-generated method stub
		InputStream response = send(REQUEST_URL);
		String result = StringHelper.inputStreamToString(response);
		return result;
	}

}
