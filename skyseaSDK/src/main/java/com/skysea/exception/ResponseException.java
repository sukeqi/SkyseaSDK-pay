package com.skysea.exception;

public class ResponseException extends Exception {
	public final static int UnknownError = 17; 	

	public ResponseException(String reason, String message) {
		super(message);
		mErrorCode = parseReason(reason);
		mErrorMsg = message;
	}
	
	public ResponseException(int reason, String message) {
		super(message);
		mErrorCode = reason;
		mErrorMsg = message;
	}
	
	public ResponseException(String message) {
		super(message);
		mErrorCode = UnknownError;
		mErrorMsg = message;
	}
	
	public int getReason() {
		return mErrorCode;
	}
	
	public String getErrorMessage(){
		return mErrorMsg;
	}

	protected final static int parseReason(String reason) {
		return UnknownError;
	}
	private int mErrorCode;
	private String mErrorMsg;
}
