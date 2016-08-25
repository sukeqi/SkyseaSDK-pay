package com.skysea.sdk.main;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;

import com.skysea.android.app.lib.MResource;
import com.skysea.app.BaseActivity;

public class AgreementActivity extends BaseActivity implements OnClickListener {

	WebView wv;
	Button btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplicationContext(), "layout", "activity_agreement"));

		initView();
		WebSettings webSettings = wv.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wv.loadUrl("http://www.ya247.com/sjreg_msg.html");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(0);
			AgreementActivity.this.finish();
			anim();
		}
		return false;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			 }
		super.onResume();
	}

	private void initView() {
		wv = (WebView) this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "wv_agreement"));
		btn_back = (Button) this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "btn_agreement_back"));
		btn_back.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		int viewid = v.getId();
		if(viewid == btn_back.getId()){
			setResult(0);
			AgreementActivity.this.finish();
			anim();
		}
			
	}

	private void anim() {
		overridePendingTransition(MResource.getIdByName(AgreementActivity.this,
				"anim", "page_from_alpha"), MResource.getIdByName(
				AgreementActivity.this, "anim", "page_left_alpha"));
	}
}
