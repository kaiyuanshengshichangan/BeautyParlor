package com.henglianmobile.beautyparlor.activity;

import java.lang.reflect.Type;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.GetCodeObject;
import com.henglianmobile.beautyparlor.entity.LoginResultObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MD5Util;
import com.henglianmobile.beautyparlor.util.SPUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 注册
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements OnClickListener {
	@ViewById
	protected EditText et_nick, et_phone, et_password,et_code;
	@ViewById
	protected TextView tv_userType,tv_getcode;
	
	private TextView tv_doctor,tv_patient,tv_saleman;
	private String userType, nick, phone, password, checkCode,code;
	private String userId;
	
	private SelectUserTypePopupWindow popupWindow;
	private TimeCount time;

	@AfterViews
	void initAttrs(){
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
	}
	@Click
	void btn_register_back(){
		this.finish();
	}
	@Click
	void rl_userType(){
		popupWindow = new SelectUserTypePopupWindow();
		popupWindow.showPopupWindow(tv_userType);
	}
	@Click
	void tv_getcode(){
		getCode();
	}
	@Click
	void btn_submit(){
		if (check()) {
			submit();
		}
//		BeautyParlorQualificationActivity_.intent(RegisterActivity.this).userId("5").start();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_type1:
			userType = Constant.BEAUTYPARLOR;
			tv_userType.setText("美容院");
			popupWindow.dismiss();
			break;
		case R.id.tv_type2:
			userType = Constant.CONSUMER;
			tv_userType.setText("消费者");
			popupWindow.dismiss();
			break;
		case R.id.tv_type3:
			userType = Constant.SALEMAN;
			tv_userType.setText("业务员");
			popupWindow.dismiss();
			break;

		default:
			break;
		}
	}
	//获取验证码
	private void getCode() {
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Tools.showMsg(this, "请输入手机号!");
			return;
		}
		time.start();
		String url = Const.GETCODEURL+phone;
		getCodeHttp(url);
	}

	private void getCodeHttp(String url) {
		LogUtil.i("url", "RegisterActivity-getCodeHttp--url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<GetCodeObject>() {
					}.getType();
					GetCodeObject getCodeObject = TApplication.gson.fromJson(
							responseString, type);
					if(getCodeObject!=null&&getCodeObject.getResponse() == 1){
						code = getCodeObject.getSendcode()+"";
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(RegisterActivity.this, HTTP_ERROR);
			}
		});
	}

	/**
	 * 进行非空验证
	 * 
	 * @return
	 */
	private boolean check() {
		nick = et_nick.getText().toString().trim();
		password = MD5Util.generatePassword(et_password.getText().toString()
				.trim());
		checkCode = et_code.getText().toString().trim();
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(nick)) {
			Tools.showMsg(this, "请输入昵称!");
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			Tools.showMsg(this, "请输入密码!");
			return false;
		}
		if (TextUtils.isEmpty(phone)) {
			Tools.showMsg(this, "请输入手机号!");
			return false;
		}
		if (TextUtils.isEmpty(checkCode)) {
			Tools.showMsg(this, "请输入验证码!");
			return false;
		}
		if (!checkCode.equals(code)) {
			Tools.showMsg(this, "您输入的验证码不正确，请重新输入!");
			return false;
		}
		if (TextUtils.isEmpty(userType)) {
			Tools.showMsg(this, "请选择用户类型!");
			return false;
		}
		return true;
	}

	/**
	 * 提交注册
	 */
	private void submit() {
		String url = Const.REGISTERURL + "uname=" + nick + "&upass=" + password
				+ "&phone="+phone+"&types="+userType;
		sendGetHttp(url);
	}
	/**
	 * 发送get请求
	 * @param url
	 */
	private void sendGetHttp(String url) {
		LogUtil.i("url", "RegisterActivity---url="+url);
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
							Tools.showMsg(RegisterActivity.this, "注册失败，请稍后重新注册!");
						}else if(response == -2){
							Tools.showMsg(RegisterActivity.this, "该手机号已经注册，请更换手机号!");
						}else if(response > 0){
							Tools.showMsg(RegisterActivity.this, "注册成功!");
							userId = response+"";
							LogUtil.i("userId", "userId="+userId);
							if(userType.equals(Constant.BEAUTYPARLOR)){
								BeautyParlorQualificationActivity_.intent(RegisterActivity.this).userId(userId).start();
							}else{
								logon();
							}
							RegisterActivity.this.finish();
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
				Tools.showMsg(RegisterActivity.this, HTTP_ERROR);
			}
		});
	}
	/**
	 * 自定义popupwindow   ,,;
	 * @author Administrator
	 *
	 */
	class SelectUserTypePopupWindow extends PopupWindow{
		public SelectUserTypePopupWindow() {
			LayoutInflater inflater = (LayoutInflater) RegisterActivity.this  
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	        View conentView = inflater.inflate(R.layout.popupwindow_dialog, null);  
	        tv_doctor = (TextView) conentView.findViewById(R.id.tv_type1);
	        tv_patient = (TextView) conentView.findViewById(R.id.tv_type2);
	        tv_saleman = (TextView) conentView.findViewById(R.id.tv_type3);
	        tv_doctor.setOnClickListener(RegisterActivity.this);
	        tv_patient.setOnClickListener(RegisterActivity.this);
	        tv_saleman.setOnClickListener(RegisterActivity.this);
	        
	        // 设置SelectPicPopupWindow的View  
	        this.setContentView(conentView);  
	        // 设置SelectPicPopupWindow弹出窗体的宽  
	        this.setWidth(LayoutParams.MATCH_PARENT);  
	        // 设置SelectPicPopupWindow弹出窗体的高  
	        this.setHeight(LayoutParams.WRAP_CONTENT);  
	        // 设置SelectPicPopupWindow弹出窗体可点击  
	        this.setFocusable(true);  
	        this.setOutsideTouchable(true);  
	        // 刷新状态  
	        this.update();  
	        // 实例化一个ColorDrawable颜色为半透明  
	        ColorDrawable dw = new ColorDrawable(0000000000);  
	        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
	        this.setBackgroundDrawable(dw);  
	        this.setAnimationStyle(R.style.popwin_anim_style);  
		}
		/** 
	     * 显示popupWindow 
	     *  
	     * @param parent 
	     */  
	    public void showPopupWindow(View parent) {  
	        if (!this.isShowing()) {  
	            // 以下拉方式显示popupwindow  
	            this.showAsDropDown(parent);  
	        } else {  
	            this.dismiss();  
	        }  
	    }
	}
	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			tv_getcode.setText("重新验证");
			tv_getcode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			tv_getcode.setClickable(false);
			tv_getcode.setText(millisUntilFinished / 1000 + "秒");
		}
	}
	
	private void logon() {
		String url = Const.LOGONURL+"uname="+phone+"&upass="+password;
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
						//登录成功
						TApplication.getInstance().user = loginResultObject;
						SPUtil.getUtil(RegisterActivity.this).saveToSp(SPUtil.USERNAME, phone);
						SPUtil.getUtil(RegisterActivity.this).saveToSp(SPUtil.PWD, et_password.getText().toString());
						MainActivity_.intent(RegisterActivity.this).start();
					}else if("0".equals(loginResultObject.getResponse())){
						Tools.showMsg(RegisterActivity.this, "用户名不存在!");
					}else if("2".equals(loginResultObject.getResponse())){
						Tools.showMsg(RegisterActivity.this, "密码错误，请重新输入!");
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(RegisterActivity.this, HTTP_ERROR);
			}
		});
	}
}
