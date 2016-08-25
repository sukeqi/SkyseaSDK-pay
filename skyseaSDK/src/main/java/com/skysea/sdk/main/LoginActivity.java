package com.skysea.sdk.main;

import java.io.IOException;
import java.util.concurrent.CancellationException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
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
import com.skysea.bean.LoginInfo;
import com.skysea.exception.ResponseException;
import com.skysea.interfaces.TicketInfoListener;
import com.skysea.utils.Utils;

public class LoginActivity extends BaseActivity implements OnClickListener,
		OnEditorActionListener {

	EditText loginUserName;
	EditText loginUserPwd;
	Button btnUserLogin;
	Button btnUsertoRegister;
	ProgressDialog pd;
	int LOGIN_REQUEST_CODE = 200;
	static TicketInfoListener callback;
	static String _CLASSNAME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplicationContext(), "layout",
				"activity_login"));

		initViews();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	static public void setOnTicketInfoListener(TicketInfoListener listener) {
		callback = listener;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}

	private void initViews() {

		btnUserLogin = (Button) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "btn_login"));
		btnUsertoRegister = (Button) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "btn_toregister"));
		loginUserName = (EditText) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "txt_login_username"));
		loginUserPwd = (EditText) this.findViewById(MResource.getIdByName(
				getApplicationContext(), "id", "txt_login_userpwd"));

		btnUserLogin.setOnClickListener(this);
		btnUsertoRegister.setOnClickListener(this);
		loginUserPwd.setOnEditorActionListener(this);

		// temporaryinfo
		if (getIntent().getStringExtra("from") != null) {
			String[] data = Utils.getTemporaryInfo(this);
			setUserInfo(data);
		} else {
			// login info
			String username = Utils.getLoginSharePreferences(this);
			if (username != null && !username.equals("") && username != "") {
				setUserName(username);
			}
		}
	}

	private void setUserName(String username) {
		loginUserName.setText(username);
		CharSequence text = loginUserName.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	private void setUserInfo(String[] data) {
		loginUserName.setText(data[0]);
		loginUserPwd.setText(data[1]);
		CharSequence text = loginUserName.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
	}

	private LoginInfo getLogininfo() {
		LoginInfo info = new LoginInfo();
		info.setUsername(loginUserName.getText().toString());
		info.setUserpwd(loginUserPwd.getText().toString());
		return info;
	}

	private void checkInfo() {
		LoginInfo info = getLogininfo();

		if (Utils.isConnectToInternet(getApplicationContext())) {
			if (!info.getUsername().trim().equals("")
					&& !info.getUserpwd().trim().equals("")) {
				login(info);
			} else {
				Utils.showAlertDialog(LoginActivity.this, MResource
						.getIdByName(getApplicationContext(), "string",
								"login_tips_title"), MResource.getIdByName(
						getApplicationContext(), "string", "login_null"));
			}
		} else {
			Utils.showAlertDialog(LoginActivity.this, MResource.getIdByName(
					getApplicationContext(), "string", "login_tips_title"),
					MResource.getIdByName(getApplicationContext(), "string",
							"net_error"));
		}
	}

	private void login(final LoginInfo info) {
		autoCancel(new AutoCancelServiceFramework<LoginInfo, Void, String>(this) {

			@Override
			protected void onPreExecute() {
				pd = Utils.show(LoginActivity.this, MResource.getIdByName(
						getApplicationContext(), "string", "login_tips_title"),
						MResource.getIdByName(getApplicationContext(),
								"string", "login_tips_content"));
			}

			@Override
			protected String doInBackground(LoginInfo... params) {
				createIPlatCokeService();
				try {
					return mIPlatService.toLogin(params[0]);
				} catch (CancellationException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ResponseException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				Utils.dismiss(pd);
				if (result != null) {
					String[] data = result.split("&");
					String res = data[0];
					String msg = data[1];
					if (Integer.parseInt(res) < 0) {
						Toast.makeText(LoginActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}

					if (Integer.parseInt(res) > 0) {
						Utils.setLoginSharePreferences(LoginActivity.this,
								info.getUsername());
						Utils.clearTemporaryInfo(LoginActivity.this);
						callback.onGotTicketInfo(msg);
						Intent intent = new Intent();
						LoginActivity.this.setResult(100, intent);
						Log.v("login", "set");
						LoginActivity.this.finish();
						Log.v("login", "finish");
						anim();
					}

				}
			}
		}.execute(info));
	}

	@Override
	public void onClick(View v) {
		int viewid = v.getId();
		if (viewid == btnUserLogin.getId()) {
			checkInfo();
		} else if (viewid == btnUsertoRegister.getId()) {
			saveWhenToRegist();
			goToforResult(LoginActivity.this, RegisterActivity.class, null,
					LOGIN_REQUEST_CODE);
			anim();
		}
	}

	// temporaryinfo
	private void saveWhenToRegist() {
		String usernameString = loginUserName.getText().toString();
		String userpwdString = loginUserPwd.getText().toString();
		Utils.setTemporaryInfo(LoginActivity.this, usernameString,
				userpwdString);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 200) {
			callback.onGotTicketInfo(data.getExtras().getString("ticket"));
			setResult(100, data);
			finish();
			anim();
		}
	}

	// edittext action
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			btnUserLogin.performClick();
		}
		return false;
	}

	// anim
	private void anim() {
		overridePendingTransition(MResource.getIdByName(LoginActivity.this,
				"anim", "page_from_alpha"), MResource.getIdByName(
				LoginActivity.this, "anim", "page_left_alpha"));
	}

}
