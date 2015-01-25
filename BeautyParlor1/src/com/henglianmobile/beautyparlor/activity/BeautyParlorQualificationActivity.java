package com.henglianmobile.beautyparlor.activity;

import java.io.File;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.util.BitmapUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

@EActivity(R.layout.activity_beautyparlor_qualification)
public class BeautyParlorQualificationActivity extends BaseActivity {

	@ViewById
	protected EditText et_beautyparlor_name, et_beautyparlor_introduce,
			et_manager, et_phone, et_employee_count, et_address, et_technician;
	@ViewById
	protected ImageView iv_jishi_photo,iv_company_pic,iv_certificate,iv_zizhizhengshu;
	
	private String jishiPhoto,companyPic,certificate,zizhizhengshu;
	private String beautyparlorName, beautyparlorIntroduce, manager, phone,
			employeeCount, address, technician;
	private File temp;
	private Bitmap bitmapFromUri;
	private Uri fromFile;
	@Extra
	String userId;

	@AfterViews
	void initAttrs() {
		LogUtil.i("info", "userId=" + userId);
	}
	@Click
	void btn_back(){
		this.finish();
	}
	/**
	 * 添加技师照片
	 */
	@Click
	void iv_jishi_photo(){
		publishPhotoDialog(0, 1);
	}
	/**
	 * 添加公司照片
	 */
	@Click
	void iv_company_pic(){
		publishPhotoDialog(2, 3);
	}
	/**
	 * 营业执照
	 */
	@Click
	void iv_certificate(){
		publishPhotoDialog(4, 5);
	}
	/**
	 * 资质证书
	 */
	@Click
	void iv_zizhizhengshu(){
		publishPhotoDialog(6, 7);
	}
	@Click
	void tv_submit(){
		if(Check()){
			submit();
		}
	}
	private boolean Check() {
		
		beautyparlorName = et_beautyparlor_name.getText().toString().trim();
		beautyparlorIntroduce = et_beautyparlor_introduce.getText().toString().trim();
		manager = et_manager.getText().toString().trim();
		phone = et_phone.getText().toString().trim();
		employeeCount = et_employee_count.getText().toString().trim();
		address = et_address.getText().toString().trim();
		technician = et_technician.getText().toString().trim();
		if(TextUtils.isEmpty(beautyparlorName)){
			Tools.showMsg(this, "请输入您的美容院名称!");
			return false;
		}
		if(TextUtils.isEmpty(beautyparlorIntroduce)){
			Tools.showMsg(this, "请输入美容院简介!");
			return false;
		}
		if(TextUtils.isEmpty(manager)){
			Tools.showMsg(this, "请输入您的管理员名称!");
			return false;
		}
		if(TextUtils.isEmpty(phone)){
			Tools.showMsg(this, "请输入您的固定电话!");
			return false;
		}
		if(TextUtils.isEmpty(employeeCount)){
			Tools.showMsg(this, "请输入您的员工数量!");
			return false;
		}
		if(TextUtils.isEmpty(address)){
			Tools.showMsg(this, "请输入美容院的地址!");
			return false;
		}
		if(TextUtils.isEmpty(technician)){
			Tools.showMsg(this, "请输入您的技师名称!");
			return false;
		}
		if(TextUtils.isEmpty(jishiPhoto)){
			Tools.showMsg(this, "请上传技师头像!");
			return false;
		}
		if(TextUtils.isEmpty(companyPic)){
			Tools.showMsg(this, "请上传一张公司照片!");
			return false;
		}
		if(TextUtils.isEmpty(certificate)){
			Tools.showMsg(this, "请上传您的营业执照!");
			return false;
		}
		if(TextUtils.isEmpty(zizhizhengshu)){
			Tools.showMsg(this, "请上传您的资格证书!");
			return false;
		}
		
		return true;
	}
	private void submit() {
		String url = Const.BEAUTYPARLORQUALIFICATIONURL;
		RequestParams params = new RequestParams();
		params.add("userId", userId);
		params.add("shopName", beautyparlorName);
		params.add("shopContent", beautyparlorIntroduce);
		params.add("adminName", manager);
		params.add("mobileNo", phone);
		params.add("peopleNum", employeeCount);
		params.add("address", address);
		params.add("technicName", technician);
		LogUtil.i("url", "url="+url+params.toString());
		params.add("techniImg", jishiPhoto);
		params.add("companyImg", companyPic);
		params.add("licenceImg", certificate);
		params.add("certImg", zizhizhengshu);
		submitHttp(url, params);
		Tools.showProgressDialog(this, "正在提交信息，请稍后...");
	}
	private void submitHttp(String url, RequestParams params) {
		HttpUtil.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(BeautyParlorQualificationActivity.this,
						HTTP_ERROR);
				Tools.closeProgressDialog();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				LogUtil.i("hck", responseString);
				if (statusCode == 200) {
					int response = Integer.parseInt(responseString);
					if (response == 0) {
						Tools.showMsg(BeautyParlorQualificationActivity.this,
								"信息提交失败!");
						Tools.closeProgressDialog();
					} else if (response == 1) {
						Tools.showMsg(BeautyParlorQualificationActivity.this,
								"信息提交成功!");
						Tools.closeProgressDialog();
						BeautyParlorQualificationActivity.this.finish();
					}
				}
			}
		});
	}
	private void publishPhotoDialog(final int arg0,final int arg1) {
		// 创建一个对话提示框
		new AlertDialog.Builder(this).setTitle("照片")
				.setItems(Constant.ITEMS, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intentFromGallery = null;
						Intent intentFromCapture = null;

						switch (which) {
						// 从相册获取图片
						case 0:
							intentFromGallery = new Intent(Intent.ACTION_PICK,
									null);
							intentFromGallery
									.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
							BeautyParlorQualificationActivity.this
									.startActivityForResult(intentFromGallery,
											arg0);
							break;
						// 拍照存储到sd上，名字为image.jpg
						case 1:
							intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);

							if (BitmapUtil.hasSdCard()) {
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												"image.jpg")));

							}
							BeautyParlorQualificationActivity.this
									.startActivityForResult(intentFromCapture,
											arg1);
							break;

						default:
							break;
						}
					}
				}).show();
	}

//	/**
//	 * 判断SDK的状态
//	 */
//	private boolean hasSdCard() {
//		try {
//			return Environment.getExternalStorageState().equals(
//					Environment.MEDIA_MOUNTED);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}

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
				bitmapFromUri = BitmapUtil.getBitmapFromUri(this, uri,
						iv_jishi_photo);
				iv_jishi_photo.setImageBitmap(bitmapFromUri);
				jishiPhoto = Tools.encodeBase64File(bitmapFromUri);
			}
			break;
		// 从相机跳转过来的，不需要data里面的数据。只需要startActivityForResult中传入的返回码进行switch即可
		case 1:
			// 此行代码的路径是自己提前定义好的路径。
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// 转换成Uri
			fromFile = Uri.fromFile(temp);
			bitmapFromUri = BitmapUtil.getBitmapFromUri(this, fromFile,
					iv_jishi_photo);
			iv_jishi_photo.setImageBitmap(bitmapFromUri);
			jishiPhoto = Tools.encodeBase64File(bitmapFromUri);
			break;
		case 2:
			if (data != null) {
				Uri uri = data.getData();
				bitmapFromUri = BitmapUtil.getBitmapFromUri(this, uri,
						iv_company_pic);
				iv_company_pic.setImageBitmap(bitmapFromUri);
				companyPic = Tools.encodeBase64File(bitmapFromUri);
			}
			break;
			// 从相机跳转过来的，不需要data里面的数据。只需要startActivityForResult中传入的返回码进行switch即可
		case 3:
			// 此行代码的路径是自己提前定义好的路径。
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// 转换成Uri
			fromFile = Uri.fromFile(temp);
			bitmapFromUri = BitmapUtil.getBitmapFromUri(this, fromFile,
					iv_company_pic);
			iv_company_pic.setImageBitmap(bitmapFromUri);
			companyPic = Tools.encodeBase64File(bitmapFromUri);
			break;
		case 4:
			if (data != null) {
				Uri uri = data.getData();
				bitmapFromUri = BitmapUtil.getBitmapFromUri(this, uri,
						iv_certificate);
				iv_certificate.setImageBitmap(bitmapFromUri);
				certificate = Tools.encodeBase64File(bitmapFromUri);
			}
			break;
			// 从相机跳转过来的，不需要data里面的数据。只需要startActivityForResult中传入的返回码进行switch即可
		case 5:
			// 此行代码的路径是自己提前定义好的路径。
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// 转换成Uri
			fromFile = Uri.fromFile(temp);
			bitmapFromUri = BitmapUtil.getBitmapFromUri(this, fromFile,
					iv_certificate);
			iv_certificate.setImageBitmap(bitmapFromUri);
			certificate = Tools.encodeBase64File(bitmapFromUri);
			break;
		case 6:
			if (data != null) {
				Uri uri = data.getData();
				bitmapFromUri = BitmapUtil.getBitmapFromUri(this, uri,
						iv_zizhizhengshu);
				iv_zizhizhengshu.setImageBitmap(bitmapFromUri);
				zizhizhengshu = Tools.encodeBase64File(bitmapFromUri);
			}
			break;
			// 从相机跳转过来的，不需要data里面的数据。只需要startActivityForResult中传入的返回码进行switch即可
		case 7:
			// 此行代码的路径是自己提前定义好的路径。
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// 转换成Uri
			fromFile = Uri.fromFile(temp);
			bitmapFromUri = BitmapUtil.getBitmapFromUri(this, fromFile,
					iv_zizhizhengshu);
			iv_zizhizhengshu.setImageBitmap(bitmapFromUri);
			zizhizhengshu = Tools.encodeBase64File(bitmapFromUri);
			break;
		default:
			break;
		}
	}
//	/**
//	 * 
//	 * @param context
//	 * @param uri
//	 * @param imageView
//	 *            通过imageView得到imageView控件的宽和高，进行缩放填充
//	 * @return
//	 */
//	public Bitmap getBitmapFromUri(Context context, Uri uri, ImageView imageView) {
//		ContentResolver cr = context.getContentResolver();
//
//		try {
//			InputStream in = cr.openInputStream(uri);
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inJustDecodeBounds = true;
//			BitmapFactory.decodeStream(in, null, options);
//			try {
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			int mWidth = options.outWidth;
//			int mHeight = options.outHeight;
//			int screenWidth = imageView.getWidth();
//			int screenHeight = imageView.getHeight();
//
//			float scale = 1;
//			if (mWidth > screenWidth || mHeight > screenHeight) {
//				if (mWidth / mHeight > screenWidth / screenHeight) {
//					scale = mWidth / (float) screenWidth;
//				} else {
//					scale = mHeight / (float) screenHeight;
//				}
//			}
//
//			options = new BitmapFactory.Options();
//			options.inSampleSize = Math.round(scale);
//
//			in = cr.openInputStream(uri);
//			Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
//
//			try {
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return bitmap;
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return null;
//	}
}
