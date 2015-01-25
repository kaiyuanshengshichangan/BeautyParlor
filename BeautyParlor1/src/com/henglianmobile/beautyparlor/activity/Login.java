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
 * 登录
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
				// 第三方授权通过
				oPlatform = ShareSDK.getPlatform(platform);
				if (oPlatform != null) {
					String userId = oPlatform.getDb().getUserId();
					String gender = oPlatform.getDb().getUserGender();
					if (gender.equals("m")) {
						userInfo.setUserGender(UserInfo.Gender.BOY);
						gender = "男";
						sex = 1;
					} else {
						userInfo.setUserGender(UserInfo.Gender.GIRL);
						gender = "女";
						sex = 0;
					}
					userInfo.setUserId(userId);
					userInfo.setUserIcon(oPlatform.getDb().getUserIcon());
					userInfo.setUserName(oPlatform.getDb().getUserName());
					userInfo.setUserNote(oPlatform.getDb().getUserId());
					Log.i("info", "userInfo=" + userInfo.toString());
				}
				// 进行本地登录
				login();
				return true;
			}

			public boolean onSignUp(UserInfo info) {
				// 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
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
//							Tools.showMsg(Login.this, "用户名不存在!");
							//进行注册
							register();
						}else if(uid == -2){
							Tools.showMsg(Login.this, "密码错误，请重新输入!");
						}else if(uid > 0){
							//登录成功
							TApplication.user = loginResultObject;
//							SPUtil.getUtil(Login.this).saveToSp(SPUtil.USERNAME, userInfo.getUserId());
//							SPUtil.getUtil(Login.this).saveToSp(SPUtil.PWD, userInfo.getUserId());
							//修改个人信息
							updateUserInfo();
							
						}
					}else if("0".equals(loginResultObject.getResponse())){
						Tools.showMsg(Login.this, "操作错误!");
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
							Tools.showMsg(Login.this, "网络有误!");
						} else if (response == 1) {
//							Tools.showMsg(Login.this, "修改成功!");
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
							Tools.showMsg(Login.this, "注册失败，请稍后重新注册!");
						}else if(response == -2){
							Tools.showMsg(Login.this, "该手机号或用户名已经注册，请更换手机号!");
						}else if(response > 0){
//							Tools.showMsg(Login.this, "注册成功!");
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
