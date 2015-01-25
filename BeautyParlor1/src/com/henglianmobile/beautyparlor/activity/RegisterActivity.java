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
 * ע��
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
		time = new TimeCount(60000, 1000);//����CountDownTimer����
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
			tv_userType.setText("����Ժ");
			popupWindow.dismiss();
			break;
		case R.id.tv_type2:
			userType = Constant.CONSUMER;
			tv_userType.setText("������");
			popupWindow.dismiss();
			break;
		case R.id.tv_type3:
			userType = Constant.SALEMAN;
			tv_userType.setText("ҵ��Ա");
			popupWindow.dismiss();
			break;

		default:
			break;
		}
	}
	//��ȡ��֤��
	private void getCode() {
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Tools.showMsg(this, "�������ֻ���!");
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
	 * ���зǿ���֤
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
			Tools.showMsg(this, "�������ǳ�!");
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			Tools.showMsg(this, "����������!");
			return false;
		}
		if (TextUtils.isEmpty(phone)) {
			Tools.showMsg(this, "�������ֻ���!");
			return false;
		}
		if (TextUtils.isEmpty(checkCode)) {
			Tools.showMsg(this, "��������֤��!");
			return false;
		}
		if (!checkCode.equals(code)) {
			Tools.showMsg(this, "���������֤�벻��ȷ������������!");
			return false;
		}
		if (TextUtils.isEmpty(userType)) {
			Tools.showMsg(this, "��ѡ���û�����!");
			return false;
		}
		return true;
	}

	/**
	 * �ύע��
	 */
	private void submit() {
		String url = Const.REGISTERURL + "uname=" + nick + "&upass=" + password
				+ "&phone="+phone+"&types="+userType;
		sendGetHttp(url);
	}
	/**
	 * ����get����
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
							Tools.showMsg(RegisterActivity.this, "ע��ʧ�ܣ����Ժ�����ע��!");
						}else if(response == -2){
							Tools.showMsg(RegisterActivity.this, "���ֻ����Ѿ�ע�ᣬ������ֻ���!");
						}else if(response > 0){
							Tools.showMsg(RegisterActivity.this, "ע��ɹ�!");
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
	 * �Զ���popupwindow   ,,;
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
	        
	        // ����SelectPicPopupWindow��View  
	        this.setContentView(conentView);  
	        // ����SelectPicPopupWindow��������Ŀ�  
	        this.setWidth(LayoutParams.MATCH_PARENT);  
	        // ����SelectPicPopupWindow��������ĸ�  
	        this.setHeight(LayoutParams.WRAP_CONTENT);  
	        // ����SelectPicPopupWindow��������ɵ��  
	        this.setFocusable(true);  
	        this.setOutsideTouchable(true);  
	        // ˢ��״̬  
	        this.update();  
	        // ʵ����һ��ColorDrawable��ɫΪ��͸��  
	        ColorDrawable dw = new ColorDrawable(0000000000);  
	        // ��back���������ط�ʹ����ʧ,������������ܴ���OnDismisslistener �����������ؼ��仯�Ȳ���  
	        this.setBackgroundDrawable(dw);  
	        this.setAnimationStyle(R.style.popwin_anim_style);  
		}
		/** 
	     * ��ʾpopupWindow 
	     *  
	     * @param parent 
	     */  
	    public void showPopupWindow(View parent) {  
	        if (!this.isShowing()) {  
	            // ��������ʽ��ʾpopupwindow  
	            this.showAsDropDown(parent);  
	        } else {  
	            this.dismiss();  
	        }  
	    }
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
						//��¼�ɹ�
						TApplication.getInstance().user = loginResultObject;
						SPUtil.getUtil(RegisterActivity.this).saveToSp(SPUtil.USERNAME, phone);
						SPUtil.getUtil(RegisterActivity.this).saveToSp(SPUtil.PWD, et_password.getText().toString());
						MainActivity_.intent(RegisterActivity.this).start();
					}else if("0".equals(loginResultObject.getResponse())){
						Tools.showMsg(RegisterActivity.this, "�û���������!");
					}else if("2".equals(loginResultObject.getResponse())){
						Tools.showMsg(RegisterActivity.this, "�����������������!");
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
