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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.GridViewAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.beautyparlor.ProposalDetailObject;
import com.henglianmobile.beautyparlor.ui.activity.ShowPicturesActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
  
/**
 * 美容院-我的消息-方案请求详情
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_beautyparlor_proposal_request_detail)
public class ProposalRequestDetailActivity extends BaseActivity {

	@ViewById
	protected TextView tv_nick, tv_phone, tv_topic, tv_publish_time;
	@ViewById
	protected ImageView iv_photo, iv_meiyou_pic, iv_meiyou_pic1,
			iv_meiyou_pic2;
	@ViewById 
	protected GridView gv_meiyou_pics;
	@ViewById   
	protected LinearLayout ll_meiyou_pics;
 
	private List<String> pics;
	private ProposalDetailObject proposalDetailObject;
 
	@Extra
	protected int id;

	@AfterViews
	protected void getData() {
		String url = Const.GETPROPOSALREQUESTDETAIL + id;
		getDataHttp(url);
	}

	private void getDataHttp(String url) {
		LogUtil.i("url", "ProposalRequestDetailActivity--url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<ProposalDetailObject>>() {
					}.getType();
					List<ProposalDetailObject> proposalDetailObjects = TApplication.gson
							.fromJson(responseString, type);
					if (proposalDetailObjects != null
							&& proposalDetailObjects.size() != 0) {
						proposalDetailObject = proposalDetailObjects.get(0);
						tv_nick.setText(proposalDetailObject.getDcNickName());
						tv_phone.setText(proposalDetailObject.getDcCellPhone());
						tv_topic.setText(proposalDetailObject.getDcContent());
						tv_publish_time.setText(Tools
								.DateStrToDateStr(proposalDetailObject
										.getDtAddTime()));
						String headImgPath = proposalDetailObject
								.getDcHeadImg();
						LogUtil.i("info", "headImg=" + headImgPath);
						ImageLoader.getInstance().displayImage(headImgPath,
								iv_photo, TApplication.optionsImage,
								new MyAnimateFirstDisplayListener());
						String topicPics = proposalDetailObject.getDcIpath();
						showPics(topicPics);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(ProposalRequestDetailActivity.this,
						Tools.HTTP_ERROR);
			}
		});
	}

	@Click
	protected void btn_back() {
		this.finish();
	}

	/**
	 * 推出方案
	 */
	@Click
	protected void btn_push_proposal() {
		PushProposalPublishActivity_.intent(this)
				.extra("proposalDetailObject", proposalDetailObject).start();
	}

	private void showPics(String topicPics) {
		LogUtil.i("info", "topicPics====" + topicPics);
		pics = new ArrayList<String>();
		if (!"".equals(topicPics)) {

			String[] topics = topicPics.split(",");
			for (int i = 0; i < topics.length; i++) {
				pics.add(topics[i]);
			}
			if (topics.length == 1) {
				iv_meiyou_pic.setVisibility(View.VISIBLE);
				ll_meiyou_pics.setVisibility(View.GONE);
				gv_meiyou_pics.setVisibility(View.GONE);
				String picUrlone = topics[0];
				ImageLoader.getInstance().displayImage(picUrlone,
						iv_meiyou_pic, TApplication.optionsImage,
						new MyAnimateFirstDisplayListener());

				iv_meiyou_pic.setTag(pics);
				iv_meiyou_pic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						ShowPicturesActivity_
//								.intent(ProposalRequestDetailActivity.this)
//								.extra("position", 0)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(ProposalRequestDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
						intent.putExtra("position", 0);
						intent.putStringArrayListExtra("pics",
										(ArrayList<String>) pics);
						startActivity(intent);
					}
				});

			} else if (topics.length == 2) {
				ll_meiyou_pics.setVisibility(View.VISIBLE);
				iv_meiyou_pic.setVisibility(View.GONE);
				gv_meiyou_pics.setVisibility(View.GONE);
				ImageLoader.getInstance().displayImage(topics[0],
						iv_meiyou_pic1, TApplication.optionsImage,
						new MyAnimateFirstDisplayListener());
				ImageLoader.getInstance().displayImage(topics[1],
						iv_meiyou_pic2, TApplication.optionsImage,
						new MyAnimateFirstDisplayListener());

				iv_meiyou_pic1.setTag(pics);
				iv_meiyou_pic1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						ShowPicturesActivity_
//								.intent(ProposalRequestDetailActivity.this)
//								.extra("position", 0)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(ProposalRequestDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
						intent.putExtra("position", 0);
						intent.putStringArrayListExtra("pics",
										(ArrayList<String>) pics);
						startActivity(intent);
					}
				});

				iv_meiyou_pic2.setTag(pics);
				iv_meiyou_pic2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						ShowPicturesActivity_
//								.intent(ProposalRequestDetailActivity.this)
//								.extra("position", 1)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(ProposalRequestDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
						intent.putExtra("position", 1);
						intent.putStringArrayListExtra("pics",
										(ArrayList<String>) pics);
						startActivity(intent);
					}
				});

			} else if (topics.length >= 3) {
				gv_meiyou_pics.setVisibility(View.VISIBLE);
				iv_meiyou_pic.setVisibility(View.GONE);
				ll_meiyou_pics.setVisibility(View.GONE);

				GridViewAdapter adapter = new GridViewAdapter(
						ProposalRequestDetailActivity.this, pics);
				gv_meiyou_pics.setAdapter(adapter);
				gv_meiyou_pics.setTag(pics);
				gv_meiyou_pics
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
//								ShowPicturesActivity_
//										.intent(ProposalRequestDetailActivity.this)
//										.extra("position", position)
//										.stringArrayListExtra("pics",
//												(ArrayList<String>) pics)
//										.start();
								Intent intent = new Intent(ProposalRequestDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
								intent.putExtra("position", position);
								intent.putStringArrayListExtra("pics",
												(ArrayList<String>) pics);
								startActivity(intent);
							}
						});
			}
		} else {
			gv_meiyou_pics.setVisibility(View.GONE);
			iv_meiyou_pic.setVisibility(View.GONE);
			ll_meiyou_pics.setVisibility(View.GONE);
		}
	}
}
