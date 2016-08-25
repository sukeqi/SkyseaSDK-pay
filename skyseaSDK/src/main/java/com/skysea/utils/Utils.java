package com.skysea.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
	/**
	 * check net
	 */
	public static boolean isConnectToInternet(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * ProgressDialog
	 */
	public static ProgressDialog show(Context context, int titleResId,
			int messageResId) {
		try {
			ProgressDialog pd = new ProgressDialog(context);
			pd.setTitle(titleResId);
			pd.setMessage(context.getText(messageResId));
			pd.setCancelable(true);
			pd.show();
			return pd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ProgressDialog show(Context context, int titleResId,
			int messageResId, OnCancelListener cancelListener) {
		try {
			ProgressDialog pd = new ProgressDialog(context);
			pd.setTitle(titleResId);
			pd.setMessage(context.getText(messageResId));
			pd.setCancelable(true);
			pd.setOnCancelListener(cancelListener);
			pd.show();
			return pd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setText(ProgressDialog pd, String title, String message) {
		if (pd == null)
			return;

		if (title != null)
			pd.setTitle(title);

		if (message != null)
			pd.setMessage(message);
	}

	public static void setText(ProgressDialog pd, String title, String message,
			OnCancelListener cancelListener) {
		if (pd == null)
			return;

		if (cancelListener != null)
			pd.setOnCancelListener(cancelListener);

		if (title != null)
			pd.setTitle(title);

		if (message != null)
			pd.setMessage(message);
	}

	public static void dismiss(ProgressDialog pd) {
		if (pd == null)
			return;

		if (pd.isShowing() && pd.getWindow() != null) {
			try {
				pd.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static AlertDialog.Builder showAlertDialog(Context context,
			int titleId, int messageId) {

		try {
			AlertDialog.Builder ab = new AlertDialog.Builder(context);
			ab.setTitle(titleId);
			ab.setMessage(messageId);
			ab.setPositiveButton("确定", null);
			ab.show();
			return ab;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static AlertDialog.Builder showAlertDialog(Context context,
			String title, String message) {

		try {
			AlertDialog.Builder ab = new AlertDialog.Builder(context);
			ab.setTitle(title);
			ab.setMessage(message);
			return ab;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String subPayResultString(String result){
		String index1String = "result={";
		int index1 = result.indexOf(index1String);
		index1 += index1String.length();
		int index2 = result.length()-1;
		String res = result.substring(index1, index2);
		 
		String urlResult = null;
		try {
			urlResult = URLEncoder.encode(res, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return urlResult;
	}
	
	// login config
	
	public static void setLoginSharePreferences(Activity mActivity,String username){
		SharedPreferences.Editor userinfo = mActivity.getSharedPreferences("user_info", 0).edit();
		userinfo.putString("username", username);
		userinfo.commit();
	}
	
	public static String getLoginSharePreferences(Activity mActivity){
		
		String username = "";
		SharedPreferences userinfo = mActivity.getSharedPreferences("user_info", 0);
		if(userinfo != null){
			username = userinfo.getString("username", "");
		}
		return username;
	}
	
	public static void setTemporaryInfo(Activity mActivity,String username,String pwd){
		SharedPreferences.Editor userinfo = mActivity.getSharedPreferences("t_userinfo", 0).edit();
		userinfo.putString("tusername", username);
		userinfo.putString("tuserpwd", pwd);
		userinfo.commit();
	}
	
	public static String[] getTemporaryInfo(Activity mActivity){
		String username = "";
		String userpwd = "";
		SharedPreferences userinfo = mActivity.getSharedPreferences("t_userinfo", 0);
		if(userinfo != null){
			username = userinfo.getString("tusername", "");
			userpwd = userinfo.getString("tuserpwd", "");
		}
		return new String[]{username,userpwd};
	}
	
	public static void clearTemporaryInfo(Activity mActivity){
		SharedPreferences.Editor temporary = mActivity.getSharedPreferences("t_userinfo", 0).edit();
		temporary.clear();
		temporary.commit();
	}
}
