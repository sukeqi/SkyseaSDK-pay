package com.skysea.bean;

public class CItem {
	private int ID	;
	private String Value = "";
	public boolean isSelect;

	public CItem(){
		ID = 0;
		Value = "";
	}
	
	public CItem(int _id,String _value){
		ID = _id;
		Value = _value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
		return Value;
	}
	
	public int getId(){
		return ID;
	}
	
	public String getValue(){
		return Value;
	}
	
}
