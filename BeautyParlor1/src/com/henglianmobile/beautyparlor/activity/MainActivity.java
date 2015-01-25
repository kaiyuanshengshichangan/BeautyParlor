package com.henglianmobile.beautyparlor.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.apache.http.Header;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.adapter.MainFragmentPageAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.UserInfoDetailObject;
import com.henglianmobile.beautyparlor.service.UpdateApkService;
import com.henglianmobile.beautyparlor.ui.fragment.MeBeautyParlorFragment;
import com.henglianmobile.beautyparlor.ui.fragment.MeBeautyParlorFragment_;
import com.henglianmobile.beautyparlor.ui.fragment.MeConsumerFragment;
import com.henglianmobile.beautyparlor.ui.fragment.MeConsumerFragment_;
import com.henglianmobile.beautyparlor.ui.fragment.MeSalemanFragment;
import com.henglianmobile.beautyparlor.ui.fragment.MeSalemanFragment_;
import com.henglianmobile.beautyparlor.ui.fragment.MeiYouQuanFragment;
import com.henglianmobile.beautyparlor.ui.fragment.MeiYouQuanFragment_;
import com.henglianmobile.beautyparlor.ui.fragment.ShouYeFragment;
import com.henglianmobile.beautyparlor.ui.fragment.ShouYeFragment_;
import com.henglianmobile.beautyparlor.ui.fragment.ZiXunFragment;
import com.henglianmobile.beautyparlor.ui.fragment.ZiXunFragment_;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;

@WindowFeature({ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

	@ViewById
	protected RelativeLayout toast_conten;
	@ViewById
	protected ViewPager viewpager;
	@ViewById   
	protected Button btn_main_fragment_shouye, btn_main_fragment_zixun,
			btn_main_fragment_meiyouquan, btn_main_fragment_me;
	private List<Fragment> fragments;
	private MainFragmentPageAdapter adapter;
	private int currentFragmentIndex; 
	private String userType; 
	private MyMainActivityReceiver receiver;

    private double back_pressed; 
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	 	receiver = new MyMainActivityReceiver();
		IntentFilter filter = new IntentFilter();
		String action1 = Constant.SHOUYETOOTHERACTIVITY;
		String action2 = Constant.SHOUYEBEAUTYPARLORMYPROPOSAL;
		String action3 = Constant.UPDATEBASEINFOACTION;
		filter.addAction(action1);
		filter.addAction(action2);
		filter.addAction(action3);
		registerReceiver(receiver, filter);
		getData();
		checkVersion();
	}

	private void getData() {
		String url = Const.GETUSERINFODETAILURL + TApplication.user.getUid();
		getDataHttp(url);
	}

	private void getDataHttp(String url) {
		LogUtil.i("url", "MainActivity--url=" + url);
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
						UserInfoDetailObject userInfoDetailObject1 = userInfoDetailObjects
								.get(0);
						TApplication.getInstance().userInfoDetailObject = userInfoDetailObject1;
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(MainActivity.this, Tools.HTTP_ERROR);
			}
		});
	}
	@AfterViews 
	void initView() {
		userType = TApplication.user.getUtype();
		fragments = new ArrayList<Fragment>();
 
		ShouYeFragment shouYeFragment = new ShouYeFragment_();
		ZiXunFragment ziXunFragment = new ZiXunFragment_();
		MeiYouQuanFragment meiYouQuanFragment = new MeiYouQuanFragment_();
		MeBeautyParlorFragment beautyParlorFragment = new MeBeautyParlorFragment_();
		MeConsumerFragment consumerFragment = new MeConsumerFragment_();
		MeSalemanFragment salemanFragment = new MeSalemanFragment_();
		fragments.add(ziXunFragment);
		fragments.add(meiYouQuanFragment);
		LogUtil.i("info", "userType=" + userType);
		if (userType.equals(Constant.BEAUTYPARLOR)) {
			fragments.add(0, shouYeFragment);
			fragments.add(beautyParlorFragment);
		} else if (userType.equals(Constant.CONSUMER)) {
			fragments.add(0, shouYeFragment);
			fragments.add(consumerFragment);
		} else if (userType.equals(Constant.SALEMAN)) {
			fragments.add(salemanFragment);
			btn_main_fragment_shouye.setVisibility(View.GONE);
			btn_main_fragment_zixun
					.setBackgroundResource(R.drawable.btn_zixun_red);
			setInitViewSaleman();
			btn_main_fragment_zixun
					.setBackgroundResource(R.drawable.btn_zixun_red_big);
		}
		adapter = new MainFragmentPageAdapter(getSupportFragmentManager(),
				(ArrayList<Fragment>) fragments);
		viewpager.setAdapter(adapter);
		addListener();
	}

	public void addListener() {
		// 给ViewPager滑动事件加监听，当滑动时，按钮颜色随之改变

		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int fragmentIndex) {
				// TODO Auto-generated method stub
				if (userType.equals(Constant.SALEMAN)) {
					currentFragmentIndex = fragmentIndex;

					if (fragmentIndex == 0) {
						setInitViewSaleman();
						btn_main_fragment_zixun
								.setBackgroundResource(R.drawable.btn_zixun_red_big);
					} else if (fragmentIndex == 1) {
						setInitViewSaleman();
						btn_main_fragment_meiyouquan
								.setBackgroundResource(R.drawable.btn_meiyouquan_red_big);
					} else if (fragmentIndex == 2) {
						setInitViewSaleman();
						btn_main_fragment_me
								.setBackgroundResource(R.drawable.btn_me_red_big);
					}
				} else {
					currentFragmentIndex = fragmentIndex;

					if (fragmentIndex == 0) {
						setInitView();
						btn_main_fragment_shouye
								.setBackgroundResource(R.drawable.btn_shouye_red);
					} else if (fragmentIndex == 1) {
						setInitView();
						btn_main_fragment_zixun
								.setBackgroundResource(R.drawable.btn_zixun_red);
					} else if (fragmentIndex == 2) {
						setInitView();
						btn_main_fragment_meiyouquan
								.setBackgroundResource(R.drawable.btn_meiyouquan_red);
					} else if (fragmentIndex == 3) {
						setInitView();
						btn_main_fragment_me
								.setBackgroundResource(R.drawable.btn_me_red);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * 初始化Button-saleman
	 */
	private void setInitViewSaleman() {
		btn_main_fragment_zixun
				.setBackgroundResource(R.drawable.btn_zixun_black_big);
		btn_main_fragment_meiyouquan
				.setBackgroundResource(R.drawable.btn_meiyouquan_black_big);
		btn_main_fragment_me.setBackgroundResource(R.drawable.btn_me_black_big);
	}

	/**
	 * 初始化Button
	 */
	private void setInitView() {
		btn_main_fragment_shouye
				.setBackgroundResource(R.drawable.btn_shouye_black);
		btn_main_fragment_zixun
				.setBackgroundResource(R.drawable.btn_zixun_black);
		btn_main_fragment_meiyouquan
				.setBackgroundResource(R.drawable.btn_meiyouquan_black);
		btn_main_fragment_me.setBackgroundResource(R.drawable.btn_me_black);
	}

	@Click
	void btn_main_fragment_shouye() {
		setInitView();
		btn_main_fragment_shouye
				.setBackgroundResource(R.drawable.btn_shouye_red);
		currentFragmentIndex = 0;
		viewpager.setCurrentItem(currentFragmentIndex);
	}

	@Click
	void btn_main_fragment_zixun() {
		if (userType.equals(Constant.SALEMAN)) {
			setInitViewSaleman();
			btn_main_fragment_zixun
					.setBackgroundResource(R.drawable.btn_zixun_red_big);
			currentFragmentIndex = 0;
		} else {
			setInitView();
			btn_main_fragment_zixun
					.setBackgroundResource(R.drawable.btn_zixun_red);
			currentFragmentIndex = 1;
		}
		viewpager.setCurrentItem(currentFragmentIndex);
	}

	@Click
	void btn_main_fragment_meiyouquan() {
		if (userType.equals(Constant.SALEMAN)) {
			setInitViewSaleman();
			btn_main_fragment_meiyouquan
					.setBackgroundResource(R.drawable.btn_meiyouquan_red_big);
			currentFragmentIndex = 1;
		} else {
			setInitView();
			btn_main_fragment_meiyouquan
					.setBackgroundResource(R.drawable.btn_meiyouquan_red);
			currentFragmentIndex = 2;
		}
		viewpager.setCurrentItem(currentFragmentIndex);
	}

	@Click
	void btn_main_fragment_me() {
		if (userType.equals(Constant.SALEMAN)) {
			setInitViewSaleman();
			btn_main_fragment_me
					.setBackgroundResource(R.drawable.btn_me_red_big);
			currentFragmentIndex = 2;
		} else {
			setInitView();
			btn_main_fragment_me.setBackgroundResource(R.drawable.btn_me_red);
			currentFragmentIndex = 3;
		}
		viewpager.setCurrentItem(currentFragmentIndex);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	/**
     * 点击两次返回退出系统
     * 
     * @param view
     */
    @Override
    public void onBackPressed() {
    	if (back_pressed + 3000 > System.currentTimeMillis()) {
            finish();
            TApplication.getInstance().exitActivities();
            super.onBackPressed();
        }
        else
            Tools.showCustomToast(MainActivity.this,"再次点击，退出美容院!",toast_conten);
        back_pressed = System.currentTimeMillis();
    }
	private class MyMainActivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constant.SHOUYETOOTHERACTIVITY)) {
				viewpager.setCurrentItem(3);
			} else if (intent.getAction().equals(
					Constant.SHOUYEBEAUTYPARLORMYPROPOSAL)) {
				viewpager.setCurrentItem(3);
				Intent intent1 = new Intent(
						Constant.SHOUYEBEAUTYPARLORMYPROPOSALUPDATE);
				MainActivity.this.sendBroadcast(intent1);
			} else if (intent.getAction().equals(
					Constant.UPDATEBASEINFOACTION)) {
				getData();
			}

		}
	}
	/***
	 * 检查是否更新版本
	 */
	public void checkVersion() {
		
		LogUtil.i("info", "zheli=====================");
		if (TApplication.localVersion < TApplication.serverVersion) {
			LogUtil.i("info", "localVersion=" + TApplication.localVersion);
			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("软件升级")
					.setMessage("发现新版本,建议立即更新使用.")
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// 开启更新服务UpdateService
									// 这里为了把update更好模块化，可以传一些updateService依赖的值
									// 如布局ID，资源ID，动态获取的标题,这里以app_name为例
									Intent updateIntent = new Intent(
											MainActivity.this,
											UpdateApkService.class);
									updateIntent.putExtra(
											"app_name",
											getResources().getString(
													R.string.app_name));
									startService(updateIntent);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			alert.create().show();

		}
	}
}
