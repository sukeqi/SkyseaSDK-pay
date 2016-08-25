package com.skysea.bean;

public class OrderInfo {
	
	private String userid;
	private String gameid;
	private String gameserverid;
	private String amount;
	private String payment_mode;
	private String payment_mode_value;
	private String order_num;
	private String xb_orderid;

	public String getXb_orderid() {
		return xb_orderid;
	}
	public void setXb_orderid(String xb_orderid) {
		this.xb_orderid = xb_orderid;
	}
	public String getPayment_mode_value() {
		return payment_mode_value;
	}
	public void setPayment_mode_value(String payment_mode_value) {
		this.payment_mode_value = payment_mode_value;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	private String username;
	private String gamename;
	private String gameservername;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getGameid() {
		return gameid;
	}
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	public String getGameserverid() {
		return gameserverid;
	}
	public void setGameserverid(String gameserverid) {
		this.gameserverid = gameserverid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	 
	public String getPayment_mode() {
		return payment_mode;
	}
	public void setPayment_mode(String payment_mode) {
		this.payment_mode = payment_mode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public String getGameservername() {
		return gameservername;
	}
	public void setGameservername(String gameservername) {
		this.gameservername = gameservername;
	}
	
}
