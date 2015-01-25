package com.henglianmobile.beautyparlor.ui.fragment;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.adapter.MeiYouQuanListAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.MeiYouQuanListObject;
import com.henglianmobile.beautyparlor.ui.activity.MeiYouQuanDetailActivity;
import com.henglianmobile.beautyparlor.ui.activity.PublishMeiYouQuanActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.BitmapUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

@EFragment(R.layout.fragment_meiyouquan)
public class MeiYouQuanFragment extends Fragment {

	@ViewById
	protected PullToRefreshListView lv_meiyouquan_list;
	@ViewById
	protected Button btn_back;
 
	private ListView lv;
	private View headerVeiw;
	private ImageView iv_meiyouquan_header_view, iv_user_photo;
	private TextView tv_user_nick;
	private MeiYouQuanListAdapter adapter;
	private List<MeiYouQuanListObject> lists = new ArrayList<MeiYouQuanListObject>();
	private int curPage = 1;
  
	private boolean notAddHeadView = true;
	private String userId = "0";
	private Dialog dialog; 
	 
	private String picBase64Str;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
	}  
	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i("info", "MeiYouQuanFragment---onDestroy=");
		lists.clear();
		adapter = null;
		// lv.removeHeaderView(headerVeiw);
		lv = null;
		notAddHeadView = true;
	}

	@AfterViews
	void setView() {
		if (notAddHeadView) {
			userId = "0";
			headerVeiw = LayoutInflater.from(this.getActivity()).inflate(
					R.layout.fragment_meiyouquan_list_header_view, null);
			iv_meiyouquan_header_view = (ImageView) headerVeiw
					.findViewById(R.id.iv_meiyouquan_header_view);
			iv_user_photo = (ImageView) headerVeiw
					.findViewById(R.id.iv_user_photo);
			tv_user_nick = (TextView) headerVeiw
					.findViewById(R.id.tv_user_nick);
			iv_meiyouquan_header_view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog = new Dialog(MeiYouQuanFragment.this.getActivity(), R.style.dialog);
					dialog.setContentView(R.layout.dialog_choose_background_pic);
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();
					Window dialogWindow = dialog.getWindow();
					TextView tv_change = (TextView) dialogWindow
							.findViewById(R.id.tv_change);
					tv_change.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							publishPhotoDialog(0, 1);
							dialog.cancel();
						}
					});
				}
			});
			iv_user_photo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					btn_back.setVisibility(View.VISIBLE);
					userId = TApplication.user.getUid();
					lists.clear();
					curPage = 1;
					getData();
				}
			});
			lv_meiyouquan_list.setMode(Mode.BOTH);
			lv_meiyouquan_list
					.setOnRefreshListener(new OnRefreshListener2<ListView>() {

						@Override
						public void onPullDownToRefresh(
								PullToRefreshBase<ListView> refreshView) {
							lv_meiyouquan_list.setMode(Mode.BOTH);
							lists.clear();
							curPage = 1;
							getData();
						}

						@Override
						public void onPullUpToRefresh(
								PullToRefreshBase<ListView> refreshView) {
							// 刷新完成
							lv_meiyouquan_list.setMode(Mode.BOTH);
							curPage++;
							getData();
						}
					});
			lv = lv_meiyouquan_list.getRefreshableView();

			lv.addHeaderView(headerVeiw);

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (position == 1) {
						return;
					}
					LogUtil.i("info", "position=" + position);
					MeiYouQuanListObject meiYouListObject = lists
							.get(position - 2);
					int tId = meiYouListObject.getId();
					int tType = meiYouListObject.gettType();
//					MeiYouQuanDetailActivity_
//							.intent(MeiYouQuanFragment.this.getActivity())
//							.extra("tId", tId).extra("tType", tType)
//							.start();
					Intent intent = new Intent(MeiYouQuanFragment.this.getActivity(), AnnotationClassUtil.get(MeiYouQuanDetailActivity.class));
					intent.putExtra("tId", tId);
					intent.putExtra("tType", tType);
					MeiYouQuanFragment.this.getActivity().startActivity(intent);
				}
			});
			lists.clear();
			curPage = 1;
			getData();
			notAddHeadView = false;
			
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setData();
	}
	private void setData() {
		String headImgPath = TApplication.getInstance().userInfoDetailObject
				.getDcHeadImg();
		ImageLoader.getInstance().displayImage(headImgPath, iv_user_photo,
				TApplication.optionsImage, new MyAnimateFirstDisplayListener());
		tv_user_nick.setText(TApplication.getInstance().userInfoDetailObject
				.getDcNickName());
		String backgroundPath = TApplication.getInstance().userInfoDetailObject.getDcBackImg();
		ImageLoader.getInstance().displayImage(backgroundPath, iv_meiyouquan_header_view,
				TApplication.optionsImage, new MyAnimateFirstDisplayListener());
	}

	@Click
	protected void btn_back(){
		userId = "0";
		lists.clear();
		curPage = 1;
		getData();
		btn_back.setVisibility(View.GONE);
	}
	private void getData() {
		String url = Const.GETMEIYOUQUANLISTURL + "userId=" + userId
				+ "&type=0" + "&page=" + curPage + "&rows=" + Const.PAGEROWS;
		getHttpData(url);
	}

	/**
	 * 发布美友圈跳转
	 */
	@Click
	protected void iv_publish() {
//		PublishMeiYouQuanActivity_
//				.intent(MeiYouQuanFragment.this.getActivity()).start();
		Intent intent = new Intent(MeiYouQuanFragment.this.getActivity(), AnnotationClassUtil.get(PublishMeiYouQuanActivity.class));
		MeiYouQuanFragment.this.getActivity().startActivity(intent);
	}

	private void getHttpData(String url) {
		LogUtil.i("url", "MeiYouQuanFragment---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MeiYouQuanFragment----res=" + arg2);
				if ((!TextUtils.isEmpty(arg2)) && arg2.contains("Message")) {
					try {
						JSONObject jsonObject = new JSONObject(arg2);
						String msg = jsonObject.getString("Message");
						Tools.showMsg(MeiYouQuanFragment.this.getActivity(),
								msg);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						Tools.showMsg(MeiYouQuanFragment.this.getActivity(),
								Tools.HTTP_ERROR);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				lv_meiyouquan_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MeiYouQuanFragment----res="
							+ responseString);
					Type type = new TypeToken<List<MeiYouQuanListObject>>() {
					}.getType();
					List<MeiYouQuanListObject> meiYouListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (meiYouListObjects != null) {
						for (int i = 0; i < meiYouListObjects.size(); i++) {
							lists.add(meiYouListObjects.get(i));
						}
						if (meiYouListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(
										MeiYouQuanFragment.this.getActivity(),
										Tools.LOAD_ALL);
							}
						} else {
							lv_meiyouquan_list.setMode(Mode.BOTH);
							// pageNum = pageNum + 1;
						}
						if (adapter == null) {
							adapter = new MeiYouQuanListAdapter(
									MeiYouQuanFragment.this.getActivity(),
									lists);
							lv.setAdapter(adapter);

						} else {
							adapter.changeData(lists);
						}
					}
					lv_meiyouquan_list.onRefreshComplete();
				}
			}
		});
	}
	private void publishPhotoDialog(final int arg0,final int arg1) {
		// 创建一个对话提示框
		new AlertDialog.Builder(MeiYouQuanFragment.this.getActivity()).setTitle("照片")
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
							MeiYouQuanFragment.this
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
							MeiYouQuanFragment.this
									.startActivityForResult(intentFromCapture,
											arg1);
							break;

						default:
							break;
						}
					}
				}).show();
	}
	private File temp;
	private Bitmap bitmapFromUri;
	private Uri fromFile;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 当按下返回键的时候，resultCode为0
		if (resultCode == 0) {
			return;
		}
		switch (requestCode) {
		// 从相册跳转过来的
		case 0:
			if (data != null) {
				Uri uri = data.getData();
				bitmapFromUri = BitmapUtil.getBitmapFromUri(MeiYouQuanFragment.this.getActivity(), uri,
						iv_meiyouquan_header_view);
				iv_meiyouquan_header_view.setImageBitmap(bitmapFromUri);
				picBase64Str = Tools.encodeBase64File(bitmapFromUri);
				UploadPhoto(picBase64Str);
			}
			break;
		// 从相机跳转过来的，不需要data里面的数据。只需要startActivityForResult中传入的返回码进行switch即可
		case 1:
			// 此行代码的路径是自己提前定义好的路径。
			temp = new File(Environment.getExternalStorageDirectory(),
					"/image.jpg");
			// 转换成Uri
			fromFile = Uri.fromFile(temp);
			bitmapFromUri = BitmapUtil.getBitmapFromUri(MeiYouQuanFragment.this.getActivity(), fromFile,
					iv_meiyouquan_header_view);
			iv_meiyouquan_header_view.setImageBitmap(bitmapFromUri);
			picBase64Str = Tools.encodeBase64File(bitmapFromUri);
			UploadPhoto(picBase64Str);
			break;
		default:
			break;
		}
	}
	/**
	 * 上传背景
	 * @param picBase64Str
	 */
	private void UploadPhoto(String picBase64Str) {
		Tools.showProgressDialog(getActivity(), "正在上传背景图片，请稍后...");
//		 String url = "http://192.168.3.8:8085/bllCommon/upheadimg.ashx?";
		String url = Const.UPLOADPHOTOURL;
		RequestParams params = new RequestParams();
		params.add("uid", TApplication.user.getUid());
		params.add("type", "2");
//		params.add("headimg", "");
		params.add("headimg", picBase64Str);
		LogUtil.i("url", "url=" + url + params.toString());
		HttpUtil.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("hck", "UpdatePeronalInfoActivity---上传背景=" + arg2);
				Tools.showMsg(MeiYouQuanFragment.this.getActivity(), Tools.HTTP_ERROR);
				Tools.closeProgressDialog();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", "UpdatePeronalInfoActivity-=====--上传背景"
							+ responseString);
					int response = Integer.parseInt(responseString);
					if (response == 0) {
						Tools.showMsg(MeiYouQuanFragment.this.getActivity(), "背景上传失败!");
						Tools.closeProgressDialog();
					} else if (response == 1) {
						Tools.showMsg(MeiYouQuanFragment.this.getActivity(), "背景上传成功!");
						iv_meiyouquan_header_view.setImageBitmap(bitmapFromUri);
						Intent intent = new Intent(Constant.UPDATEBASEINFOACTION);
						MeiYouQuanFragment.this.getActivity().sendBroadcast(intent);
						Tools.closeProgressDialog();
					}
				}
			}
		});
	}
}
