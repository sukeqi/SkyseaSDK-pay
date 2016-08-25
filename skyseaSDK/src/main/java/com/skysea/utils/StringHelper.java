/**
 * 
 */
package com.skysea.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 字符串帮助类
 * @author sky
 */
public class StringHelper {

	public static String inputStreamToString (InputStream in){
		
		StringBuffer bf = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String result = "";
		try {
			while ((result = br.readLine()) != null) {
				bf.append(result);
			}
			return bf.toString();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}
	
}
