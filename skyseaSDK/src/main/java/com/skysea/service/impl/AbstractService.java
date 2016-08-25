/**
 * 
 */
package com.skysea.service.impl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CancellationException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.skysea.exception.ResponseException;
import com.skysea.request.AbstractRequest;
import com.skysea.service.IService;
import com.skysea.utils.BasicServiceParams;
import com.skysea.utils.HelperUtil;

/**
 * 服务抽象类
 * @author sky sea
 */
public abstract class AbstractService<SerParam extends BasicServiceParams> implements IService<SerParam>{
	/**
	 * 服务相关参数
	 */
	protected SerParam mParams;
	/**
	 * http客户端
	 */
	protected HttpClient mHttpClient;
	/**
	 * 是否中止
	 */
	protected boolean mAborted;
	/**
	 * 请求对象
	 */
	protected AbstractRequest<?> mRequest;
	
	/**
	 * 初始化服务的状态
	 */
	public AbstractService() {
		reset();
	}

	@Override
	public void setParam(SerParam params) {
		// TODO Auto-generated method stub
		mParams.applyTo(params);
	}

	@Override
	public void commitParam(SerParam params) {
		// TODO Auto-generated method stub
		mParams.applyTo(params);
		applyServiceParams();
	}

	@Override
	public void resetParam(SerParam params) {
		// TODO Auto-generated method stub
		mParams.applyTo(params);
		applyServiceParams();
	}

	@Override
	public void abortService() {
		// TODO Auto-generated method stub
		synchronized (this) {
			mAborted = true;
			if (mRequest != null) {
				Log.d(this.getClass().getSimpleName(),
						"Need to cancel current request:" + mRequest.toString());
				mRequest.cancel();
				mRequest = null;
			}
			if (mHttpClient != null) {
				Log.d(this.getClass().getSimpleName(), "Shutdown connection!");
				mHttpClient.getConnectionManager().shutdown();
			}
		}
	}

	@Override
	public boolean isAborted() {
		// TODO Auto-generated method stub
		return mAborted;
	}

	/**
	 * 应用服务参数
	 */
	protected void applyServiceParams() {
		HelperUtil.applyServiceParams(mParams, mHttpClient);
	}

	/**
	 * 重置http服务状态
	 */
	protected void reset() {
		//服务已经中止或尚未存在http客户时执行
		if (mAborted || mHttpClient == null) {
			HttpClient client;
			if (mHttpClient != null) {
				//服务中止状态
				client = mHttpClient;
			} else {
				//不存在服务实例
				client = new DefaultHttpClient();
			}
			HttpParams param = client.getParams();
			SchemeRegistry schreg = client.getConnectionManager()
					.getSchemeRegistry();
			mHttpClient = new DefaultHttpClient(new SingleClientConnManager(
					param, schreg), param);
			mAborted = false;
		}
	}

	/**
	 * 控制请求对象的发送
	 * @param request 发送请求
	 * @return
	 * @throws CancellationException
	 * @throws ClientProtocolException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws CokeResponseException
	 */
	protected <T> T Send(AbstractRequest<T> request)
			throws CancellationException, ClientProtocolException,
			IllegalArgumentException, IOException, ResponseException {
		try {
			synchronized (this) {
				if (mAborted) {
					throw new CancellationException();
				} else if (mRequest != null) {
					throw new IllegalStateException(
							"Another request is still executing!");
				}
				request.setHttpClient(mHttpClient);
				mRequest = request;
			}
			return request.send();
		} catch (CancellationException e) {
			throw e;
		} catch (SocketTimeoutException e) {
			if (mAborted) {
				throw new CancellationException();
			} else {
				throw e;
			}
		} catch (IOException e) {
			HttpClient httpClient = mHttpClient;
			synchronized (this) {
				mHttpClient = null;
				reset();
				Log.w(this.getClass().getSimpleName(),
						"Connection need reset!");
			}
			if (mAborted) {
				throw new CancellationException();
			} else {
				httpClient.getConnectionManager().shutdown();
				throw e;
			}
		} finally {
			synchronized (this) {
				mRequest = null;
			}
		}

	}
}
