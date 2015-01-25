package com.henglianmobile.beautyparlor.ui.activity;

import java.lang.reflect.Type;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.doublefi123.diary.widget.CircularImage;
import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.UserInfoDetailObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 美友圈-美友基本信息详情
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_meiyou_baseinfo_detail)
public class MeiYouBaseInfoDetailActivity extends BaseActivity {
	@ViewById
	protected TextView et_user_nick, et_age, et_address, et_sign;
	@ViewById
	protected TextView tv_beautyparlor_name, tv_beautyparlor_introduce,
			tv_manager, tv_beautyparlor_phone, tv_beautyparlor_people_count,
			et_beautyparlor_address, et_jishi_name;
	@ViewById
	protected ImageView iv_beautyparlor_photo, iv_jishi_photo, iv_company_pic,
			iv_certificate, iv_zizhizhengshu;
	@ViewById
	protected CircularImage iv_my_photo;
	@ViewById
	protected LinearLayout ll_consumer, ll_no;
	@ViewById
	protected ScrollView sv_beautyparlor;
	@ViewById
	protected Button btn_add;
	@ViewById
	protected ImageView iv_sex;

	private String userType,techniImgPath,companyImgPath,licenceImgPath,cerImgPath;
	@Extra
	protected int id,friend;

	@AfterViews
	protected void getData() {
		if (id == TApplication.getInstance().userInfoDetailObject.getDnUserid()) {
			btn_add.setVisibility(View.GONE);
		}
		if(friend == 1){
			btn_add.setVisibility(View.GONE);
		}
		String url = Const.GETUSERINFODETAILURL + id;
		getDataHttp(url);
	}

	private void getDataHttp(String url) {
		LogUtil.i("url", "MeiYouBaseInfoDetailActivity--url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<UserInfoDetailObject>>() {
					}.getType();
					List<UserInfoDetailObject> userInfoDetailObjects = TApplication.gson
							.fromJson(responseString, type);
					if (userInfoDetailObjects != null
							&& userInfoDetailObjects.size() != 0) {
						UserInfoDetailObject meiYouInfoDetail = userInfoDetailObjects
								.get(0);
						userType = meiYouInfoDetail.getDnType() + "";
						if (Constant.BEAUTYPARLOR.equals(userType)) {
							sv_beautyparlor.setVisibility(View.VISIBLE);
							ll_consumer.setVisibility(View.GONE);
							ll_no.setVisibility(View.GONE);
							showTextBeautyParlor(meiYouInfoDetail);
						} else {
							ll_consumer.setVisibility(View.VISIBLE);
							sv_beautyparlor.setVisibility(View.GONE);
							ll_no.setVisibility(View.GONE);
							showTextConsumer(meiYouInfoDetail);
						}
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MeiYouBaseInfoDetailActivity.this,
						Tools.HTTP_ERROR);
			}
		});
	}

	@Click
	protected void btn_back() {
		this.finish();
	}
	//techniImgPath,companyImgPath,licenceImgPath,cerImgPath
	@Click
	protected void iv_jishi_photo() {
		ShowPictureActivity_.intent(this).extra("picPath", techniImgPath).start();
	}
	@Click
	protected void iv_company_pic() {
		ShowPictureActivity_.intent(this).extra("picPath", companyImgPath).start();
	}
	@Click
	protected void iv_certificate() {
		ShowPictureActivity_.intent(this).extra("picPath", licenceImgPath).start();
	}
	@Click
	protected void iv_zizhizhengshu() {
		ShowPictureActivity_.intent(this).extra("picPath", cerImgPath).start();
	}

	/**
	 * 添加好友
	 */
	@Click
	protected void btn_add() {
		String url = Const.ADDFRIENDSURL
				+ "uid=" + TApplication.user.getUid()
				+ "&fid=" + id
				+ "&userName=" + TApplication.getInstance().userInfoDetailObject
						.getDcNickName();
		addFriendHttp(url);
	}

	private void addFriendHttp(String url) {
		LogUtil.i("url", "MeiYouBaseInfoDetailActivity--url=" + url);
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
							Tools.showMsg(MeiYouBaseInfoDetailActivity.this,
									"添加请求发送失败，请稍后重试!");
						} else if (response > 1) {
							Tools.showMsg(MeiYouBaseInfoDetailActivity.this,
									"添加请求已发送!");
							MeiYouBaseInfoDetailActivity.this.finish();
						} else if (response == -2) {
							Tools.showMsg(MeiYouBaseInfoDetailActivity.this,
									"该用户已经是您的好友!");
						} else if (response == -3) {
							Tools.showMsg(MeiYouBaseInfoDetailActivity.this,
									"您已经发送了添加好友请求,请耐心等待!");
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
				Tools.showMsg(MeiYouBaseInfoDetailActivity.this,
						Tools.HTTP_ERROR);
			}
		});
	}

	protected void showTextBeautyParlor(UserInfoDetailObject meiYouInfoDetail) {
		// , ,
		// , , ,
		// ,
		tv_beautyparlor_name.setText(meiYouInfoDetail.getDcNickName());
		tv_beautyparlor_introduce.setText(meiYouInfoDetail.getDcContent());
		tv_manager.setText(meiYouInfoDetail.getAdminName());
		tv_beautyparlor_phone.setText(meiYouInfoDetail.getDcPhone());
		tv_beautyparlor_people_count.setText(meiYouInfoDetail.getPeopleNum());
		et_beautyparlor_address.setText(meiYouInfoDetail.getAddress());
		et_jishi_name.setText(meiYouInfoDetail.getTechnicName());

		String headImgPath = meiYouInfoDetail.getDcHeadImg();
		techniImgPath = meiYouInfoDetail.getTechniImg();
		companyImgPath = meiYouInfoDetail.getCompanyImg();
		licenceImgPath = meiYouInfoDetail.getLicenceImg();
		cerImgPath = meiYouInfoDetail.getCertImg();
		ImageLoader.getInstance().displayImage(headImgPath,
				iv_beautyparlor_photo, TApplication.optionsImage,
				new MyAnimateFirstDisplayListener());
		ImageLoader.getInstance().displayImage(techniImgPath,
				iv_jishi_photo, TApplication.optionsImage,
				new MyAnimateFirstDisplayListener());
		ImageLoader.getInstance().displayImage(companyImgPath,
				iv_company_pic, TApplication.optionsImage,
				new MyAnimateFirstDisplayListener());
		ImageLoader.getInstance().displayImage(licenceImgPath,
				iv_certificate, TApplication.optionsImage,
				new MyAnimateFirstDisplayListener());
		ImageLoader.getInstance().displayImage(cerImgPath,
				iv_zizhizhengshu, TApplication.optionsImage,
				new MyAnimateFirstDisplayListener());
	}

	protected void showTextConsumer(UserInfoDetailObject meiYouInfoDetail) {
		et_user_nick.setText(meiYouInfoDetail.getDcNickName());
		et_age.setText(meiYouInfoDetail.getAge() + "");
		et_address.setText(meiYouInfoDetail.getAddress());
		et_sign.setText(meiYouInfoDetail.getDcSign());
		String photoPath = meiYouInfoDetail.getDcHeadImg();
		ImageLoader.getInstance().displayImage(photoPath, iv_my_photo,
				TApplication.optionsImage, new MyAnimateFirstDisplayListener());
		int sex = meiYouInfoDetail.getSex();
		if (sex == 0) {
			iv_sex.setImageResource(R.drawable.iv_female);
		} else {
			iv_sex.setImageResource(R.drawable.iv_man);
		}
	}
}
