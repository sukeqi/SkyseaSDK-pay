package com.skysea.async;

import java.util.HashSet;

public class AutoCancelController {
	public AutoCancelController() {
		mGcSet = new HashSet<Cancelable>();
	}
	
	AutoCancelController(int capacity){
		mGcSet = new HashSet<Cancelable>(capacity);
	}
	
	/**
	 * 添加受托管清理的任务
	 * @param task
	 * @return
	 */
	public boolean add(Cancelable task){
		return mGcSet.add(task);
	}
	
	/**
	 * 删除受托管清理的任务
	 * @param task
	 * @return
	 */
	public boolean remove(Cancelable task){
		return mGcSet.remove(task);
		
	}
	/**
	 * 执行取消操作。将对每一个受托管清理的任务
	 * 进行取消操作，并在执行后清空列表
	 */
	public void clean(){
		for(Cancelable task: mGcSet){
			task.cancel();
		}
		mGcSet.clear();
		
	}
	
	/**
	 * 清除所有受托管清理的任务（并不取消任务）
	 */
	public void clear(){
		mGcSet.clear();
	}
	public HashSet<Cancelable> mGcSet = null;
}
