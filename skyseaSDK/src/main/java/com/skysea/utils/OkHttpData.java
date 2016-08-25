package com.skysea.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;

/**
 * Created by jyd-pc006 on 16/8/24.
 */
public class OkHttpData {

    private int mHashCode = 0;

    /**
     * 超时时间5秒
     */
    private final int CONNECT_TIME_OUT = 5000;

    public OkHttpData() {
    }

    public OkHttpData(int hashCode) {
        this.mHashCode = hashCode;
    }

    /**
     * 无参get
     *
     * @param callback
     * @param url
     */
    public void get(Callback callback, String url) throws IllegalArgumentException {
        GetBuilder get = OkHttpUtils.get();
        get.url(url);
        get.tag(mHashCode);
        RequestCall requestCall = get.build();
        requestCall.connTimeOut(CONNECT_TIME_OUT);
        requestCall.execute(callback);
    }

    /**
     * get方式请求服务端数据
     *
     * @param callback
     * @param url      服务端url
     * @param params   参数
     */
    public void get(Callback callback, String url, HashMap<String, String> params) throws IllegalArgumentException {
        GetBuilder get = OkHttpUtils.get();
        get.url(url);
        get.tag(mHashCode);
        get.params(params);
        RequestCall requestCall = get.build();
        requestCall.connTimeOut(CONNECT_TIME_OUT);
        requestCall.execute(callback);
    }

    /**
     * 无参post
     *
     * @param callback
     * @param url      服务端url
     */
    public void post(Callback callback, String url) throws IllegalArgumentException {
        PostFormBuilder post = OkHttpUtils.post();
        post.url(url);
        post.tag(mHashCode);
        RequestCall requestCall = post.build();
        requestCall.connTimeOut(CONNECT_TIME_OUT);
        requestCall.execute(callback);
    }

    /**
     * post方式请求服务端数据
     *
     * @param callback
     * @param url      服务端url
     * @param params   参数
     */
    public void post(Callback callback, String url, HashMap<String, String> params) throws IllegalArgumentException {
        PostFormBuilder post = OkHttpUtils.post();
        post.url(url);
        post.tag(mHashCode);
        post.params(params);
        RequestCall requestCall = post.build();
        requestCall.connTimeOut(CONNECT_TIME_OUT);
        requestCall.execute(callback);
    }

}
