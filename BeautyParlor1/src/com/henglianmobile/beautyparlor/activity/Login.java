package com.henglianmobile.beautyparlor.activity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.LoginResultObject;
import com.henglianmobile.beautyparlor.entity.UserInfo;
import com.henglianmobile.beautyparlor.ui.activity.consumer.UpdatePeronalInfoActivity;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MD5Util;
import com.henglianmobile.beautyparlor.util.SPUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.henglianmobile.beautyparlor.view.OnLoginListener;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * ��¼
 * @author Administrator
 *
 */
public class Login extends BaseActivity {
	private Platform oPlatform;
	private UserInfo userInfo = new UserInfo();
	private int sex;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		String logoutTag = getIntent().getStringExtra("logout");
		showDemo(logoutTag);
	}
	private void showDemo(String tag) {
		LoginAndThirdPartyLoginActivity tpl = new LoginAndThirdPartyLoginActivity(Login.this,tag);
		tpl.setOnLoginListener(new OnLoginListener() {
			public boolean onSignin(String platform, HashMap<String, Object> res) {
				// ��������Ȩͨ��
				oPlatform = ShareSDK.getPlatform(platform);
				if (oPlatform != null) {
					String userId = oPlatform.getDb().getUserId();
					String gender = oPlatform.getDb().getUserGender();
					if (gender.equals("m")) {
						userInfo.setUserGender(UserInfo.Gender.BOY);
						gender = "��";
						sex = 1;
					} else {
						userInfo.setUserGender(UserInfo.Gender.GIRL);
						gender = "Ů";
						sex = 0;
					}
					userInfo.setUserId(userId);
					userInfo.setUserIcon(oPlatform.getDb().getUserIcon());
					userInfo.setUserName(oPlatform.getDb().getUserName());
					userInfo.setUserNote(oPlatform.getDb().getUserId());
					Log.i("info", "userInfo=" + userInfo.toString());
				}
				// ���б��ص�¼
				login();
				return true;
			}

			public boolean onSignUp(UserInfo info) {
				// ��д����ע����Ϣ�Ĵ��룬����true��ʾ���ݺϷ���ע��ҳ����Թر�
				return true;
			}
		});
		tpl.show(this);
	}

	private void login() {
		String url = Const.LOGONURL+"uname="+userInfo.getUserId()+"&upass="+MD5Util.generatePassword(userInfo.getUserId());
		LogUtil.i("url", "LoginActivity-reff-url="+url);
		sendGetHttpLogon(url);
	}

	private void sendGetHttpLogon(String url) {
		LogUtil.i("url", "LoginActivity--url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck===", responseString);
					Type type = new TypeToken<List<LoginResultObject>>() {
					}.getType();
					List<LoginResultObject> loginResultObjects = TApplication.gson.fromJson(
							responseString, type);
					LoginResultObject loginResultObject = loginResultObjects.get(0);
					if("1".equals(loginResultObject.getResponse())){
						int uid = Integer.parseInt(loginResultObject.getUid());
						if(uid == -1){
//							Tools.showMsg(Login.this, "�û���������!");
							//����ע��
							register();
						}else if(uid == -2){
							Tools.showMsg(Login.this, "�����������������!");
						}else if(uid > 0){
							//��¼�ɹ�
							TApplication.user = loginResultObject;
//							SPUtil.getUtil(Login.this).saveToSp(SPUtil.USERNAME, userInfo.getUserId());
//							SPUtil.getUtil(Login.this).saveToSp(SPUtil.PWD, userInfo.getUserId());
							//�޸ĸ�����Ϣ
							updateUserInfo();
							
						}
					}else if("0".equals(loginResultObject.getResponse())){
						Tools.showMsg(Login.this, "��������!");
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(Login.this, HTTP_ERROR);
			}
		});
	}
	protected void updateUserInfo() {
		String url = Const.UPDATEUSERINFODETAILURL
				+ "userId=" + TApplication.user.getUid()
				+ "&headImg=" + userInfo.getUserIcon()
				+ "&nickname=" + userInfo.getUserName() 
				+ "&sphone="
				+ "&sex="+sex
				+ "&age=0"
				+ "&address="
				+ "&signInfo="
				+ "&content="
				+ "&bankName="
				+ "&accountNo=";
		submitHttpGet(url);
	}
	private void submitHttpGet(String url) {
		LogUtil.i("url", "Login----UpdatePeronalInfoActivity-submitHttpGet-url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = jsonObject
								.getInt("response");
						if (response == 0) {
							Tools.showMsg(Login.this, "��������!");
						} else if (response == 1) {
//							Tools.showMsg(Login.this, "�޸ĳɹ�!");
							MainActivity_.intent(Login.this).start();
							Login.this.finish();
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
				Tools.showMsg(Login.this, HTTP_ERROR);
			}
		});
	}
	protected void register() {
		String url = Const.REGISTERURL + "uname=" + userInfo.getUserId() + "&upass=" + MD5Util.generatePassword(userInfo.getUserId())
				+ "&phone="+""+"&types="+Constant.CONSUMER;
		LogUtil.i("url", "Login---url="+url);
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
							Tools.showMsg(Login.this, "ע��ʧ�ܣ����Ժ�����ע��!");
						}else if(response == -2){
							Tools.showMsg(Login.this, "���ֻ��Ż��û����Ѿ�ע�ᣬ������ֻ���!");
						}else if(response > 0){
//							Tools.showMsg(Login.this, "ע��ɹ�!");
							login();
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
				Tools.showMsg(Login.this, HTTP_ERROR);
			}
		});
	}
	public void finishLoginActivity() {
		Login.this.finish();
	}
}
