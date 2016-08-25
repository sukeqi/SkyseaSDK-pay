package com.skysea.request;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.skysea.async.Cancelable;
import com.skysea.config.Constants;
import com.skysea.exception.ResponseException;
import com.skysea.utils.AssistInputStream;



public abstract class AbstractRequest<R> implements Cancelable {

	protected boolean mCanceled;
	/**
	 * http客户类
	 */
	private HttpClient mHttpClient;
	/**
	 * http基础请求，可根据情况转换为不同的请求类型
	 */
	private HttpRequestBase mHttpRequest;
	/**
	 * 参数列表
	 */
	private ArrayList<NameValuePair> mRequestParams = new ArrayList<NameValuePair>();
	
	/**
	 * 追踪标记
	 */
	private static final String REQUEST_TAG = ">>";
	/**
	 * GET请求
	 */
	protected final static String METHOD_GET = "GET";
	/**
	 * POST请求
	 */
	protected final static String METHOD_POST = "POST";
	
	
	/**
	 * 根据请求方法初始化请求对象
	 * 
	 * @param method
	 */
	public AbstractRequest(String method) {
		if (method.equalsIgnoreCase(METHOD_GET)) {
			mHttpRequest = new HttpGet();
		} else {
			mHttpRequest = new HttpPost();
		}
	}
	
	/**
	 * @param httpClient
	 *            请求所用到的http客户
	 * @return 请求对象
	 */
	public synchronized AbstractRequest<R> setHttpClient(HttpClient httpClient) {
		mHttpClient = httpClient;
		return this;
	}
	
	/**
	 * 抽象发送请求方法在子类中要求实现
	 * 
	 * @return 结果业务对象
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws CancellationException
	 * @throws IllegalArgumentException
	 * @throws CokeResponseException
	 */
	public abstract  R send() throws ClientProtocolException, IOException,
			CancellationException, IllegalArgumentException,
			ResponseException;
	
	/**
	 * 设置相关参数
	 * 
	 * @param name
	 * @param value
	 */
	protected final void setRequestParam(String name, String value) {
		mRequestParams.add(new BasicNameValuePair(name, value));
	}
	
	/**
	 * 追踪请求字串
	 * 
	 * @param request
	 * @param params
	 */
	protected final void dumpRequest(HttpRequestBase request,
			ArrayList<NameValuePair> params) {
		Log.d(REQUEST_TAG, request.getRequestLine().toString());
		Header[] headers = request.getAllHeaders();
		for (Header header : headers) {
			Log.d(REQUEST_TAG, header.toString());
		}
		for (NameValuePair param : params) {
			Log.d(REQUEST_TAG, param.toString());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see andr.paiyao.ui.utils.Cancelable#cancel()
	 */
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		synchronized (this) {
			mCanceled = true;
			if (mHttpClient != null) {
				mHttpClient.getConnectionManager().shutdown();
			}
		}
	}

	@Override
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return mCanceled;
	}
	
	
	/**
	 * 请求发送方法
	 * 
	 * @param uri
	 *            发送的地址
	 * @return 结果流
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public InputStream send(String uri) throws ClientProtocolException,
			IOException {
		String reqUri = uri;
		InputStream responseStream = null;
		// get方法处理，参数放在url后面请求
		if (mHttpRequest.getMethod().equalsIgnoreCase(METHOD_GET)) {
			mHttpRequest.setHeader("Content-Type", "text/xml; charset=utf-8");
			if (!mRequestParams.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				boolean bFirstParam = true;
				// 拼接属性与请求url
				for (NameValuePair pair : mRequestParams) {
					if (!bFirstParam) {
						sb.append("&");
					} else {
						bFirstParam = false;
					}
					sb.append(pair.getName() + "=");
					sb.append(pair.getValue());
				}
				reqUri = reqUri + "?" + sb;
			}
		} else { // post方法处理，参数放在实体中
			HttpPost postMethod = (HttpPost) mHttpRequest;
			// 请求参数不为空，将参数放入表单实体,设置头的内容类型为application/x-www-form-urlencoded
			if (!mRequestParams.isEmpty()) {
				postMethod.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
				postMethod.setEntity(new UrlEncodedFormEntity(mRequestParams,
						HTTP.UTF_8));
			} else { // 无参数，直接设置头的内容类型为text/xml
				postMethod.setHeader("Content-Type", "text/xml; charset=utf-8");
			}
		}
		// 设置请求uri
		mHttpRequest.setURI(URI.create(reqUri));
		synchronized (this) {
			if (mHttpClient == null) {
				mHttpClient = new DefaultHttpClient();
			}
		}
		if (mCanceled) {
			throw new CancellationException();
		}
		if (Constants.DEBUG) {
			dumpRequest(mHttpRequest, mRequestParams);
		}
		HttpResponse response = null;
		try {
			response = mHttpClient.execute(mHttpRequest);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (mCanceled) {
				throw new CancellationException();
			} else {
				throw e;
			}
		}
		StatusLine statusLine = response.getStatusLine();
		if (statusLine != null) {
			int statusCode = statusLine.getStatusCode();
			int internalStatusCode = 200;
			Header statusCodeHeader = response.getFirstHeader("Status-Code");
			// 获得内部状态码
			if (statusCodeHeader != null) {
				internalStatusCode = Integer.parseInt(statusCodeHeader
						.getValue());
			}
			if (statusCode < 200 || statusCode >= 300
					|| internalStatusCode < 200 || internalStatusCode >= 300) {
				throw new HttpResponseException(statusCode, response
						.getStatusLine().getReasonPhrase());
			}
			
			
			// 获得实体中的流接近
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				if (is != null) {
					// 日后需要修改部分，只能追踪有限流
					responseStream = new AssistInputStream(is);
				}
			}

		}
		return responseStream;
	}
}
