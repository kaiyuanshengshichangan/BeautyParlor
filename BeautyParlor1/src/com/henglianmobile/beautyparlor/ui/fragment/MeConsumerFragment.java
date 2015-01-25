package com.henglianmobile.beautyparlor.ui.fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.Login;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.UserInfoDetailObject;
import com.henglianmobile.beautyparlor.ui.activity.consumer.MyFriends;
import com.henglianmobile.beautyparlor.ui.activity.consumer.MyInformationsActivity;
import com.henglianmobile.beautyparlor.ui.activity.consumer.MyProposalActivity;
import com.henglianmobile.beautyparlor.ui.activity.consumer.ProposalRequestPublishActivity;
import com.henglianmobile.beautyparlor.ui.activity.consumer.UpdatePeronalInfoActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.SPUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 普通用户-我的
 * 
 * @author Administrator
 *  
 */
@EFragment(R.layout.fragment_me_consumer)
public class MeConsumerFragment extends Fragment {
	@ViewById
	protected ImageView iv_my_photo;
	@ViewById
	protected TextView tv_my_nick, tv_my_sign;
 
	@Override 
	public void onResume() {
		super.onResume();
//		String url = Const.GETUSERINFODETAILURL + TApplication.user.getUid();
//		getDataHttp(url);
		setBaseData();
	}

	private void setBaseData() {
		tv_my_nick.setText(TApplication.getInstance().userInfoDetailObject.getDcNickName());
		tv_my_sign.setText(TApplication.getInstance().userInfoDetailObject.getDcSign());
		String photoPath = TApplication.getInstance().userInfoDetailObject.getDcHeadImg();
		LogUtil.i("info", "photo="+photoPath);
		ImageLoader.getInstance().displayImage(photoPath,
				iv_my_photo, TApplication.optionsImage,
	 			new MyAnimateFirstDisplayListener());
	}
 
	/**
	 * 修改个人信息界面-跳转
	 */
	@Click
	protected void iv_personal_info_edit() {
//		UpdatePeronalInfoActivity_.intent(getActivity()).start();
		Intent intent = new Intent(getActivity(), AnnotationClassUtil.get(UpdatePeronalInfoActivity.class));
		getActivity().startActivity(intent);
	}

	/**
	 * 请求方案
	 */
	@Click
	protected void ll_proposal_request() {
//		ProposalRequestPublishActivity_.intent(getActivity()).start();
		Intent intent = new Intent(getActivity(), AnnotationClassUtil.get(ProposalRequestPublishActivity.class));
		getActivity().startActivity(intent);
	}

	/**
	 * 我的方案
	 */
	@Click
	protected void ll_my_proposal() {
//		MyProposalActivity_.intent(getActivity()).start();
		Intent intent = new Intent(getActivity(), AnnotationClassUtil.get(MyProposalActivity.class));
		getActivity().startActivity(intent);
	}

	/**
	 * 我的好友
	 */
	@Click
	protected void ll_my_friends() {
		Intent intent = new Intent(getActivity(), AnnotationClassUtil.get(MyFriends.class));
		getActivity().startActivity(intent);
		
	}

	/**
	 * 我的消息
	 */
	@Click
	protected void ll_my_infos() {
//		MyInformationsActivity_.intent(getActivity())
//				.extra("userId", TApplication.user.getUid()).start();
		Intent intent = new Intent(getActivity(), AnnotationClassUtil.get(MyInformationsActivity.class));
		intent.putExtra("userId", TApplication.user.getUid());
		intent.putExtra("userType", 3);
		getActivity().startActivity(intent);

//		MyInformationsActivity_.intent(getActivity())
//				.extra("userId", TApplication.user.getUid())
//				.extra("userType", 3).start();
	}

	/**
	 * 退出
	 */
	@Click
	protected void btn_logout() {
		SPUtil.getUtil(MeConsumerFragment.this.getActivity()).saveToSp(
				SPUtil.PWD, "");
		Intent intent = new Intent(this.getActivity(), Login.class);
		intent.putExtra("logout", "1");
		startActivity(intent);
//		LoginActivity_.intent(getActivity()).extra("logout", "1").start();
		TApplication.getInstance().exitActivities();
	}

}
