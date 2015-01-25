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
	 * ��Ӽ�ʦ��Ƭ
	 */
	@Click
	void iv_jishi_photo(){
		publishPhotoDialog(0, 1);
	}
	/**
	 * ��ӹ�˾��Ƭ
	 */
	@Click
	void iv_company_pic(){
		publishPhotoDialog(2, 3);
	}
	/**
	 * Ӫҵִ��
	 */
	@Click
	void iv_certificate(){
		publishPhotoDialog(4, 5);
	}
	/**
	 * ����֤��
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
			Tools.showMsg(this, "��������������Ժ����!");
			return false;
		}
		if(TextUtils.isEmpty(beautyparlorIntroduce)){
			Tools.showMsg(this, "����������Ժ���!");
			return false;
		}
		if(TextUtils.isEmpty(manager)){
			Tools.showMsg(this, "���������Ĺ���Ա����!");
			return false;
		}
		if(TextUtils.isEmpty(phone)){
			Tools.showMsg(this, "���������Ĺ̶��绰!");
			return false;
		}
		if(TextUtils.isEmpty(employeeCount)){
			Tools.showMsg(this, "����������Ա������!");
			return false;
		}
		if(TextUtils.isEmpty(address)){
			Tools.showMsg(this, "����������Ժ�ĵ�ַ!");
			return false;
		}
		if(TextUtils.isEmpty(technician)){
			Tools.showMsg(this, "���������ļ�ʦ����!");
			return false;
		}
		if(TextUtils.isEmpty(jishiPhoto)){
			Tools.showMsg(this, "���ϴ���ʦͷ��!");
			return false;
		}
		if(TextUtils.isEmpty(companyPic)){
			Tools.showMsg(this, "���ϴ�һ�Ź�˾��Ƭ!");
			return false;
		}
		if(TextUtils.isEmpty(certificate)){
			Tools.showMsg(this, "���ϴ�����Ӫҵִ��!");
			return false;
		}
		if(TextUtils.isEmpty(zizhizhengshu)){
			Tools.showMsg(this, "���ϴ������ʸ�֤��!");
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
		Tools.showProgressDialog(this, "�����ύ��Ϣ�����Ժ�...");
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
								"��Ϣ�ύʧ��!");
						Tools.closeProgressDialog();
					} else if (response == 1) {
						Tools.showMsg(BeautyParlorQualificationActivity.this,
								"��Ϣ�ύ�ɹ�!");
						Tools.closeProgressDialog();
						BeautyParlorQualificationActivity.this.finish();
					}
				}
			}
		});
	}
	private void publishPhotoDialog(final int arg0,final int arg1) {
		// ����һ���Ի���ʾ��
		new AlertDialog.Builder(this).setTitle("��Ƭ")
				.setItems(Constant.ITEMS, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intentFromGallery = null;
						Intent intentFromCapture = null;

						switch (which) {
						// ������ȡͼƬ
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
						// ���մ洢��sd�ϣ�����Ϊimage.jpg
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
//	 * �ж�SDK��״̬
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
		// �����·��ؼ���ʱ��resultCodeΪ0
		if (resultCode == 0) {
			return;
		}
		switch (requestCode) {
		// �������ת������
		case 0:
			if (data != null) {
				Uri uri = data.getData();
				bitmapFromUri = BitmapUtil.getBitmapFromUri(this, uri,
						iv_jishi_photo);
				iv_jishi_photo.setImageBitmap(bitmapFromUri);
				jishiPhoto = Tools.encodeBase64File(bitmapFromUri);
			}
			break;
		// �������ת�����ģ�����Ҫdata��������ݡ�ֻ��ҪstartActivityForResult�д���ķ��������switch����
		case 1:
			// ���д����·�����Լ���ǰ����õ�·����
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// ת����Uri
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
			// �������ת�����ģ�����Ҫdata��������ݡ�ֻ��ҪstartActivityForResult�д���ķ��������switch����
		case 3:
			// ���д����·�����Լ���ǰ����õ�·����
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// ת����Uri
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
			// �������ת�����ģ�����Ҫdata��������ݡ�ֻ��ҪstartActivityForResult�д���ķ��������switch����
		case 5:
			// ���д����·�����Լ���ǰ����õ�·����
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// ת����Uri
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
			// �������ת�����ģ�����Ҫdata��������ݡ�ֻ��ҪstartActivityForResult�д���ķ��������switch����
		case 7:
			// ���д����·�����Լ���ǰ����õ�·����
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// ת����Uri
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
//	 *            ͨ��imageView�õ�imageView�ؼ��Ŀ�͸ߣ������������
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
