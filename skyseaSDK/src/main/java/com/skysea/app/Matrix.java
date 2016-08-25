package com.skysea.app;

import com.skysea.android.app.lib.MResource;
import com.skysea.interfaces.IDispatcherCallback;
import com.skysea.interfaces.TicketInfoListener;
import com.skysea.sdk.R;
import com.skysea.sdk.main.LoginActivity;
import com.skysea.sdk.main.PaymentInfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Matrix {

	/**
	 * 此方法用于登陆
	 * 
	 * @param context
	 *            -Activity引用
	 */
	public static void invokeLoginHandler(Activity context,
			TicketInfoListener listener) {
		LoginActivity.setOnTicketInfoListener(listener);
		Intent intent = new Intent();
		intent.setClass(context, LoginActivity.class);
		context.startActivityForResult(intent, 0);
		context.overridePendingTransition(
				MResource.getIdByName(context, "anim", "page_from_alpha"),
				MResource.getIdByName(context, "anim", "page_left_alpha"));
	}

	/**
	 * 此方法用于传参进入支付页面
	 * 
	 * @param context
	 *            -Activity引用
	 * @param userid
	 *            -用户id
	 * @param gameid
	 *            -游戏id
	 * @param gameserverid
	 *            -游戏服务器id
	 * @param xb_orderid
	 * 
	 * @param callback
	 */
	public static void invokeToPayHandler(Activity context, String userid,
			String gameid, String gameserverid, String xb_orderid,IDispatcherCallback callback) {
		PaymentInfoActivity.setDispatcherCallBack(callback);
		Intent intent = new Intent();
		intent.setClass(context, PaymentInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("userid", userid);
		bundle.putString("gameid", gameid);
		bundle.putString("gameserverid", gameserverid);
		bundle.putString("xb_orderid", xb_orderid);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, 0);
		context.overridePendingTransition(
				MResource.getIdByName(context, "anim", "page_from_alpha"),
				MResource.getIdByName(context, "anim", "page_left_alpha"));
	}

}
