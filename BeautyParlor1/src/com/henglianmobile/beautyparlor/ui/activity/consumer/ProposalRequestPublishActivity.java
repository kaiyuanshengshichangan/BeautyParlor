package com.henglianmobile.beautyparlor.ui.activity.consumer;

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
import com.henglianmobile.beautyparlor.adapter.PopupwindowDialogObjectListAdapter;
import com.henglianmobile.beautyparlor.adapter.SelectPhotosAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.consumer.ProjectType;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.henglianmobile.beautyparlor.util.Util;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Activity-请求方案
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_consumer_proposal_publish)
public class ProposalRequestPublishActivity extends BaseActivity {

	@ViewById
	protected TextView tv_projectType, tv_address;
	@ViewById
	protected EditText et_content;
	@ViewById
	protected GridView gv_patient_pics;
	@ViewById
	protected RelativeLayout rl_projectType;
	private String userId, uname, psId,types;

	private ArrayList<Map<String, String>> list;
	private SelectPhotosAdapter adapter;
	private ImageButton ibDel;

	private List<File> smallPicList;
	private ArrayList<String> listfile = new ArrayList<String>();
	private PublishProposalReceiver receiver;
	private SelectProjectTypePopupWindow popupWindow;
	private ListView listView;
	private List<ProjectType> projectTypes = new ArrayList<ProjectType>();

	private String address;
	@Extra
	protected String jumpfromshouye;

	@AfterViews
	public void initViews() {
		userId = TApplication.user.getUid();
		uname = TApplication.user.getNickname();
		LogUtil.i("info", "userId=" + userId + ",uname=" + uname);

		// 构建适配器
		list = new ArrayList<Map<String, String>>();
		adapter = new SelectPhotosAdapter(this, list,
				Constant.PIC_PROPOSALREQUEST);
		gv_patient_pics.setAdapter(adapter);
		// 注册广播接收器
		receiver = new PublishProposalReceiver();
		IntentFilter filter = new IntentFilter(
				Constant.ACTIONCONSUMERPROPOSALREQUESTSELECTPHOTO);
		registerReceiver(receiver, filter);
		addListener();
		// 获取分类
		String url = Const.CONSUMERGETPROJECTTYPEURL;
		getProjectType(url);
		tv_address.setText(TApplication.getInstance().userInfoDetailObject
				.getAddress());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 获取整容分类
	 * 
	 * @param url
	 */
	private void getProjectType(String url) {
		LogUtil.i("url", "ProposalRequestPublishActivity--getProjectType=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<ProjectType>>() {
					}.getType();
					projectTypes = TApplication.gson
							.fromJson(responseString, type);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(ProposalRequestPublishActivity.this, HTTP_ERROR);
			}
		});
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
			// 发布广播
			Intent intent = new Intent(Constant.SHOUYETOOTHERACTIVITY);
			sendBroadcast(intent);
		}
		this.finish();
	}

	@Click
	protected void rl_projectType() {
		popupWindow = new SelectProjectTypePopupWindow();
		popupWindow.showPopupWindow(rl_projectType);
	}

	@Click
	protected void btn_publish() {
		publish();
	}

	private void publish() {
		String content = et_content.getText().toString().trim();
		address = tv_address.getText().toString();
		if (TextUtils.isEmpty(content)) {
			Tools.showMsg(this, "请输入您的请求方案!");
			return;
		}
		if (TextUtils.isEmpty(types)) {
			Tools.showMsg(this, "请选择类型!");
			return;
		}

		String url = Const.CONSUMERPUBLISHPROPOSALREQUESTURL 
				+ "uid=" + userId
				+ "&toUserId=0"
				+ "&parentId=0"
				+ "&tit=" + "" 
				+ "&content=" + content 
				+ "&types=" + types
				+ "&price=" + "0" 
				+ "&address=" + address
				+ "&recordId=0";
		publishHttpGet(url);
	}

	private void publishHttpGet(String url) {
		LogUtil.i("url", "ProposalRequestPublishActivity--url=" + url);
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
							Tools.showMsg(ProposalRequestPublishActivity.this,
									"发布数据失败!");
						} else if (response > 0) {

							psId = response + "";
							LogUtil.i("info", "psId=" + psId);
							if (list.size() != 0) {
								// 上传图片
								Tools.showProgressDialog(
										ProposalRequestPublishActivity.this,
										"正在上传图片，请稍后...");
								publishPic();
							} else {
								ProposalRequestPublishActivity.this.finish();
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
				Tools.showMsg(ProposalRequestPublishActivity.this, HTTP_ERROR);
			}
		});
	}

	private void publishPic() {
		new Thread() {
			public void run() {
				// 开始压缩
				if (compressPhoto()) {
					// 压缩成功,开始上传
					mHandler.sendEmptyMessage(MESSAGE_COMPRESS_SUCCESS);
				} else {
					mHandler.sendEmptyMessage(MESSAGE_COMPRESS_FAILD);
				}
			}
		}.start();
	}

	/**
	 * 压缩图片，需要异步执行
	 * 
	 * @return
	 */
	public boolean compressPhoto() {
		if (list != null) {
			smallPicList = new ArrayList<File>();
			// 压缩图片
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
	 * 保存Bitmap到文件
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
	 * 上传图片
	 * 
	 * @param position
	 */
	private void uploadPhoto(int position) {
		File picFile = smallPicList.get(position);
		if (picFile == null || !picFile.exists()) {
			return;
		}
		// 开始上传
		String url = Const.UPLOADPICTUREURL;
		RequestParams params = new RequestParams();
		try {
			params.put("oid", psId);
			params.put("types", Constant.FANGAN);
			if (position == 0) {
				// 作为封面
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
		 * 因为两种方式都用到了startActivityForResult方法，
		 * 
		 * 这个方法执行完后都会执行onActivityResult方法， 所以为了区别到底选择了那个方式获取图片要进行判断，
		 * 
		 * 这里的requestCode跟startActivityForResult里面第二个参数对应
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
	 * 自定义广播接收器
	 * 
	 * @author Administrator
	 * 
	 */

	private class PublishProposalReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
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
				Tools.showMsg(ProposalRequestPublishActivity.this, "发布成功");
				if ("1".equals(jumpfromshouye)) {
					// 发布广播
					Intent intent = new Intent(Constant.SHOUYETOOTHERACTIVITY);
					sendBroadcast(intent);
				}
				ProposalRequestPublishActivity.this.finish();
				break;
			case MESSAGE_UPLOAD_FAILD:
				Tools.closeProgressDialog();
				Tools.showMsg(ProposalRequestPublishActivity.this, "上传失败");
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 自定义popupwindow ,,;
	 * 
	 * @author Administrator
	 * 
	 */
	class SelectProjectTypePopupWindow extends PopupWindow {
		public SelectProjectTypePopupWindow() {
			LayoutInflater inflater = (LayoutInflater) ProposalRequestPublishActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View conentView = inflater.inflate(
					R.layout.popupwindow_dialog_usual, null);
			// 找到布局的控件
			listView = (ListView) conentView.findViewById(R.id.lv_dialog);
			// tv_doctor = (TextView) conentView.findViewById(R.id.tv_type1);
			// tv_patient = (TextView) conentView.findViewById(R.id.tv_type2);
			// tv_saleman = (TextView) conentView.findViewById(R.id.tv_type3);
			// tv_doctor.setOnClickListener(RegisterActivity.this);
			// tv_patient.setOnClickListener(RegisterActivity.this);
			// tv_saleman.setOnClickListener(RegisterActivity.this);
			PopupwindowDialogObjectListAdapter adapter = new PopupwindowDialogObjectListAdapter(
					ProposalRequestPublishActivity.this, projectTypes);
			adapter.setCallback(new PopupwindowDialogObjectListAdapter.OnItemClickCallback() {

				@Override
				public void onClick(int position, boolean isSelect) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(),
					// positions.get(position), Toast.LENGTH_SHORT).show();
					tv_projectType
							.setText(projectTypes.get(position).getTypeName());
					types = projectTypes.get(position).getId()+"";
					popupWindow.dismiss();
				}
			});
			listView.setAdapter(adapter);
			// 设置SelectPicPopupWindow的View
			this.setContentView(conentView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.MATCH_PARENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			this.setOutsideTouchable(true);
			// 刷新状态
			this.update();
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0000000000);
			// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
			this.setBackgroundDrawable(dw);
			this.setAnimationStyle(R.style.popwin_anim_style);
		}

		/**
		 * 显示popupWindow
		 * 
		 * @param parent
		 */
		public void showPopupWindow(View parent) {
			if (!this.isShowing()) {
				// 以下拉方式显示popupwindow
				this.showAsDropDown(parent);
			} else {
				this.dismiss();
			}
		}
	}
}
