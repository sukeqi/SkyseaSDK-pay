package com.skysea.app;

import com.skysea.async.AutoCancelController;
import com.skysea.async.Cancelable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends Activity {

	private AutoCancelController mAutoCancelController = new AutoCancelController();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	protected void goTo(Context form, Class<? extends Activity> to, Bundle data) {
		Intent intent = new Intent();
		intent.setClass(form, to);
		if(data != null){
			intent.putExtras(data);
		}
		startActivity(intent);
	}
	
	protected void goToforResult(Context form, Class<? extends BaseActivity> to, Bundle data,int requestCode) {
		Intent intent = new Intent();
		intent.setClass(form, to);
		if(data != null){
			intent.putExtras(data);
		}
		startActivityForResult(intent, requestCode);
	}

	public void autoCancel(Cancelable task) {
		mAutoCancelController.add(task);
	}

	public void removeAutoCancel(Cancelable task) {
		mAutoCancelController.remove(task);
	}

	public AutoCancelController getAutoCancelController() {
		return mAutoCancelController;
	}
	
}
