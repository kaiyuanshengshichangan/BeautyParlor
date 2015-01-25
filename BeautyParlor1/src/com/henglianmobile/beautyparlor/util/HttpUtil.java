package com.henglianmobile.beautyparlor.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * ͨ��async��ܷ���http����
 * 
 * @author Administrator
 * 
 */
public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient();
	/** ʵ�������� */
	static {
		client.setTimeout(11000); // �������ӳ�ʱ����������ã�Ĭ��Ϊ10s
	}

	/** ��һ������url��ȡһ��string���� */
	public static void get(String urlString, AsyncHttpResponseHandler res) {

		client.get(urlString, res);
	}

	// url���������
	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	// url���������
	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	// ������������ȡjson�����������
	public static void get(String urlString, JsonHttpResponseHandler res) {
		client.get(urlString, res);
	}

	// ����������ȡjson�����������
	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	// ��������ʹ�ã��᷵��byte����
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}

	// ��������get����(��������һ��URL)
	/*
	private void sendGetHttp(String url) {
		// LogUtil.i("url", "LoginActivity--url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			// �ɹ���ִ�д˷���
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("res", "res=" + responseString);

				}
			}

			// ����ʧ�ܺ�ִ�д˷���
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString,
					Throwable arg3) {

			}
		});
	}
	*/
	
	//��������post����(��������һ��URL)
	/*
	private void sendPostHttp(String url) {

		RequestParams params = new RequestParams();
		params.put("", "");
		params.put("", "");
		params.put("", "");
		HttpUtil.post(url, params, new TextHttpResponseHandler() {

			// �ɹ���ִ�д˷���
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "res=" + responseString);

				}
			}

			// ����ʧ�ܺ�ִ�д˷���
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString,
					Throwable arg3) {

			}
		});
	}
	*/
}
