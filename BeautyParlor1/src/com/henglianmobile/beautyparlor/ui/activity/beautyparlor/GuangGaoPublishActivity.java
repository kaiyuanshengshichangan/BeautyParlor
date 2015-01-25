package com.henglianmobile.beautyparlor.ui.activity.beautyparlor;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.PopupwindowDialogStringListAdapter;
import com.henglianmobile.beautyparlor.adapter.SelectPhotosAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.GetPriceListObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.henglianmobile.beautyparlor.util.Util;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

@EActivity(R.layout.activity_beautyparlor_guanggao_publish)
public class GuangGaoPublishActivity extends BaseActivity {

	@ViewById
	protected TextView tv_guanggao_to,tv_money;
	@ViewById
	protected EditText et_content, et_title;
	@ViewById
	protected GridView gv_patient_pics;
	@ViewById
	protected RelativeLayout rl_guanggao_to;
	private String psId;

	private ArrayList<Map<String, String>> list;
	private SelectPhotosAdapter adapter;
	private ImageButton ibDel;

	private List<File> smallPicList;
	private ArrayList<String> listfile = new ArrayList<String>();
	private PublishGuangGaoReceiver receiver;
	private SelectProjectTypePopupWindow popupWindow;
	private ListView listView;
	private List<Map<String, String>> guangGaoTo = new ArrayList<Map<String, String>>();
	private String guangGaoToId;
	private float adPrice;
	private String content,title;

	// @Extra
	// protected String address;
	@Extra
	protected String jumpfromshouye;

	@AfterViews
	public void initViews() {
		// ����������
		list = new ArrayList<Map<String, String>>();
		adapter = new SelectPhotosAdapter(this, list,
				Constant.PIC_PUBLISH_GUANGGAO);
		gv_patient_pics.setAdapter(adapter);
		// ע��㲥������
		receiver = new PublishGuangGaoReceiver();
//		IntentFilter filter = new IntentFilter(
//				Constant.ACTIONBEAUTYPARLORPULISHGUANGGAOSELECTPHOTO);
		IntentFilter filter = new IntentFilter();
		String action1 = Constant.ACTIONBEAUTYPARLORPULISHGUANGGAOSELECTPHOTO;
		String action2 = Constant.ACTIONPUSHGUANGGAOSUCCESS;
		filter.addAction(action1);
		filter.addAction(action2);
		registerReceiver(receiver, filter);
		addListener();

		// ��ȡ�۸�
		String url = Const.GETPRICEURL;
		getPriceHttp(url);
		publishGuangGaoTo();
	}

	private void getPriceHttp(String url) {
		LogUtil.i("url", "GuangGaoPublishActivity--url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<GetPriceListObject>>() {
					}.getType();
					List<GetPriceListObject> priceListObjects = TApplication.gson
							.fromJson(responseString, type);
					if(priceListObjects!=null&&priceListObjects.size()!=0){
						GetPriceListObject getPriceListObject = priceListObjects.get(0);
						adPrice = getPriceListObject.getAdPrice();
						tv_money.setText(adPrice+"");
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(GuangGaoPublishActivity.this,
						Tools.HTTP_ERROR);
			}
		});
	}

	/**
	 * ������浽--
	 * 
	 * @param url
	 */
	private void publishGuangGaoTo() {
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "1");
		map1.put("type", "��Ѷ");
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "2");
		map2.put("type", "����Ȧ");
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("id", "3");
		map3.put("type", "ȫ��");
		guangGaoTo.add(map1);
		guangGaoTo.add(map2);
		guangGaoTo.add(map3);
	}

	private void addListener() {
		gv_patient_pics.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				ibDel = (ImageButton) view.findViewById(R.id.ib_del);
				LogUtil.i("info", "ibDel");
				if (ibDel.getVisibility() == View.GONE) {
					LogUtil.i("info", "ibDel=" + ibDel.getVisibility());
					ibDel.setVisibility(View.VISIBLE);
				} else {
					ibDel.setVisibility(View.GONE);
					LogUtil.i("info", "ibDel====" + ibDel.getVisibility());
				}
				ibDel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						list.remove(position);
						adapter.notifyDataSetChanged();
						ibDel.setVisibility(View.GONE);
						TApplication.picsCount = list.size();
					}
				});
			}
		});
	}

	@Click
	void btn_back() {
		if ("1".equals(jumpfromshouye)) {
			// �����㲥
			Intent intent = new Intent(Constant.SHOUYETOOTHERACTIVITY);
			sendBroadcast(intent);
		}
		this.finish();
	}

	@Click
	protected void rl_guanggao_to() {
		popupWindow = new SelectProjectTypePopupWindow();
		popupWindow.showPopupWindow(rl_guanggao_to);
	}


	/**
	 * ȡ��
	 */
	@Click
	protected void tv_cancel() {
		this.finish();
	}
	/**
	 * ֧��������
	 */
	@Click
	protected void tv_pay_and_publish() {
//		publish();
		if(check()){
			PayTypeSelectActivity_.intent(this)
			.extra("expenseType", Constant.PUBLISHGUANGGAO)
			.extra("price", adPrice).start();
		}
	}
	/**
	 * Ԥ��
	 */
	@Click
	protected void tv_preview() {
		content = et_content.getText().toString().trim();
		title = et_title.getText().toString().trim();
		ArrayList<String> pics = new ArrayList<String>();
		for(int i = 0;i < list.size();i ++){
			Map<String, String> map = list.get(i);
			pics.add(map.get("path"));
		}
		LogUtil.i("info", "content11="+content);
		ProposalDetailPreviewActivity_.intent(this)
		.extra("content", content)
		.extra("title", title)
		.extra("tag", 1)
		.stringArrayListExtra("pics", pics).start();
	}
	private void publish(int payId) {
		if(check()){
			String url = Const.PUBLISHGUANGGAO 
					+ "uid=" + TApplication.user.getUid() 
					+ "&titles=" + title 
					+ "&content=" + content 
					+ "&stype=" + guangGaoToId 
					+ "&site=" + TApplication.getInstance().userInfoDetailObject.getAddress()
					+ "&price=" + adPrice
					+ "&recordId=" + payId;
			publishHttpGet(url);
		}
	}

	private boolean check() {
		title = et_title.getText().toString().trim();
		content = et_content.getText().toString().trim();
		if (TextUtils.isEmpty(title)) {
			Tools.showMsg(this, "�����������!");
			return false;
		}
		if (TextUtils.isEmpty(content)) {
			Tools.showMsg(this, "����������!");
			return false;
		}
		if (TextUtils.isEmpty(guangGaoToId)) {
			Tools.showMsg(this, "���ѹ�淢�������أ�");
			return false;
		}
		return true;
	}

	private void publishHttpGet(String url) {
		LogUtil.i("url", "GuangGaoPublishActivity--url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						// int response = Integer.parseInt(jsonObject
						// .getString("response"));
						int response = jsonObject.getInt("response");
						if (response == 0) {
							Tools.showMsg(GuangGaoPublishActivity.this,
									"��������ʧ��!");
						} else if (response > 0) {

							psId = response + "";
							LogUtil.i("info", "psId=" + psId);
							if (list.size() != 0) {
								// �ϴ�ͼƬ
								Tools.showProgressDialog(
										GuangGaoPublishActivity.this,
										"�����ϴ�ͼƬ�����Ժ�...");
								publishPic();
							} else {
								GuangGaoPublishActivity.this.finish();
							}
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
				Tools.showMsg(GuangGaoPublishActivity.this, HTTP_ERROR);
			}
		});
	}

	private void publishPic() {
		new Thread() {
			public void run() {
				// ��ʼѹ��
				if (compressPhoto()) {
					// ѹ���ɹ�,��ʼ�ϴ�
					mHandler.sendEmptyMessage(MESSAGE_COMPRESS_SUCCESS);
				} else {
					mHandler.sendEmptyMessage(MESSAGE_COMPRESS_FAILD);
				}
			}
		}.start();
	}

	/**
	 * ѹ��ͼƬ����Ҫ�첽ִ��
	 * 
	 * @return
	 */
	public boolean compressPhoto() {
		if (list != null) {
			smallPicList = new ArrayList<File>();
			// ѹ��ͼƬ
			for (Map<String, String> map : list) {
				String path = map.get("path");
				File photoFile = new File(path);
				if (photoFile.exists()) {
					File newFile = new File(getCacheDir(), photoFile.getName());
					if (saveBitmapToFile(Util.getimage(path, 800, 800), newFile)) {
						smallPicList.add(newFile);
					} else {
						LogUtil.i("info", "photo compress failed,path is '"
								+ path + "'");
						return false;
					}
				} else {
					LogUtil.i("info", "photo is not exist,path is '" + path
							+ "'");
					return false;
				}
			}
		}
		return true;
	}

	//
	/**
	 * ����Bitmap���ļ�
	 * 
	 * @param bm
	 * @param newFile
	 * @return
	 */
	public boolean saveBitmapToFile(Bitmap bm, File newFile) {
		if (newFile == null) {
			return false;
		}
		if (newFile.exists()) {
			newFile.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(newFile);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			LogUtil.i("info",
					"photo save success,path is '" + newFile.getAbsolutePath()
							+ "'");
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.i("info",
					"photo save failed,path is '" + newFile.getAbsolutePath()
							+ "'");
			return false;
		}
		return true;
	}

	/**
	 * �ϴ�ͼƬ
	 * 
	 * @param position
	 */
	private void uploadPhoto(int position) {
		File picFile = smallPicList.get(position);
		if (picFile == null || !picFile.exists()) {
			return;
		}
		// ��ʼ�ϴ�
		String url = Const.UPLOADPICTUREURL;
		RequestParams params = new RequestParams();
		try {
			params.put("oid", psId);
			params.put("types", Constant.GUANGAO);
			if (position == 0) {
				// ��Ϊ����
				params.put("covers", 1);
			}
			String picContent = Tools.encodeBase64File(picFile);
			params.put("headimg", picContent);
			HttpUtil.post(url, params, new TextHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] arg1,
						String responseString) {
					if (statusCode == 200) {
						LogUtil.i("hck", responseString);
						int response = Integer.parseInt(responseString);
						if (response == 0) {
							mHandler.sendEmptyMessage(MESSAGE_UPLOAD_FAILD);
						} else if (response == 1) {
							Message message = new Message();
							message.what = MESSAGE_UPLOADING;
							currentUploadPos = currentUploadPos + 1;
							mHandler.sendMessage(message);
						}
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub

					mHandler.sendEmptyMessage(MESSAGE_UPLOAD_FAILD);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 * 
		 * ��Ϊ���ַ�ʽ���õ���startActivityForResult������
		 * 
		 * �������ִ����󶼻�ִ��onActivityResult������ ����Ϊ�����𵽵�ѡ�����Ǹ���ʽ��ȡͼƬҪ�����жϣ�
		 * 
		 * �����requestCode��startActivityForResult����ڶ���������Ӧ
		 */

		LogUtil.i("info", "----//------------=" + requestCode + "------------"
				+ resultCode);
		if (requestCode == Constant.REQUEST_CAMERA && resultCode == RESULT_OK) {
			HashMap<String, String> map = new HashMap<String, String>();
			File tempOutFile = SelectPhotosAdapter.tempOutFile;
			map.put("image_id", "");
			map.put("path", tempOutFile.getAbsolutePath());
			list.add(map);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(receiver);
	}

	/**
	 * �Զ���㲥������
	 * 
	 * @author Administrator
	 * 
	 */

	private class PublishGuangGaoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(Constant.ACTIONBEAUTYPARLORPULISHGUANGGAOSELECTPHOTO)){
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					if (bundle.getStringArrayList("files") != null) {
						listfile = bundle.getStringArrayList("files");
						for (int i = 0; i < listfile.size(); i++) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("image_id", "");
							map.put("path", listfile.get(i));
							list.add(map);
						}
						TApplication.picsCount = list.size();
						adapter.notifyDataSetChanged();
					}
				}
			}else if(action.equals(Constant.ACTIONPUSHGUANGGAOSUCCESS)){
				int payId = intent.getIntExtra("payId", 0);
				LogUtil.i("info", "payId = "+payId);
				publish(payId);
			}
		}

	}

	private final int MESSAGE_COMPRESS_SUCCESS = 100;
	private final int MESSAGE_COMPRESS_FAILD = 99;
	private final int MESSAGE_UPLOADING = 201;
	private final int MESSAGE_UPLOAD_SUCCESS = 200;
	private final int MESSAGE_UPLOAD_FAILD = 199;
	private final int MESSAGE_POST_SUCCESS = 300;
	private final int MESSAGE_POST_FAILD = 299;
	private static int currentUploadPos = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_COMPRESS_SUCCESS:
				currentUploadPos = 0;
				uploadPhoto(currentUploadPos);
				break;
			case MESSAGE_UPLOADING:
				if (currentUploadPos + 1 > list.size()) {
					mHandler.sendEmptyMessage(MESSAGE_POST_SUCCESS);
				} else {
					uploadPhoto(currentUploadPos);
				}
				break;
			case MESSAGE_POST_SUCCESS:
				Tools.closeProgressDialog();
				Tools.showMsg(GuangGaoPublishActivity.this, "�����ɹ�");
				if ("1".equals(jumpfromshouye)) {
					// �����㲥
					Intent intent = new Intent(Constant.SHOUYETOOTHERACTIVITY);
					sendBroadcast(intent);
				}
				GuangGaoPublishActivity.this.finish();
				break;
			case MESSAGE_UPLOAD_FAILD:
				Tools.closeProgressDialog();
				Tools.showMsg(GuangGaoPublishActivity.this, "�ϴ�ʧ��");
				break;
			default:
				break;
			}
		}
	};

	/**
	 * �Զ���popupwindow ,,;
	 * 
	 * @author Administrator
	 * 
	 */
	class SelectProjectTypePopupWindow extends PopupWindow {
		public SelectProjectTypePopupWindow() {
			LayoutInflater inflater = (LayoutInflater) GuangGaoPublishActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View conentView = inflater.inflate(
					R.layout.popupwindow_dialog_usual, null);
			// �ҵ����ֵĿؼ�
			listView = (ListView) conentView.findViewById(R.id.lv_dialog);
			// tv_doctor = (TextView) conentView.findViewById(R.id.tv_type1);
			// tv_patient = (TextView) conentView.findViewById(R.id.tv_type2);
			// tv_saleman = (TextView) conentView.findViewById(R.id.tv_type3);
			// tv_doctor.setOnClickListener(RegisterActivity.this);
			// tv_patient.setOnClickListener(RegisterActivity.this);
			// tv_saleman.setOnClickListener(RegisterActivity.this);
			PopupwindowDialogStringListAdapter adapter = new PopupwindowDialogStringListAdapter(
					GuangGaoPublishActivity.this, guangGaoTo);
			adapter.setCallback(new PopupwindowDialogStringListAdapter.OnItemClickCallback() {

				@Override
				public void onClick(int position, boolean isSelect) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(),
					// positions.get(position), Toast.LENGTH_SHORT).show();
					tv_guanggao_to
							.setText(guangGaoTo.get(position).get("type"));
					guangGaoToId = guangGaoTo.get(position).get("id");
					popupWindow.dismiss();
				}
			});
			listView.setAdapter(adapter);
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
}
