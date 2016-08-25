package com.skysea.async;

public interface Cancelable {
	void cancel();
	boolean isCanceled();
}
