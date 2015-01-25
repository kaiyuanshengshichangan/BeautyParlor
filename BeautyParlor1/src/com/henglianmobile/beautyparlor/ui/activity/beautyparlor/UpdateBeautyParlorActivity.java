package com.henglianmobile.beautyparlor.ui.activity.beautyparlor;

import java.io.File;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;

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

@EActivity(R.layout.activity_beautyparlor_update_personal_info)
public class UpdateBeautyParlorActivity extends BaseActivity {

	@ViewById
	protected CircularImage iv_beautyparlor_photo;
	@ViewById
	protected EditText et_beautyparlor_name, et_beautyparlor_phone,
			et_beautyparlor_address, et_beautyparlor_introduce;
	private String beautyparlorName, beautyparlorPhone, beautyparlorAddress,
			beautyparlorIntroduce;

	@AfterViews
	protected void setData() {
		et_beautyparlor_name.setText(TApplication.getInstance().userInfoDetailObject.getDcNickName());
		et_beautyparlor_phone.setText(TApplication.getInstance().userInfoDetailObject.getDcPhone());
		et_beautyparlor_address.setText(TApplication.getInstance().userInfoDetailObject.getAddress());
		et_beautyparlor_introduce.setText(TApplication.getInstance().userInfoDetailObject.getDcContent());
		String path = TApplication.getInstance().userInfoDetailObject.getDcHeadImg();
		ImageLoader.getInstance().displayImage(path, iv_beautyparlor_photo,
				TApplication.optionsImage, new MyAnimateFirstDisplayListener());
	}

	@Click
	protected void btn_back() {
		this.finish();
	}

	@Click
	protected void btn_submit() {
		submit();
	}

	@Click
	protected void iv_beautyparlor_photo() {
		UploadPhotoUtil.publishPhotoDialog(UpdateBeautyParlorActivity.this);
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
				UploadPhotoUtil.startPhotoZoom(uri,
						UpdateBeautyParlorActivity.this);
			}
			break;
		// �������ת�����ģ�����Ҫdata��������ݡ�ֻ��ҪstartActivityForResult�д���ķ��������switch����
		case 1:
			// ���д����·�����Լ���ǰ����õ�·����
			File temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// ת����Uri
			Uri fromFile = Uri.fromFile(temp);

			UploadPhotoUtil.startPhotoZoom(fromFile,
					UpdateBeautyParlorActivity.this);
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
			iv_beautyparlor_photo.setImageBitmap(photo);
			String picBase64Str = Tools.encodeBase64File(photo);
			UploadPhoto(picBase64Str);
		}
	}

	/**
	 * �ϴ�ͷ��
	 * 
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
				LogUtil.i("hck", "UpdateBeautyParlorActivity---�ϴ�ͷ��" + arg2);
				Tools.showMsg(UpdateBeautyParlorActivity.this, HTTP_ERROR);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", "UpdateBeautyParlorActivity---�ϴ�ͷ��"
							+ responseString);
					int response = Integer.parseInt(responseString);
					if (response == 0) {
						Tools.showMsg(UpdateBeautyParlorActivity.this,
								"ͷ���ϴ�ʧ��!");
					} else if (response == 1) {
						Tools.showMsg(UpdateBeautyParlorActivity.this,
								"ͷ���ϴ��ɹ�!");
						//���͹㲥
						Intent intent = new Intent(Constant.UPDATEBASEINFOACTION);
						sendBroadcast(intent);
					}
				}
			}
		});
	}

	private void submit() {
		beautyparlorName = et_beautyparlor_name.getText().toString();
		beautyparlorPhone = et_beautyparlor_phone.getText().toString();
		beautyparlorAddress = et_beautyparlor_address.getText().toString();
		beautyparlorIntroduce = et_beautyparlor_introduce.getText().toString();
		String url = Const.UPDATEUSERINFODETAILURL 
				+ "userId=" + TApplication.user.getUid() 
				+ "&headImg=" + TApplication.getInstance().userInfoDetailObject.getDcHeadImg()
				+ "&nickname=" + beautyparlorName
				+ "&sphone=" + beautyparlorPhone 
				+ "&sex=0" + "&age=0"
				+ "&address=" + beautyparlorAddress 
				+ "&signInfo="
				+ "&content=" + beautyparlorIntroduce 
				+ "&bankName="
				+ "&accountNo=";
		submitHttpGet(url);
	}

	private void submitHttpGet(String url) {
		LogUtil.i("url", "UpdateBeautyParlorActivity-submitHttpGet-url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = jsonObject.getInt("response");
						if (response == 0) {
							Tools.showMsg(UpdateBeautyParlorActivity.this,
									"�޸�ʧ��!");
						} else if (response == 1) {
							Tools.showMsg(UpdateBeautyParlorActivity.this,
									"�޸ĳɹ�!");
							//���͹㲥
							Intent intent = new Intent(Constant.UPDATEBASEINFOACTION);
							sendBroadcast(intent);
							UpdateBeautyParlorActivity.this.finish();
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
				Tools.showMsg(UpdateBeautyParlorActivity.this, HTTP_ERROR);
			}
		});
	}
}
