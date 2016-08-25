package com.skysea.utils;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.skysea.config.Constants;


public class AssistInputStream extends FilterInputStream {
	private static final String RESPONSE_RECV_TAG = "<<";
	private ByteArrayOutputStream mTraceBuffer;
	private boolean mbDumpContent = true;

	/**
	 * @param in
	 * 			只能追踪有限字节数的流
	 */
	public AssistInputStream(InputStream in) {
		super(in);
		if (mbDumpContent && Constants.DEBUG) {
			mTraceBuffer = new ByteArrayOutputStream(1024);
		}
	}

	public AssistInputStream(InputStream is, boolean bDumpContent) {
		super(is);
		mbDumpContent = bDumpContent;
		if (mbDumpContent && Constants.DEBUG) {
			mTraceBuffer = new ByteArrayOutputStream(1024);
		}
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		int num = super.read();
		if (num >= 0) {
			if (mbDumpContent && mTraceBuffer != null) {
				mTraceBuffer.write(num);
			}
		} else {
			dump();
		}
		return num;
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		// TODO Auto-generated method stub
		return read(buffer, 0, buffer.length);
	}

	@Override
	public int read(byte[] buffer, int offset, int count) throws IOException {
		// TODO Auto-generated method stub
		int num = super.read(buffer, offset, count);
		if (num >= 0) {
			if (mbDumpContent && mTraceBuffer != null) {
				mTraceBuffer.write(buffer, 0, num);
			}
		} else {
			dump();
		}
		return num;
	}

	/**
	 *倾倒出流内容 
	 */
	void dump() {
		if (mbDumpContent && mTraceBuffer != null) {
			try {
				Log.d(RESPONSE_RECV_TAG, mTraceBuffer.toString("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mTraceBuffer = null;
		}
	}
}
