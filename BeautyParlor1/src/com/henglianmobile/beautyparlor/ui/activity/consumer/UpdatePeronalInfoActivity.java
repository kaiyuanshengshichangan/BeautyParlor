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
 * Activity-修改个人信息
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
					// 选择了男
					sex = 1;
				} else if (checkedId == R.id.radioFemale) {
					// 选择了女
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
		// 当按下返回键的时候，resultCode为0
		if (resultCode == 0) {
			return;
		}
		switch (requestCode) {
		// 从相册跳转过来的
		case 0:
			if (data != null) {
				Uri uri = data.getData();
				UploadPhotoUtil.startPhotoZoom(uri,UpdatePeronalInfoActivity.this);
			}
			break;
		// 从相机跳转过来的，不需要data里面的数据。只需要startActivityForResult中传入的返回码进行switch即可
		case 1:
			// 此行代码的路径是自己提前定义好的路径。
			File temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// 转换成Uri
			Uri fromFile = Uri.fromFile(temp);

			UploadPhotoUtil.startPhotoZoom(fromFile,UpdatePeronalInfoActivity.this);
			break;
		case 2:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
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
	 * 保存裁剪之后的图片数据
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
	 * 上传头像
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
				LogUtil.i("hck", "UpdatePeronalInfoActivity---上传头像" + arg2);
				Tools.showMsg(UpdatePeronalInfoActivity.this, HTTP_ERROR);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", "UpdatePeronalInfoActivity---上传头像"
							+ responseString);
					int response = Integer.parseInt(responseString);
					if (response == 0) {
						Tools.showMsg(UpdatePeronalInfoActivity.this, "头像上传失败!");
					} else if (response == 1) {
						Tools.showMsg(UpdatePeronalInfoActivity.this, "头像上传成功!");
						//发送广播
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
							Tools.showMsg(UpdatePeronalInfoActivity.this, "修改失败!");
						} else if (response == 1) {
							Tools.showMsg(UpdatePeronalInfoActivity.this, "修改成功!");
							//发送广播
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
