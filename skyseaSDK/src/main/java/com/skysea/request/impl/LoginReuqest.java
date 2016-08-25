/**
 * 
 */
package com.skysea.request.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CancellationException;

import org.apache.http.client.ClientProtocolException;

import com.skysea.bean.LoginInfo;
import com.skysea.config.Constants;
import com.skysea.exception.ResponseException;
import com.skysea.request.AbstractRequest;
import com.skysea.utils.StringHelper;

/**
 * @author sky
 *
 */
public class LoginReuqest extends AbstractRequest<String> {
	
	public static final String REQUEST_URL = Constants.Login_URL;

	public LoginReuqest(LoginInfo info) {
		super(METHOD_POST);
		// TODO Auto-generated constructor stub
		setRequestParam("username", info.getUsername());
		setRequestParam("userpwd", info.getUserpwd());
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
