package com.skysea.sdk.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.skysea.alipay.AlixPay;
import com.skysea.android.app.lib.MResource;
import com.skysea.app.BaseActivity;
import com.skysea.bean.OrderInfo;

public class OrderInfoActivity extends BaseActivity implements OnClickListener{

	Button btn_order_back;
	Button btn_order_submit;
	TextView tv_order_paymentmode;
	TextView tv_order_num;
	TextView tv_order_username;
	TextView tv_order_gamename;
	TextView tv_order_gameservername;
	TextView tv_order_amount;
	OrderInfo orderInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MResource.getIdByName(getApplicationContext(), "layout", "activity_orderinfo"));
		
		initViews();
		
		initListeners();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			 }
		super.onResume();
	}

	private void initViews(){
		btn_order_back = (Button)this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "btn_orderinfo_back"));
		btn_order_submit = (Button)this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "btn_toalipay"));
		tv_order_amount = (TextView)this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "tv_orderinfo_amount"));
		tv_order_gamename = (TextView)this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "tv_orderinfo_gamaname"));
		tv_order_gameservername = (TextView)this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "tv_orderinfo_gameservername"));
		tv_order_num = (TextView)this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "tv_orderinfo_num"));
		tv_order_paymentmode = (TextView)this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "tv_orderinfo_payment"));
		tv_order_username = (TextView)this.findViewById(MResource.getIdByName(getApplicationContext(), "id", "tv_orderinfo_username"));
	}

	private void initListeners(){
		btn_order_back.setOnClickListener(this);
		btn_order_submit.setOnClickListener(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Intent intent = getIntent();
		
		orderInfo = new OrderInfo();
		orderInfo.setAmount(intent.getExtras().getString("amount"));
		orderInfo.setGamename(intent.getExtras().getString("gamename"));
		orderInfo.setGameservername(intent.getExtras().getString("servername"));
		orderInfo.setPayment_mode(intent.getExtras().getString("payment_mode"));
		orderInfo.setPayment_mode_value(intent.getExtras().getString("payment_mode_value"));
		orderInfo.setUsername(intent.getExtras().getString("username"));
		orderInfo.setOrder_num(intent.getExtras().getString("ordernum"));
		
		tv_order_paymentmode.setText(orderInfo.getPayment_mode_value());
		tv_order_num.setText(orderInfo.getOrder_num());
		tv_order_gamename.setText(orderInfo.getGamename());
		tv_order_username.setText(orderInfo.getUsername());
		tv_order_gameservername.setText(orderInfo.getGameservername());
		tv_order_amount.setText(hanlerAmount(orderInfo.getAmount())+getString(MResource.getIdByName(OrderInfoActivity.this, "string", "order_yuan")));
	
	}
	
	private String hanlerAmount(String amount){
		int money = Integer.valueOf(amount);
		return money+"";
	}
	
	@Override
	public void onClick(View v) {
		int viewid = v.getId();
		
		if(viewid == btn_order_back.getId()){
			setResult(301);
			OrderInfoActivity.this.finish();
			anim();
		}else if (viewid == btn_order_submit.getId()) {
			submitOrder();
		}
	}
	
	private void submitOrder(){
		//alipay = 21
		if (Integer.parseInt(orderInfo.getPayment_mode()) == 21) {
			AlixPay alixPay = new AlixPay(OrderInfoActivity.this, orderInfo);
			alixPay.pay();
		} 
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void anim() {
		overridePendingTransition(MResource.getIdByName(
				OrderInfoActivity.this, "anim", "page_from_alpha"),
				MResource.getIdByName(OrderInfoActivity.this, "anim",
						"page_left_alpha"));
	}
}
