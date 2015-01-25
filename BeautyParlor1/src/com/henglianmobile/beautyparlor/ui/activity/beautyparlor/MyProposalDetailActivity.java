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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
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
import com.henglianmobile.beautyparlor.entity.beautyparlor.ProposalDetailObject;
import com.henglianmobile.beautyparlor.ui.activity.ShowPicturesActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.ListViewUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
 
/**
 * 美容院-我的方案-方案详情
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_beautyparlor_proposal_detail)
public class MyProposalDetailActivity extends BaseActivity {
 
	@ViewById
	protected TextView tv_title, tv_content, tv_publish_time;
	@ViewById
	protected EditText et_comment;
	@ViewById
	protected ImageView iv_meiyou_pic, iv_meiyou_pic1,
			iv_meiyou_pic2;
	@ViewById
	protected GridView gv_meiyou_pics;
	@ViewById 
	protected LinearLayout ll_meiyou_pics, ll_submit_commit, ll_bottom;
	@ViewById
	protected ListView lv_pinglun;
 
	private List<String> pics;
	private ProposalDetailObject proposalDetailObject;

	@Extra
	protected int pId;

	@AfterViews
	protected void getData() {
		String url = Const.GETPROPOSALREQUESTDETAIL + pId;
		getDataHttp(url);
		String url1 = Const.GETMEIYOUQUANDETAILCOMMENTURL + pId + "&type="
				+ Constant.FANGAN;
		getCommentHttp(url1);
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
						String title = "给"
								+ proposalDetailObject.getToNickName()
								+ "推出的整容" + proposalDetailObject.getTypeName()
								+ "部的方案";
						tv_title.setText(title);
						tv_content.setText(proposalDetailObject.getDcContent());
						tv_publish_time.setText(Tools
								.DateStrToDateStr(proposalDetailObject
										.getDtAddTime()));
						String topicPics = proposalDetailObject.getDcIpath();
						showPics(topicPics);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MyProposalDetailActivity.this, Tools.HTTP_ERROR);
			}
		});
	}

	@Click
	protected void btn_back() {
		this.finish();
	}

	@Click
	protected void ll_bottom() {
		if (ll_submit_commit.getVisibility() == View.GONE) {
			ll_submit_commit.setVisibility(View.VISIBLE);
			ll_bottom.setVisibility(View.GONE);
		} else {
			ll_submit_commit.setVisibility(View.GONE);
		}
	}

	@Click
	protected void btn_send() {// 发送评论
		String comment = et_comment.getText().toString().trim();
		if (TextUtils.isEmpty(comment)) {
			Tools.showMsg(this, "发送内容不能为空!");
			return;
		}
		String url = Const.ADDMEIYOUQUANCOMMENTURL + "tid=" + pId + "&type="
				+ Constant.FANGAN + "&uid=" + TApplication.user.getUid()
				+ "&fname=" + TApplication.user.getNickname() + "&content="
				+ comment;
		sendHttpGet(url);
	}

	private void sendHttpGet(String url) {
		LogUtil.i("url", "发送评论----url==" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String responseString) {
				if (arg0 == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = jsonObject.getInt("response");
						if (response == 0) {
							Tools.showMsg(MyProposalDetailActivity.this,
									"回复失败!");
						} else if (response > 0) {
							Tools.showMsg(MyProposalDetailActivity.this,
									"回复成功!");
							et_comment.setText("");
							ll_submit_commit.setVisibility(View.GONE);
							ll_bottom.setVisibility(View.VISIBLE);
							String url = Const.GETMEIYOUQUANDETAILCOMMENTURL
									+ pId + "&type=" + Constant.FANGAN;
							getCommentHttp(url);
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
				Tools.showMsg(MyProposalDetailActivity.this, HTTP_ERROR);
			}
		});
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
								MyProposalDetailActivity.this,
								meiYouQuanCommentListObjects);
						lv_pinglun.setAdapter(adapter);
						ListViewUtil
								.setListViewHeightBasedOnChildren(lv_pinglun);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MyProposalDetailActivity.this, HTTP_ERROR);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			if (ll_submit_commit.getVisibility() == View.VISIBLE) {
				ll_submit_commit.setVisibility(View.GONE);
				ll_bottom.setVisibility(View.VISIBLE);
			} else {
				MyProposalDetailActivity.this.finish();
			}
			return true;
		}
		return false;
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
						// ShowPicturesActivity_
						// .intent(MyProposalDetailActivity.this)
						// .extra("position", 0)
						// .stringArrayListExtra("pics",
						// (ArrayList<String>) pics).start();
						Intent intent = new Intent(
								MyProposalDetailActivity.this,
								AnnotationClassUtil
										.get(ShowPicturesActivity.class));
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
						// ShowPicturesActivity_
						// .intent(MyProposalDetailActivity.this)
						// .extra("position", 0)
						// .stringArrayListExtra("pics",
						// (ArrayList<String>) pics).start();
						Intent intent = new Intent(
								MyProposalDetailActivity.this,
								AnnotationClassUtil
										.get(ShowPicturesActivity.class));
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
//								.intent(MyProposalDetailActivity.this)
//								.extra("position", 1)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(MyProposalDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
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
						MyProposalDetailActivity.this, pics);
				gv_meiyou_pics.setAdapter(adapter);
				gv_meiyou_pics.setTag(pics);
				gv_meiyou_pics
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
//								ShowPicturesActivity_
//										.intent(MyProposalDetailActivity.this)
//										.extra("position", position)
//										.stringArrayListExtra("pics",
//												(ArrayList<String>) pics)
//										.start();
								Intent intent = new Intent(MyProposalDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
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
