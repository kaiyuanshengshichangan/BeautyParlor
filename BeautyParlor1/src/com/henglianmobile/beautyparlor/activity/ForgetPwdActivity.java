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
 * ��������
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
		time = new TimeCount(60000, 1000);//����CountDownTimer����
	}

	@Click
	void btn_back(){
		this.finish();
	}
	@Click
	void tv_getcode(){
		phone = et_phone_number.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Tools.showMsg(this, "�������ֻ���!");
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
			Tools.showMsg(this, "�������ֻ���!");
			return;
		}
//		if(TextUtils.isEmpty(checkCode)){
//			Tools.showMsg(this, "����������֤��!");
//			return;
//		}
//		if (!checkCode.equals(code)) {
//			Tools.showMsg(this, "���������֤�벻��ȷ������������!");
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
	/* ����һ������ʱ���ڲ��� */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		}

		@Override
		public void onFinish() {// ��ʱ���ʱ����
			tv_getcode.setText("������֤");
			tv_getcode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// ��ʱ������ʾ
			tv_getcode.setClickable(false);
			tv_getcode.setText(millisUntilFinished / 1000 + "��");
		}
	}
}
