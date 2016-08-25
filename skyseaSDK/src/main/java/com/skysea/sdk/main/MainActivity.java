package com.skysea.sdk.main;

import com.skysea.app.Matrix;
import com.skysea.interfaces.IDispatcherCallback;
import com.skysea.interfaces.TicketInfoListener;
import com.skysea.sdk.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	Button btnLogin;
	Button btnPay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnLogin = (Button)findViewById(R.id.button1);
		btnPay = (Button)findViewById(R.id.button2);
		btnLogin.setOnClickListener(this);
		btnPay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
		    Matrix.invokeLoginHandler(MainActivity.this, new TicketInfoListener() {
				@Override
				public void onGotTicketInfo(String arg0) {
					// TODO Auto-generated method stub
					Log.v("mainactivity", "ticket:"+arg0);
				}
			});
			break;

		case R.id.button2:
			Matrix.invokeToPayHandler(MainActivity.this, "418723", "189", "1","12341",new IDispatcherCallback() {
				@Override
				public void onFinish(String result) {
					// TODO Auto-generated method stub
					Log.v("mainactivity", "pay result:"+result);
				}
			});
			break;
		}
	}

	 

 

}
