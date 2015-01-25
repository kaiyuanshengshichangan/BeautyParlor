package com.henglianmobile.beautyparlor.activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.widget.EditText;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MD5Util;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
@EActivity(R.layout.activity_change_password)
public class ResetPasswordActivity extends BaseActivity {
	@ViewById
	protected EditText et_password,et_confirm_password;
	@Extra
	protected String phone;

	@Click
	void btn_back(){
		this.finish();
	}
	@Click
	void btn_submit(){
		submit();
	}

	private void submit() {
		String pwd = et_password.getText().toString().trim();
		String confirmPwd = et_confirm_password.getText().toString().trim();
		pwd = MD5Util.generatePassword(pwd);
		confirmPwd = MD5Util.generatePassword(confirmPwd);
		if(TextUtils.isEmpty(pwd)){
			Tools.showMsg(this, "��������������!");
			return;
		}
		if(TextUtils.isEmpty(confirmPwd)){
			Tools.showMsg(this, "��������ȷ������!");
			return;
		}
		if(!pwd.equals(confirmPwd)){
			Tools.showMsg(this, "��������������벻һ��������������!");
			return;
		}
		String url = Const.FORGETPASSWORDURL+"mobileNo="+phone+"&newPsw="+pwd;
		submitHttp(url);
	}

	private void submitHttp(String url) {
		LogUtil.i("url", "ResetPasswordActivity---url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", "ResetPasswordActivity===="+responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = Integer.parseInt(jsonObject.getString("response"));
						if(response == 0){
							Tools.showMsg(ResetPasswordActivity.this, "�޸�ʧ��!");
						}else if(response == 1){
							Tools.showMsg(ResetPasswordActivity.this, "�޸ĳɹ�!");
							ResetPasswordActivity.this.finish();
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
				Tools.showMsg(ResetPasswordActivity.this, HTTP_ERROR);
			}
		});
	}

}
