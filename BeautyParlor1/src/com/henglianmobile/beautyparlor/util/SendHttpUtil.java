package com.henglianmobile.beautyparlor.util;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.TextHttpResponseHandler;
/**
 * ������������
 * ����ͳһ�ķ���ֵΪ0��1����������
 * ������ʾ
 * @author Administrator
 *
 */
public class SendHttpUtil {
	/**
	 * ����ͳһ�ķ���ֵΪ0��1����������
	 * @param url 
	 * @param context
	 * @param message1
	 * @param message2
	 */
	public static void submitHttpGet(String url,final Context context,final String failed,final String success,final boolean close) {
		LogUtil.i("url", context+"---url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = Integer.parseInt(jsonObject.getString("response"));
						if(response == 0){
							Tools.showMsg(context, failed);
						}else if(response > 0){
							//
							Tools.showMsg(context, success);
							if(close){
								((Activity)context).finish();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(context, Tools.HTTP_ERROR);
			}
		});
	}
}
