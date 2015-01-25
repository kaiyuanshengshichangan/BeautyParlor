package com.henglianmobile.beautyparlor.ui.activity.beautyparlor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.GridViewAdapter;
import com.henglianmobile.beautyparlor.adapter.MeiYouQuanPinglunAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.MeiYouQuanCommentListObject;
import com.henglianmobile.beautyparlor.entity.beautyparlor.GuangGaoDetailObject;
import com.henglianmobile.beautyparlor.ui.activity.ShowPicturesActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.ListViewUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

@EActivity(R.layout.activity_beautyparlor_my_guanggao_detail)
public class MyGuangGaoDetailActivity extends BaseActivity{

	@ViewById
	protected TextView tv_title, tv_content, tv_publish_time,
			tv_pinglun_count, tv_zan_count;
	@ViewById
	protected ImageView iv_pic, iv_pic1,
	 		iv_pic2, iv_pinglun;
	@ViewById
	protected LinearLayout ll_pics;
	@ViewById 
	protected GridView gv_pics;
	@ViewById
	protected ListView lv_pinglun; 
	 
	private List<String> pics;
	@Extra
	protected int gId,tType;
 
	@AfterViews
	protected void getData() {
		String url = Const.GETMYGUANGGAODETAILURL+gId;
		getDataHttp(url);
		String url1 = Const.GETMEIYOUQUANDETAILCOMMENTURL + gId + "&type="
				+ tType;
		getCommentHttp(url1);
	}

	private void getDataHttp(String url) {
		LogUtil.i("url", "MyGuangGaoDetailActivity--url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<GuangGaoDetailObject>>() {
					}.getType();
					List<GuangGaoDetailObject> guangGaoDetailObjects = TApplication.gson
							.fromJson(responseString, type);
					if (guangGaoDetailObjects != null
							&& guangGaoDetailObjects.size() != 0) {
						GuangGaoDetailObject guangGaoDetailObject = guangGaoDetailObjects
								.get(0);
						tv_title.setText(guangGaoDetailObject.getDcAdTitle());
						tv_content.setText(guangGaoDetailObject.getDcContent());
						tv_publish_time.setText(Tools.DateStrToDateStr(guangGaoDetailObject
								.getDtAddTime()));
						tv_zan_count.setText(guangGaoDetailObject.getLikeCount()+"");
						// 获取图片
				
						String topicPics = guangGaoDetailObject.getDcIpath();
						showPics(topicPics);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MyGuangGaoDetailActivity.this, Tools.HTTP_ERROR);
			}
		});
	}

	@Click
	protected void btn_back() {
		this.finish();
	}

	
	private void getCommentHttp(String url) {
		LogUtil.i("url", "获取评论----url==" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String responseString) {
				if (arg0 == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<MeiYouQuanCommentListObject>>() {
					}.getType();
					List<MeiYouQuanCommentListObject> meiYouQuanCommentListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (meiYouQuanCommentListObjects != null) {

						MeiYouQuanPinglunAdapter adapter = new MeiYouQuanPinglunAdapter(
								MyGuangGaoDetailActivity.this,
								meiYouQuanCommentListObjects);
						lv_pinglun.setAdapter(adapter);
						ListViewUtil
								.setListViewHeightBasedOnChildren(lv_pinglun);
						tv_pinglun_count.setText(meiYouQuanCommentListObjects
								.size() + "");
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MyGuangGaoDetailActivity.this, HTTP_ERROR);
			}
		});
	}

	private void showPics(String topicPics) {
		LogUtil.i("info", "topicPics====" + topicPics);
		pics = new ArrayList<String>();
		if (!TextUtils.isEmpty(topicPics)) {

			String[] topics = topicPics.split(",");
			for (int i = 0; i < topics.length; i++) {
				pics.add(topics[i]);
			}
			if (topics.length == 1) {
				iv_pic.setVisibility(View.VISIBLE);
				ll_pics.setVisibility(View.GONE);
				gv_pics.setVisibility(View.GONE);
				String picUrlone = topics[0];
				ImageLoader.getInstance().displayImage(picUrlone,
						iv_pic, TApplication.optionsImage,
						new MyAnimateFirstDisplayListener());

				iv_pic.setTag(pics);
				iv_pic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						ShowPicturesActivity_
//								.intent(MyGuangGaoDetailActivity.this)
//								.extra("position", 0)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(MyGuangGaoDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
						intent.putExtra("position", 0);
						intent.putStringArrayListExtra("pics",
										(ArrayList<String>) pics);
						startActivity(intent);
					}
				});

			} else if (topics.length == 2) {
				ll_pics.setVisibility(View.VISIBLE);
				iv_pic.setVisibility(View.GONE);
				gv_pics.setVisibility(View.GONE);
				ImageLoader.getInstance().displayImage(topics[0],
						iv_pic1, TApplication.optionsImage,
						new MyAnimateFirstDisplayListener());
				ImageLoader.getInstance().displayImage(topics[1],
						iv_pic2, TApplication.optionsImage,
						new MyAnimateFirstDisplayListener());

				iv_pic1.setTag(pics);
				iv_pic1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						ShowPicturesActivity_
//								.intent(MyGuangGaoDetailActivity.this)
//								.extra("position", 0)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(MyGuangGaoDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
						intent.putExtra("position", 0);
						intent.putStringArrayListExtra("pics",
										(ArrayList<String>) pics);
						startActivity(intent);
					}
				});

				iv_pic2.setTag(pics);
				iv_pic2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						ShowPicturesActivity_
//								.intent(MyGuangGaoDetailActivity.this)
//								.extra("position", 1)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(MyGuangGaoDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
						intent.putExtra("position", 1);
						intent.putStringArrayListExtra("pics",
										(ArrayList<String>) pics);
						startActivity(intent);
					}
				});

			} else if (topics.length >= 3) {
				gv_pics.setVisibility(View.VISIBLE);
				iv_pic.setVisibility(View.GONE);
				ll_pics.setVisibility(View.GONE);

				GridViewAdapter adapter = new GridViewAdapter(
						MyGuangGaoDetailActivity.this, pics);
				gv_pics.setAdapter(adapter);
				gv_pics.setTag(pics);
				gv_pics
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
//								ShowPicturesActivity_
//										.intent(MyGuangGaoDetailActivity.this)
//										.extra("position", position)
//										.stringArrayListExtra("pics",
//												(ArrayList<String>) pics)
//										.start();
								Intent intent = new Intent(MyGuangGaoDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
								intent.putExtra("position", position);
								intent.putStringArrayListExtra("pics",
												(ArrayList<String>) pics);
								startActivity(intent);
							}
						});
			}
		} else {
			gv_pics.setVisibility(View.GONE);
			iv_pic.setVisibility(View.GONE);
			ll_pics.setVisibility(View.GONE);
		}
	}
}
