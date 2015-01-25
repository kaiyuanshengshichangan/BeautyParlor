package com.henglianmobile.beautyparlor.ui.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.Login;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.ui.activity.consumer.MyInformationsActivity;
import com.henglianmobile.beautyparlor.ui.activity.consumer.UpdatePeronalInfoActivity;
import com.henglianmobile.beautyparlor.ui.activity.saleman.MyBeautyParlorActivity;
import com.henglianmobile.beautyparlor.ui.activity.saleman.MyCommissionActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.SPUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

@EFragment(R.layout.fragment_me_saleman)
public class MeSalemanFragment extends Fragment {
	@ViewById
	protected ImageView iv_my_photo;
	@ViewById
	protected TextView tv_my_nick, tv_my_sign, tv_account_bank,
			tv_account_number;
 
	@Override
	public void onResume() {
		super.onResume();
		setBaseData();
	}
 
	private void setBaseData() {
		tv_my_nick.setText(TApplication.getInstance().userInfoDetailObject.getDcNickName());
	 	tv_my_sign.setText(TApplication.getInstance().userInfoDetailObject.getDcSign());
		tv_account_bank.setText(TApplication.getInstance().userInfoDetailObject.getBankName());
		tv_account_number.setText(TApplication.getInstance().userInfoDetailObject.getAccountNo());
		String photoPath = TApplication.getInstance().userInfoDetailObject.getDcHeadImg();
		ImageLoader.getInstance().displayImage(photoPath,
				iv_my_photo, TApplication.optionsImage,
				new MyAnimateFirstDisplayListener());
	}
 
	/**
	 * 修改个人信息界面-跳转
	 */   
	@Click
	protected void iv_personal_info_edit() {
//		UpdatePeronalInfoActivity_.intent(getActivity())
//				.extra("saleman", 1).start();
		Intent intent = new Intent(this.getActivity(), AnnotationClassUtil.get(UpdatePeronalInfoActivity.class));
		intent.putExtra("saleman", 1);
		this.getActivity().startActivity(intent);
	}

	/**
	 * 我的美容院
	 */
	@Click
	protected void ll_my_beautyparlor() {
//		MyBeautyParlorActivity_.intent(getActivity()).start();
		Intent intent = new Intent(this.getActivity(), AnnotationClassUtil.get(MyBeautyParlorActivity.class));
		this.getActivity().startActivity(intent);
	}

	/**
	 * 我的提成
	 */
	@Click
	protected void ll_my_commission() {
//		MyCommissionActivity_.intent(getActivity()).start();
		Intent intent = new Intent(this.getActivity(), AnnotationClassUtil.get(MyCommissionActivity.class));
		this.getActivity().startActivity(intent);
	}

	/**
	 * 我的消息
	 */
	@Click
	protected void ll_my_info() {
//		MyInformationsActivity_.intent(this.getActivity())
//				.extra("userId", TApplication.user.getUid()).start();
		Intent intent = new Intent(this.getActivity(), AnnotationClassUtil.get(MyInformationsActivity.class));
		intent.putExtra("userId", TApplication.user.getUid());
		intent.putExtra("userType", 4);
		this.getActivity().startActivity(intent);
//		MyInformationsActivity_.intent(this.getActivity())
//				.extra("userId", TApplication.user.getUid())
//				.extra("userType", 4).start();
	}

	/**
	 * 退出
	 */
	@Click
	protected void btn_logout() {
		SPUtil.getUtil(MeSalemanFragment.this.getActivity()).saveToSp(
				SPUtil.PWD, "");
		Intent intent = new Intent(this.getActivity(), Login.class);
		intent.putExtra("logout", "1");
		startActivity(intent);
//		LoginActivity_.intent(getActivity()).extra("logout", "1").start();
		TApplication.getInstance().exitActivities();
	}

}
