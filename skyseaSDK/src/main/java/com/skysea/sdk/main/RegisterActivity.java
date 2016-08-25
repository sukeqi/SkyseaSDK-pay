package com.skysea.sdk.main;

import java.io.IOException;
import java.util.concurrent.CancellationException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.skysea.android.app.lib.MResource;
import com.skysea.app.BaseActivity;
import com.skysea.async.AutoCancelServiceFramework;
import com.skysea.bean.RegisterInfo;
import com.skysea.exception.ResponseException;
import com.skysea.utils.Utils;

public class RegisterActivity extends BaseActivity implements OnClickListener,
		OnEditorActionListener {

	TextView tv_toagreement;
	EditText et_registerName;
	EditText et_registerPwd;
	Button btnUserRegister;
	TextView btnUsertoLogin;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplicationContext(), "layout",
				"activity_register"));

		initView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}

	private void initView() {
		tv_toagreement = (TextView) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "tv_agree"));
		et_registerName = (EditText) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "txt_register_username"));
		et_registerPwd = (EditText) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "txt_register_userpwd"));
		btnUserRegister = (Button) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "register_btn"));
		btnUsertoLogin = (TextView) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "btn_tologin"));
		tv_toagreement.setOnClickListener(this);
		btnUserRegister.setOnClickListener(this);
		btnUsertoLogin.setOnClickListener(this);
		et_registerPwd.setOnEditorActionListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("from", "register");
			data.putExtras(bundle);
			setResult(0, data);
			RegisterActivity.this.finish();
			anim();
		}
		return false;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			btnUserRegister.performClick();
		}
		return false;
	}

	private RegisterInfo getRegisterInfo() {
		RegisterInfo info = new RegisterInfo();
		info.setUsername(et_registerName.getText().toString());
		info.setUserpwd(et_registerPwd.getText().toString());
		return info;
	}

	private void checkInfo() {
		RegisterInfo info = getRegisterInfo();

		if (Utils.isConnectToInternet(getApplicationContext())) {

			if (!info.getUsername().trim().equals("")
					&& !info.getUserpwd().trim().equals("")) {
				register(info);
			} else {
				Utils.showAlertDialog(RegisterActivity.this, MResource
						.getIdByName(getApplicationContext(), "string",
								"register_tips_title"), MResource.getIdByName(
						getApplicationContext(), "string", "login_null"));
			}
		} else {
			Utils.showAlertDialog(RegisterActivity.this, MResource.getIdByName(
					getApplicationContext(), "string", "register_tips_title"),
					MResource.getIdByName(getApplicationContext(), "string",
							"net_error"));
		}
	}

	private void register(final RegisterInfo info) {
		autoCancel(new AutoCancelServiceFramework<RegisterInfo, Void, String>(
				this) {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				pd = Utils.show(RegisterActivity.this, MResource.getIdByName(
						getApplicationContext(), "string",
						"register_tips_title"), MResource.getIdByName(
						getApplicationContext(), "string", "register_submit"));
			}

			@Override
			protected String doInBackground(RegisterInfo... params) {
				// TODO Auto-generated method stub
				createIPlatCokeService();

				try {
					return mIPlatService.toRegister(params[0]);
				} catch (CancellationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				pd.dismiss();
				if (result != null) {
					String[] data = result.split("&");
					String res = data[0];
					String msg = data[1];
					if (Integer.parseInt(res) < 0) {
						Toast.makeText(RegisterActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}
					if (Integer.parseInt(res) > 0) {
						Utils.setLoginSharePreferences(RegisterActivity.this,
								info.getUsername());
						Intent intent = new Intent();
						intent.putExtra("ticket", msg);
						setResult(200, intent);
						RegisterActivity.this.finish();
					}
				}
			}

		}.execute(info));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewid = v.getId();
		if (viewid == tv_toagreement.getId()) {
			goToforResult(RegisterActivity.this, AgreementActivity.class, null,
					0);
			anim();
		} else if (viewid == btnUsertoLogin.getId()) {
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("from", "register");
			setResult(0,data);
			RegisterActivity.this.finish();
			anim();
		} else if (viewid == btnUserRegister.getId()) {
			checkInfo();
		}
	}

	private void anim() {
		overridePendingTransition(MResource.getIdByName(RegisterActivity.this,
				"anim", "page_from_alpha"), MResource.getIdByName(
				RegisterActivity.this, "anim", "page_left_alpha"));
	}

}
