package com.henglianmobile.beautyparlor.activity;

import java.lang.reflect.Type;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.LoginResultObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MD5Util;
import com.henglianmobile.beautyparlor.util.SPUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 登录
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
	@ViewById
	protected EditText et_phone_number, et_password;
	private String phoneNumber, password;

	@Extra
	protected String logout;

	@AfterViews
	void intiView() {
		et_phone_number.setText(SPUtil.getUtil(this).getFromSp(SPUtil.USERNAME,
				""));
		et_password.setText(SPUtil.getUtil(this).getFromSp(SPUtil.PWD, ""));
		if (!"1".equals(logout)) {
			if (check()) {
				logon();
			}
		}
	}

	@Click
	void btn_back() {
		this.finish();
	}

	@Click
	void btn_logon() {
		if (check()) {
			logon();
		}
	}

	@Click
	void btn_register() {
		RegisterActivity_.intent(LoginActivity.this).start();
	}

	@Click
	void btn_forget_password() {
		ForgetPwdActivity_.intent(LoginActivity.this).start();
	}

	@Click
	void iv_weixin() {

	}

	@Click
	void iv_qq() {

	}

	private boolean check() {
		phoneNumber = et_phone_number.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			Tools.showMsg(this, "请输入手机号!");
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			Tools.showMsg(this, "请输入密码");
			return false;
		}
		return true;
	}

	private void logon() {
		String url = Const.LOGONURL + "uname=" + phoneNumber + "&upass="
				+ MD5Util.generatePassword(password);
		sendGetHttpLogon(url);
	}

	private void sendGetHttpLogon(String url) {
		LogUtil.i("url", "LoginActivity--url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<LoginResultObject>>() {
					}.getType();
					List<LoginResultObject> loginResultObjects = TApplication.gson.fromJson(
							responseString, type);
					LoginResultObject loginResultObject = loginResultObjects.get(0);
					if("1".equals(loginResultObject.getResponse())){
						int uid = Integer.parseInt(loginResultObject.getUid());
						if(uid == -1){
							Tools.showMsg(LoginActivity.this, "用户名不存在!");
						}else if(uid == -2){
							Tools.showMsg(LoginActivity.this, "密码错误，请重新输入!");
						}else if(uid > 0){
							//登录成功
							TApplication.getInstance().user = loginResultObject;
							SPUtil.getUtil(LoginActivity.this).saveToSp(SPUtil.USERNAME, phoneNumber);
							SPUtil.getUtil(LoginActivity.this).saveToSp(SPUtil.PWD, password);
							MainActivity_.intent(LoginActivity.this).start();
							LoginActivity.this.finish();
						}
					}else if("0".equals(loginResultObject.getResponse())){
						Tools.showMsg(LoginActivity.this, "操作错误!");
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(LoginActivity.this, HTTP_ERROR);
			}
		});
	}
}
