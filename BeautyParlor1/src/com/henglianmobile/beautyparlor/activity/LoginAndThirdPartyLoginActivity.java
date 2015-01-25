package com.henglianmobile.beautyparlor.activity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.sharesdk.framework.FakeActivity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

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
import com.henglianmobile.beautyparlor.view.OnLoginListener;
import com.loopj.android.http.TextHttpResponseHandler;

public class LoginAndThirdPartyLoginActivity extends FakeActivity implements
		OnClickListener, Callback, PlatformActionListener {
	private Context context;
	private Handler handler;
	private String tag;

	private EditText et_phone_number, et_password;
	private Button btn_back, btn_logon, btn_register, btn_forget_password;
	private ImageView iv_weixin, iv_qq;
	private String phoneNumber, password;

	private OnLoginListener signupListener;

	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;
	private static final int MSG_AUTH_COMPLETE = 4;

	// @Extra
	// protected String logout;

	public LoginAndThirdPartyLoginActivity(Context context,String tag) {
		this.context = context;
		this.tag = tag;
	}

	public void onCreate() {
		activity.setContentView(R.layout.activity_login);
		handler = new Handler(this);
		initViews();
		et_phone_number.setText(SPUtil.getUtil(activity).getFromSp(
				SPUtil.USERNAME, ""));
		et_password.setText(SPUtil.getUtil(activity).getFromSp(SPUtil.PWD, ""));
		if (!"1".equals(tag)) {
			if (check()) {
				logon();
			}
		}
	}

	private void initViews() {
		et_phone_number = (EditText) activity
				.findViewById(R.id.et_phone_number);
		et_password = (EditText) activity.findViewById(R.id.et_password);
		btn_back = (Button) activity.findViewById(R.id.btn_back);
		btn_logon = (Button) activity.findViewById(R.id.btn_logon);
		btn_register = (Button) activity.findViewById(R.id.btn_register);
		btn_forget_password = (Button) activity
				.findViewById(R.id.btn_forget_password);
		iv_weixin = (ImageView) activity.findViewById(R.id.iv_weixin);
		iv_qq = (ImageView) activity.findViewById(R.id.iv_qq);
		addListener();
	}

	private void addListener() {
		btn_back.setOnClickListener(this);
		btn_logon.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		btn_forget_password.setOnClickListener(this);
		iv_weixin.setOnClickListener(this);
		iv_qq.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_logon:
			if (check()) {
				logon();
			}
			break;
		case R.id.btn_register:
			RegisterActivity_.intent(activity).start();
			break;
		case R.id.btn_forget_password:
			ForgetPwdActivity_.intent(activity).start();
			break;
		case R.id.iv_weixin:
			// 微信登录
			// 测试时，需要打包签名；sample测试时，用项目里面的demokey.keystore
			// 打包签名apk,然后才能产生微信的登录
			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
			authorize(wechat);
			break;
		case R.id.iv_qq:
			// QQ空间
			Platform qzone = ShareSDK.getPlatform(QZone.NAME);
			authorize(qzone);
			break;

		default:
			break;
		}
	}

	private boolean check() {
		phoneNumber = et_phone_number.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			Tools.showMsg(activity, "请输入手机号!");
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			Tools.showMsg(activity, "请输入密码");
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
		LogUtil.i("url", "LoginActivity--url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<LoginResultObject>>() {
					}.getType();
					List<LoginResultObject> loginResultObjects = TApplication.gson
							.fromJson(responseString, type);
					LoginResultObject loginResultObject = loginResultObjects
							.get(0);
					if("1".equals(loginResultObject.getResponse())){
						int uid = Integer.parseInt(loginResultObject.getUid());
						if(uid == -1){
							Tools.showMsg(activity, "用户名不存在!");
						}else if(uid == -2){
							Tools.showMsg(activity, "密码错误，请重新输入!");
						}else if(uid > 0){
							//登录成功
							TApplication.getInstance().user = loginResultObject;
							SPUtil.getUtil(activity).saveToSp(SPUtil.USERNAME, phoneNumber);
							SPUtil.getUtil(activity).saveToSp(SPUtil.PWD, password);
							MainActivity_.intent(activity).start();
							((Login) context).finishLoginActivity();
							activity.finish();
						}
					}else if("0".equals(loginResultObject.getResponse())){
						Tools.showMsg(activity, "操作错误!");
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(activity, Tools.HTTP_ERROR);
			}
		});
	}

	// 执行授权,获取用户信息
	// 文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
	private void authorize(Platform plat) {

		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	/** 设置授权回调，用于判断是否进入注册 */
	public void setOnLoginListener(OnLoginListener l) {
		this.signupListener = l;
	}

	public void show(Context context) {
		initSDK(context);
		super.show(context, null);
	}

	private void initSDK(Context context) {
		// 初始化sharesdk,具体集成步骤请看文档：
		// http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
		ShareSDK.initSDK(context);

	}

	@Override
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { platform.getName(), res };
			handler.sendMessage(msg);
		}
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_AUTH_CANCEL: {
			// 取消授权
			Tools.showMsg(activity, R.string.auth_cancel);
		}
			break;
		case MSG_AUTH_ERROR: {
			// 授权失败
			Tools.showMsg(activity, R.string.auth_error);
		}
			break;
		case MSG_AUTH_COMPLETE: {
			// 授权成功
			Tools.showMsg(activity, R.string.auth_complete);
			Object[] objs = (Object[]) msg.obj;
			String platform = (String) objs[0];
			HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
			if (signupListener != null) {
				signupListener.onSignin(platform, res);
			}
			activity.finish();
		}
			break;
		}
		return false;
	}
	
}
