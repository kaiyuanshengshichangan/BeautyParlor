package com.henglianmobile.beautyparlor.activity;

import java.lang.reflect.Type;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.GetCodeObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 忘记密码
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_forget_password)
public class ForgetPwdActivity extends BaseActivity {
	
	@ViewById
	protected EditText et_phone_number,et_code;
	@ViewById
	protected TextView tv_getcode;
	private TimeCount time;
	private String code,phone;

	@AfterViews
	void initAttrs(){
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
	}

	@Click
	void btn_back(){
		this.finish();
	}
	@Click
	void tv_getcode(){
		phone = et_phone_number.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Tools.showMsg(this, "请输入手机号!");
			return;
		}
		time.start();
		String url = Const.GETCODEURL+phone;
		getCodeHttp(url);
	}
	@Click
	void btn_submit(){
		String checkCode = et_code.getText().toString().trim();
		phone = et_phone_number.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Tools.showMsg(this, "请输入手机号!");
			return;
		}
//		if(TextUtils.isEmpty(checkCode)){
//			Tools.showMsg(this, "请您输入验证码!");
//			return;
//		}
//		if (!checkCode.equals(code)) {
//			Tools.showMsg(this, "您输入的验证码不正确，请重新输入!");
//			return;
//		}
		ResetPasswordActivity_.intent(this).phone(phone).start();
//		ResetPasswordActivity_.intent(this).extra("phone", phone).start();
		this.finish();
	}
	private void getCodeHttp(String url) {
		LogUtil.i("url", "ForgetPwdActivity-getCodeHttp--url="+url);
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
				Tools.showMsg(ForgetPwdActivity.this, HTTP_ERROR);
			}
		});
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
}
