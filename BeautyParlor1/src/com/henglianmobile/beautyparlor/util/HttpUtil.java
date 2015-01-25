package com.henglianmobile.beautyparlor.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 通过async框架发送http请求
 * 
 * @author Administrator
 * 
 */
public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient();
	/** 实例话对象 */
	static {
		client.setTimeout(11000); // 设置链接超时，如果不设置，默认为10s
	}

	/** 用一个完整url获取一个string对象 */
	public static void get(String urlString, AsyncHttpResponseHandler res) {

		client.get(urlString, res);
	}

	// url里面带参数
	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	// url里面带参数
	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	// 不带参数，获取json对象或者数组
	public static void get(String urlString, JsonHttpResponseHandler res) {
		client.get(urlString, res);
	}

	// 带参数，获取json对象或者数组
	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	// 下载数据使用，会返回byte数据
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}

	// 二、发送get请求(参数传入一个URL)
	/*
	private void sendGetHttp(String url) {
		// LogUtil.i("url", "LoginActivity--url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			// 成功后执行此方法
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("res", "res=" + responseString);

				}
			}

			// 网络失败后执行此方法
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString,
					Throwable arg3) {

			}
		});
	}
	*/
	
	//三、发送post请求(参数传入一个URL)
	/*
	private void sendPostHttp(String url) {

		RequestParams params = new RequestParams();
		params.put("", "");
		params.put("", "");
		params.put("", "");
		HttpUtil.post(url, params, new TextHttpResponseHandler() {

			// 成功后执行此方法
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "res=" + responseString);

				}
			}

			// 网络失败后执行此方法
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString,
					Throwable arg3) {

			}
		});
	}
	*/
}
