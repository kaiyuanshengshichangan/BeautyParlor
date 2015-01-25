package com.henglianmobile.beautyparlor.ui.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.qzone.QZone.ShareParams;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.GridViewAdapter;
import com.henglianmobile.beautyparlor.adapter.MeiYouQuanPinglunAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.MeiYouQuanCommentListObject;
import com.henglianmobile.beautyparlor.entity.MeiYouQuanDetailObject;
import com.henglianmobile.beautyparlor.entity.beautyparlor.GuangGaoDetailObject;
import com.henglianmobile.beautyparlor.entity.beautyparlor.ProposalDetailObject;
import com.henglianmobile.beautyparlor.ui.wxapi.ShareSDKUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.ListViewUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

@EActivity(R.layout.activity_meiyouquan_detail)
public class MeiYouQuanDetailActivity extends BaseActivity implements
		OnClickListener {

	@ViewById
	protected TextView tv_meiyou_nick, tv_meiyou_topic, tv_publish_time,
			tv_pinglun_count, tv_zan_count;
	@ViewById
	protected ImageView iv_meiyou_photo, iv_meiyou_pic, iv_meiyou_pic1,
			iv_meiyou_pic2, iv_pinglun;
	@ViewById
	protected GridView gv_meiyou_pics;
	@ViewById
	protected LinearLayout ll_meiyou_pics, ll_submit_commit, ll_bottom;
	@ViewById
	protected ListView lv_pinglun;
	@ViewById
	protected EditText et_comment;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private LinearLayout ll_share_weixin, ll_share_qq;

	private List<String> pics;
	private String topicPics;

	@Extra
	protected int tId, tType;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String text = ShareSDKUtil.actionToString(msg.arg2);
			switch (msg.arg1) {
			case 0:
				Tools.showMsg(MeiYouQuanDetailActivity.this, "分享成功");
				dialog.dismiss();
				break;
			case 1:
				Tools.showMsg(MeiYouQuanDetailActivity.this, "分享失败");
				dialog.dismiss();
				break;
			case 2:
				Tools.showMsg(MeiYouQuanDetailActivity.this, "取消分享");
				dialog.dismiss();
				break;
			case 3:
				Tools.showMsg(MeiYouQuanDetailActivity.this, "分享出现错误");
				dialog.dismiss();
				break;

			case 101: {
				// 成功
				Platform plat = (Platform) msg.obj;
				text = plat.getName() + " completed at " + text;
			}
				break;
			case 102: {
				// 失败
				if ("WechatClientNotExistException".equals(msg.obj.getClass()
						.getSimpleName())) {
					text = MeiYouQuanDetailActivity.this.getString(
							R.string.wechat_client_inavailable);
				} else if ("WechatTimelineNotSupportedException".equals(msg.obj
						.getClass().getSimpleName())) {
					text = MeiYouQuanDetailActivity.this.getString(
							R.string.wechat_client_inavailable);
				} else {
					text = MeiYouQuanDetailActivity.this.getString(R.string.share_failed);
				}
			}
				break;
			case 103: {
				// 取消
				Platform plat = (Platform) msg.obj;
				text = plat.getName() + " canceled at " + text;
			}
				break;
			default:
				break;
			}
		};
	};

	@AfterViews
	protected void getData() {
		if (tType == Constant.TIE_YUANSHENG) {
			String url = Const.GETMEIYOUQUANDETAILLISTURL + tId;
			getDataHttp(url);
		} else if (tType == Constant.TIE_GUANGGAO) {
			String url = Const.GETMYGUANGGAODETAILURL + tId;
			getDataHttpGuangGao(url);
		} else if (tType == Constant.TIE_FANGAN) {
			String url = Const.GETPROPOSALREQUESTDETAIL + tId;
			getDataHttpFangAn(url);
		}
		String url1 = Const.GETMEIYOUQUANDETAILCOMMENTURL + tId + "&type="
				+ tType;
		getCommentHttp(url1);
	}

	private void getDataHttpGuangGao(String url) {
		LogUtil.i("url", "MeiYouQuanDetailActivity--url=" + url);
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
						tv_meiyou_nick.setText(guangGaoDetailObject
								.getDcNickName());
						tv_meiyou_topic.setText(guangGaoDetailObject
								.getDcContent());
						tv_publish_time.setText(Tools
								.DateStrToDateStr(guangGaoDetailObject
										.getDtAddTime()));
						tv_zan_count.setText(guangGaoDetailObject
								.getLikeCount() + "");
						// 获取图片

						String photoPic = guangGaoDetailObject.getDcHeadImg()
								.trim();
						if (photoPic != null && !TextUtils.isEmpty(photoPic)) {
							ImageLoader.getInstance().displayImage(photoPic,
									iv_meiyou_photo, TApplication.optionsImage,
									new MyAnimateFirstDisplayListener());
						}

						String topicPics = guangGaoDetailObject.getDcIpath()
								.trim();
						showPics(topicPics);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MeiYouQuanDetailActivity.this, Tools.HTTP_ERROR);
			}
		});
	}

	private void getDataHttpFangAn(String url) {
		LogUtil.i("url", "MeiYouQuanDetailActivity--url=" + url);
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
						tv_meiyou_nick.setText(proposalDetailObject
								.getToNickName());
						String topicContent = proposalDetailObject
								.getDcContent();
						tv_meiyou_topic.setText(topicContent);
						tv_publish_time.setText(Tools
								.DateStrToDateStr(proposalDetailObject
										.getDtAddTime()));
						tv_zan_count.setText(proposalDetailObject
								.getLikeCount() + "");
						// 获取图片

						String photoPic = proposalDetailObject.getToHeadImg();
						if (photoPic != null && !TextUtils.isEmpty(photoPic)) {
							ImageLoader.getInstance().displayImage(photoPic,
									iv_meiyou_photo, TApplication.optionsImage,
									new MyAnimateFirstDisplayListener());
						}

						String topicPics = proposalDetailObject.getDcIpath()
								.trim();
						showPics(topicPics);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MeiYouQuanDetailActivity.this, Tools.HTTP_ERROR);
			}
		});
	}

	private void getDataHttp(String url) {
		LogUtil.i("url", "MeiYouQuanDetailActivity--url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<MeiYouQuanDetailObject>>() {
					}.getType();
					List<MeiYouQuanDetailObject> meiYouQuanDetailObjects = TApplication.gson
							.fromJson(responseString, type);
					if (meiYouQuanDetailObjects != null
							&& meiYouQuanDetailObjects.size() != 0) {
						MeiYouQuanDetailObject meiYouQuanDetailObject = meiYouQuanDetailObjects
								.get(0);
						tv_meiyou_nick.setText(meiYouQuanDetailObject
								.getDcNickName());
						tv_meiyou_topic.setText(meiYouQuanDetailObject
								.getDcTopicContent());
						tv_publish_time.setText(Tools
								.DateStrToDateStr(meiYouQuanDetailObject
										.getDtAddTime()));
						tv_zan_count.setText(meiYouQuanDetailObject
								.getLikeCount() + "");
						// 获取图片

						String photoPic = meiYouQuanDetailObject.getDcHeadImg()
								.trim();
						if (photoPic != null && !TextUtils.isEmpty(photoPic)) {
							ImageLoader.getInstance().displayImage(photoPic,
									iv_meiyou_photo, TApplication.optionsImage,
									new MyAnimateFirstDisplayListener());
						}

						topicPics = meiYouQuanDetailObject.getDcIpath().trim();
						showPics(topicPics);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MeiYouQuanDetailActivity.this, Tools.HTTP_ERROR);
			}
		});
	}

	@Click
	protected void btn_back() {
		this.finish();
	}

	@Click
	protected void iv_comment_bottom() {
		if (ll_submit_commit.getVisibility() == View.GONE) {
			ll_submit_commit.setVisibility(View.VISIBLE);
			ll_bottom.setVisibility(View.GONE);
		} else {
			ll_submit_commit.setVisibility(View.GONE);
		}
	}

	@Click
	protected void iv_zan_bottom() {
		String url = Const.CLICKZANURL + "userId=" + TApplication.user.getUid()
				+ "&type=" + tType + "&dnOid=" + tId;
		zanHttp(url);
	}

	/**
	 * 分享
	 */
	@Click
	protected void iv_share_bottom() {
		builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_share,
				null);
		// iv_share_weixin,iv_share_qq
		ll_share_weixin = (LinearLayout) view
				.findViewById(R.id.ll_share_weixin);
		ll_share_qq = (LinearLayout) view.findViewById(R.id.ll_share_qq);
		ll_share_weixin.setOnClickListener(this);
		ll_share_qq.setOnClickListener(this);
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		String content = tv_meiyou_topic.getText().toString();
		LogUtil.i("info", "qq分享topicContent=" + content);
		ShareParams sp = new ShareParams();
		sp.setTitle("");
		sp.setTitleUrl("http://www.henglianmobile.com"); // 标题的超链接
		sp.setText(content);
		if (pics.size() != 0) {
			sp.setImageUrl(pics.get(0));
//			sp.setImageUrl("http://f.hiphotos.baidu.com/image/pic/item/b219ebc4b74543a977adc9d01d178a82b9011473.jpg");
		}
		sp.setSite("要你好看");
		sp.setSiteUrl("http://www.henglianmobile.com");

		sp.setVenueName("美容院");
		sp.setVenueDescription("This is a beautiful place!");
		switch (v.getId()) {
		case R.id.ll_share_weixin:
			LogUtil.i("info", "微信分享");
			shareWeiXin(sp);
			break;
		case R.id.ll_share_qq:
			shareQQ(sp);
			break;

		default:
			break;
		}
		dialog.dismiss();
	}

	private void shareWeiXin(ShareParams sp) {
//		String content = tv_meiyou_topic.getText().toString();
//		ShareParams sp = new ShareParams();
//		sp.setTitle("");
//		sp.setText(content);
//		sp.setShareType(Platform.SHARE_IMAGE);
//		sp.setImageUrl("http://www.wyl.cc/wp-content/uploads/2014/02/10060381306b675f5c5.jpg");
//		Platform plat =  ShareSDK.getPlatform (MeiYouQuanDetailActivity.this, WechatMoments.NAME);
		Platform plat = ShareSDK.getPlatform("WechatMoments");
		plat.setPlatformActionListener(paListener2);
		plat.share(sp);
	}

	private void shareQQ(ShareParams sp) {
		
		Platform qzone = ShareSDK.getPlatform(QZone.NAME);
		qzone.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}

	private void zanHttp(String url) {
		LogUtil.i("url", "点赞----url==" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String responseString) {
				if (arg0 == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = jsonObject.getInt("response");
						if (response == 0) {
							Tools.showMsg(MeiYouQuanDetailActivity.this,
									"点赞失败!");
						} else if (response > 0) {
							Tools.showMsg(MeiYouQuanDetailActivity.this, "赞!!!");
							getData();
						} else if (response == -2) {
							Tools.showMsg(MeiYouQuanDetailActivity.this,
									"您已赞!谢谢!");
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
				Tools.showMsg(MeiYouQuanDetailActivity.this, HTTP_ERROR);
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
		String url = Const.ADDMEIYOUQUANCOMMENTURL + "tid=" + tId + "&type="
				+ tType + "&uid=" + TApplication.user.getUid() + "&fname="
				+ TApplication.user.getNickname() + "&content=" + comment;
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
							Tools.showMsg(MeiYouQuanDetailActivity.this,
									"发送评论失败!");
						} else if (response > 0) {
							Tools.showMsg(MeiYouQuanDetailActivity.this,
									"评论发送成功!");
							ll_submit_commit.setVisibility(View.GONE);
							ll_bottom.setVisibility(View.VISIBLE);
							et_comment.setText("");
							String url = Const.GETMEIYOUQUANDETAILCOMMENTURL
									+ tId + "&type=" + tType;
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
				Tools.showMsg(MeiYouQuanDetailActivity.this, HTTP_ERROR);
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
								MeiYouQuanDetailActivity.this,
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
				Tools.showMsg(MeiYouQuanDetailActivity.this, HTTP_ERROR);
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
				MeiYouQuanDetailActivity.this.finish();
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
						ShowPicturesActivity_
								.intent(MeiYouQuanDetailActivity.this)
								.extra("position", 0)
								.stringArrayListExtra("pics",
										(ArrayList<String>) pics).start();
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
						ShowPicturesActivity_
								.intent(MeiYouQuanDetailActivity.this)
								.extra("position", 0)
								.stringArrayListExtra("pics",
										(ArrayList<String>) pics).start();
					}
				});

				iv_meiyou_pic2.setTag(pics);
				iv_meiyou_pic2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ShowPicturesActivity_
								.intent(MeiYouQuanDetailActivity.this)
								.extra("position", 1)
								.stringArrayListExtra("pics",
										(ArrayList<String>) pics).start();
					}
				});

			} else if (topics.length >= 3) {
				gv_meiyou_pics.setVisibility(View.VISIBLE);
				iv_meiyou_pic.setVisibility(View.GONE);
				ll_meiyou_pics.setVisibility(View.GONE);

				GridViewAdapter adapter = new GridViewAdapter(
						MeiYouQuanDetailActivity.this, pics);
				gv_meiyou_pics.setAdapter(adapter);
				gv_meiyou_pics.setTag(pics);
				gv_meiyou_pics
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								ShowPicturesActivity_
										.intent(MeiYouQuanDetailActivity.this)
										.extra("position", position)
										.stringArrayListExtra("pics",
												(ArrayList<String>) pics)
										.start();
							}
						});
			}
		} else {
			gv_meiyou_pics.setVisibility(View.GONE);
			iv_meiyou_pic.setVisibility(View.GONE);
			ll_meiyou_pics.setVisibility(View.GONE);
		}
	}

	private PlatformActionListener paListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			LogUtil.i("info", "--------");
			Message msg = new Message();
			msg.arg1 = 3;
			handler.sendMessage(msg);
		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			LogUtil.i("info", "-------arg2-" + arg2.toString());
			Message msg = new Message();
			if (arg2.containsKey("ret")) {
				msg.arg1 = 1;
				handler.sendMessage(msg);
			} else {
				msg.arg1 = 0;
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			LogUtil.i("info", "======");
			Message msg = new Message();
			msg.arg1 = 2;
			handler.sendMessage(msg);
		}
	};
	private PlatformActionListener paListener2 = new PlatformActionListener() {

		public void onComplete(Platform plat, int action,
				HashMap<String, Object> res) {
			Message msg = new Message();
			msg.arg1 = 101;
			msg.arg2 = action;
			msg.obj = plat;
			handler.sendMessage(msg);
		}

		public void onCancel(Platform plat, int action) {
			Message msg = new Message();
			msg.arg1 = 103;
			msg.arg2 = action;
			msg.obj = plat;
			handler.sendMessage(msg);
		}

		public void onError(Platform plat, int action, Throwable t) {
			t.printStackTrace();

			Message msg = new Message();
			msg.arg1 = 102;
			msg.arg2 = action;
			msg.obj = t;
			handler.sendMessage(msg);
		}
	};
}
