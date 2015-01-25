package com.henglianmobile.beautyparlor.ui.activity.consumer;

import java.io.File;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.doublefi123.diary.widget.CircularImage;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.henglianmobile.beautyparlor.util.UploadPhotoUtil;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * Activity-�޸ĸ�����Ϣ
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_consumer_update_personal_info)
public class UpdatePeronalInfoActivity extends BaseActivity {

	@ViewById
	protected EditText et_user_nick,et_age,et_address,et_sign,et_account_bank,et_account_number;
	@ViewById
	protected RadioGroup rg_sex;
	@ViewById
	protected RadioButton radioMan, radioFemale;
	@ViewById
	protected CircularImage iv_my_photo;
	@ViewById
	protected LinearLayout ll_account_info;
	
	private String nick,age,address,sign,accountBank="",accountNumber="";
	private int sex;
	@Extra
	protected int saleman;
	@AfterViews
	protected void initView(){
		rg_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radioMan) {
					// ѡ������
					sex = 1;
				} else if (checkedId == R.id.radioFemale) {
					// ѡ����Ů
					sex = 0;
				}
			}
		});
		showData();
		if(saleman == 1){
			ll_account_info.setVisibility(View.VISIBLE);
		}
	}
	private void showData(){
		int isex = TApplication.getInstance().userInfoDetailObject.getSex();
		if (isex == 0) {
			radioMan.setChecked(false);
			radioFemale.setChecked(true);
		} else if (isex == 1) {
			radioMan.setChecked(true);
			radioFemale.setChecked(false);
		}
		et_user_nick.setText(TApplication.getInstance().userInfoDetailObject.getDcNickName());
		et_age.setText(TApplication.getInstance().userInfoDetailObject.getAge()+"");
		et_address.setText(TApplication.getInstance().userInfoDetailObject.getAddress());
		et_sign.setText(TApplication.getInstance().userInfoDetailObject.getDcSign());
		et_account_bank.setText(TApplication.getInstance().userInfoDetailObject.getBankName());
		et_account_number.setText(TApplication.getInstance().userInfoDetailObject.getAccountNo());
		String photoPath = TApplication.getInstance().userInfoDetailObject.getDcHeadImg();
		ImageLoader.getInstance().displayImage(photoPath, iv_my_photo,
				TApplication.optionsImage, new MyAnimateFirstDisplayListener());
	}
	@Click
	protected void btn_back(){
		this.finish();
	}
	@Click
	protected void btn_submit(){
		submit();
	}
	@Click
	protected void iv_my_photo(){
		UploadPhotoUtil.publishPhotoDialog(UpdatePeronalInfoActivity.this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// �����·��ؼ���ʱ��resultCodeΪ0
		if (resultCode == 0) {
			return;
		}
		switch (requestCode) {
		// �������ת������
		case 0:
			if (data != null) {
				Uri uri = data.getData();
				UploadPhotoUtil.startPhotoZoom(uri,UpdatePeronalInfoActivity.this);
			}
			break;
		// �������ת�����ģ�����Ҫdata��������ݡ�ֻ��ҪstartActivityForResult�д���ķ��������switch����
		case 1:
			// ���д����·�����Լ���ǰ����õ�·����
			File temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// ת����Uri
			Uri fromFile = Uri.fromFile(temp);

			UploadPhotoUtil.startPhotoZoom(fromFile,UpdatePeronalInfoActivity.this);
			break;
		case 2:
			/**
			 * �ǿ��жϴ��һ��Ҫ��֤���������֤�Ļ��� �ڼ���֮��������ֲ����⣬Ҫ���²ü�������
			 * ��ǰ����ʱ���ᱨNullException��С��ֻ ������ط����£���ҿ��Ը��ݲ�ͬ����ں��ʵ� �ط����жϴ����������
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			iv_my_photo.setImageBitmap(photo);
			String picBase64Str = Tools.encodeBase64File(photo);
			UploadPhoto(picBase64Str);
		}
	}
	/**
	 * �ϴ�ͷ��
	 * @param picBase64Str
	 */
	private void UploadPhoto(String picBase64Str) {
		// String url = "http://192.168.3.8:8084/BllHandle/upuserimg.ashx?";
		String url = Const.UPLOADPHOTOURL;
		RequestParams params = new RequestParams();
		params.add("uid", TApplication.user.getUid());
		params.add("type", "1");
		params.add("headimg", picBase64Str);
		LogUtil.i("url", "url=" + url + params.toString());
		HttpUtil.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("hck", "UpdatePeronalInfoActivity---�ϴ�ͷ��" + arg2);
				Tools.showMsg(UpdatePeronalInfoActivity.this, HTTP_ERROR);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", "UpdatePeronalInfoActivity---�ϴ�ͷ��"
							+ responseString);
					int response = Integer.parseInt(responseString);
					if (response == 0) {
						Tools.showMsg(UpdatePeronalInfoActivity.this, "ͷ���ϴ�ʧ��!");
					} else if (response == 1) {
						Tools.showMsg(UpdatePeronalInfoActivity.this, "ͷ���ϴ��ɹ�!");
						//���͹㲥
						Intent intent = new Intent(Constant.UPDATEBASEINFOACTION);
						sendBroadcast(intent);
					}
				}
			}
		});
	}
	private void submit() {
		nick = et_user_nick.getText().toString();
		age = et_age.getText().toString();
		address = et_address.getText().toString();
		sign = et_sign.getText().toString();
		accountBank = et_account_bank.getText().toString();
		accountNumber = et_account_number.getText().toString();
		String url = Const.UPDATEUSERINFODETAILURL
				+ "userId=" + TApplication.user.getUid()
				+ "&headImg=" + TApplication.getInstance().userInfoDetailObject.getDcHeadImg()
				+ "&nickname=" + nick 
				+ "&sphone="+TApplication.getInstance().userInfoDetailObject.getDcCellPhone()
				+ "&sex="+sex
				+ "&age="+age
				+ "&address="+address
				+ "&signInfo=" +sign
				+ "&content="
				+ "&bankName="+accountBank
				+ "&accountNo="+accountNumber;
//		String url = Const.UPDATEUSERINFODETAILURL;
//		RequestParams params = new RequestParams();
//		params.put("userId", TApplication.user.getUid());
//		params.put("headImg", TApplication.getInstance().userInfoDetailObject.getDcHeadImg());
//		params.put("nickname", nick);
//		params.put("sphone", TApplication.getInstance().userInfoDetailObject.getDcCellPhone());
//		params.put("sex", sex);
//		params.put("age", age);
//		params.put("address", address);
//		params.put("signInfo", sign);
//		params.put("content", "");
//		params.put("sex", sex);
//		params.put("bankName", accountBank);
//		params.put("accountNo", accountNumber);
		submitHttpGet(url);
	}
	private void submitHttpGet(String url) {
		LogUtil.i("url", "UpdatePeronalInfoActivity-submitHttpGet-url=" + url);
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
							Tools.showMsg(UpdatePeronalInfoActivity.this, "�޸�ʧ��!");
						} else if (response == 1) {
							Tools.showMsg(UpdatePeronalInfoActivity.this, "�޸ĳɹ�!");
							//���͹㲥
							Intent intent = new Intent(Constant.UPDATEBASEINFOACTION);
							sendBroadcast(intent);
							UpdatePeronalInfoActivity.this.finish();
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
				Tools.showMsg(UpdatePeronalInfoActivity.this, HTTP_ERROR);
			}
		});
	}
}
