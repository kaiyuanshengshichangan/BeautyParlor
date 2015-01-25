package com.henglianmobile.beautyparlor.util;

import android.util.Log;
/**
 * 日志统一管理，可在此将日志通过邮件发送出去
 * @author Administrator
 *
 */
public class LogUtil {
	public static void e(String tag,String msg){
		if(!Constant.ISRELEASE){
			Log.e(tag, msg);
		}
	}
	public static void i(String tag,Object msg){
		if(!Constant.ISRELEASE){
			Log.i(tag, String.valueOf(msg));
		}
	}
}
