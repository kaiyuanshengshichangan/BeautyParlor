package com.henglianmobile.beautyparlor.ui.activity.consumer;

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
import com.henglianmobile.beautyparlor.util.SendHttpUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 普通用户-我的方案详情
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_consumer_myproposal_detail)
public class ProposalDetailActivity extends BaseActivity {

	@ViewById
	protected TextView tv_proposal_title, tv_proposal_content, tv_publish_time,
			tv_pinglun_count, tv_zan_count;
	@ViewById
	protected ImageView iv_meiyou_photo, iv_meiyou_pic, iv_meiyou_pic1,
		 	iv_meiyou_pic2, iv_pinglun;
	@ViewById  
	protected LinearLayout ll_meiyou_pics, ll_submit_commit, ll_bottom;
	@ViewById  
	protected GridView gv_meiyou_pics;
	@ViewById 
	protected ListView lv_pinglun;
	@ViewById
	protected EditText et_comment;

	private List<String> pics;
	/** 推出方案美容院的id */
	private int userId;
 
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
		LogUtil.i("url", "ProposalDetailActivity--url=" + url);
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
						ProposalDetailObject proposalDetailObject = proposalDetailObjects
								.get(0);
						userId = proposalDetailObject.getDnUserid();
						
						String title = proposalDetailObject.getDcNickName()
								+ "推出的整容" + proposalDetailObject.getTypeName()
								+ "部的方案";
						tv_proposal_title.setText(title);
						tv_proposal_content.setText(proposalDetailObject
								.getDcContent());
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
				Tools.showMsg(ProposalDetailActivity.this, Tools.HTTP_ERROR);
			}
		});
	}

	@Click
	protected void btn_back() {
		this.finish();
	}

	/**
	 * 评论
	 */
	@Click
	protected void iv_comment_bottom() {
		if (ll_submit_commit.getVisibility() == View.GONE) {
			ll_submit_commit.setVisibility(View.VISIBLE);
			ll_bottom.setVisibility(View.GONE);
		} else {
			ll_submit_commit.setVisibility(View.GONE);
		}
	}

	/**
	 * 分享
	 */
	@Click
	protected void iv_share_bottom() {
		String url = Const.SHAREPROPOSALTOMEIYOUQUANURL + "pId=" + pId
				+ "&isShared=1";
		shareToMeiyouquan(url);
	}

	private void shareToMeiyouquan(String url) {
		SendHttpUtil.submitHttpGet(url, ProposalDetailActivity.this, "分享失败!", "分享成功!", false);
	}
	/**
	 * 加好友
	 */
	@Click
	protected void iv_add_friend_bottom() {
		String url = Const.ADDFRIENDSURL
				+ "uid=" + TApplication.user.getUid()
				+ "&fid=" + userId
				+ "&userName=" + TApplication.getInstance().userInfoDetailObject
						.getDcNickName();
		addFriendHttp(url);
	}
	private void addFriendHttp(String url) {
		LogUtil.i("url", "ProposalDetailActivity--url=" + url);
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
							Tools.showMsg(ProposalDetailActivity.this,
									"添加请求发送失败，请稍后重试!");
						} else if (response > 1) {
							Tools.showMsg(ProposalDetailActivity.this,
									"添加请求已发送!");
						} else if (response == -2) {
							Tools.showMsg(ProposalDetailActivity.this,
									"该用户已经是您的好友!");
						} else if (response == -3) {
							Tools.showMsg(ProposalDetailActivity.this,
									"您已经发送了添加好友请求!");
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
				Tools.showMsg(ProposalDetailActivity.this,
						Tools.HTTP_ERROR);
			}
		});
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
							Tools.showMsg(ProposalDetailActivity.this, "回复失败!");
						} else if (response > 0) {
							Tools.showMsg(ProposalDetailActivity.this, "回复成功!");
							ll_submit_commit.setVisibility(View.GONE);
							ll_bottom.setVisibility(View.VISIBLE);
							et_comment.setText("");
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
				Tools.showMsg(ProposalDetailActivity.this, HTTP_ERROR);
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
								ProposalDetailActivity.this,
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
				Tools.showMsg(ProposalDetailActivity.this, HTTP_ERROR);
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
				ProposalDetailActivity.this.finish();
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
//						ShowPicturesActivity_
//								.intent(ProposalDetailActivity.this)
//								.extra("position", 0)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(ProposalDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
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
//								.intent(ProposalDetailActivity.this)
//								.extra("position", 0)
//								.stringArrayListExtra("pics",
//										(ArrayList<String>) pics).start();
						Intent intent = new Intent(ProposalDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
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
						Intent intent = new Intent(ProposalDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
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
						ProposalDetailActivity.this, pics);
				gv_meiyou_pics.setAdapter(adapter);
				gv_meiyou_pics.setTag(pics);
				gv_meiyou_pics
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								Intent intent = new Intent(ProposalDetailActivity.this, AnnotationClassUtil.get(ShowPicturesActivity.class));
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
