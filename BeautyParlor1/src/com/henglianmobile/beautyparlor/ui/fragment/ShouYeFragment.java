package com.henglianmobile.beautyparlor.ui.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.ui.activity.beautyparlor.GuangGaoPublishActivity;
import com.henglianmobile.beautyparlor.ui.activity.consumer.MyProposalActivity;
import com.henglianmobile.beautyparlor.ui.activity.consumer.ProposalRequestPublishActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Constant;
@EFragment(R.layout.fragment_shouye)
public class ShouYeFragment extends Fragment {

	@ViewById
	protected LinearLayout ll_left;
	@ViewById
	protected ImageView iv_left;
	 
	private String userType;
	@AfterViews
	protected void setView(){
		userType = TApplication.user.getUtype();
		if(userType.equals(Constant.CONSUMER)){
			iv_left.setImageResource(R.drawable.iv_shouye_meirongzhaobiao);
		}else if(userType.equals(Constant.BEAUTYPARLOR)){
			iv_left.setImageResource(R.drawable.iv_shouye_fabuguanggao);
		}
	} 
	@Click  
	protected void ll_left(){
		if(userType.equals(Constant.CONSUMER)){
			//��������ת����������ҳ��
//			ProposalRequestPublishActivity_.intent(this.getActivity()).extra("jumpfromshouye", "1").start();
			Intent intent = new Intent(this.getActivity(), AnnotationClassUtil.get(ProposalRequestPublishActivity.class));
			intent.putExtra("jumpfromshouye", "1");
			this.getActivity().startActivity(intent);
		}else if(userType.equals(Constant.BEAUTYPARLOR)){
			//����Ժ��ת���������ҳ��
//			GuangGaoPublishActivity_.intent(getActivity()).extra("jumpfromshouye", "1").start();
			Intent intent = new Intent(this.getActivity(), AnnotationClassUtil.get(GuangGaoPublishActivity.class));
			intent.putExtra("jumpfromshouye", "1");
			this.getActivity().startActivity(intent);
		}
	}  
	@Click 
	protected void ll_right(){
		if(userType.equals(Constant.CONSUMER)){
			//��������ת
//			MyProposalActivity_.intent(this.getActivity()).extra("jumpfromshouye", "1").start();
			Intent intent = new Intent(this.getActivity(), AnnotationClassUtil.get(MyProposalActivity.class));
			intent.putExtra("jumpfromshouye", "1");
			this.getActivity().startActivity(intent);
		}else if(userType.equals(Constant.BEAUTYPARLOR)){
			//����Ժ��ת
			//�����㲥
			Intent intent = new Intent(Constant.SHOUYEBEAUTYPARLORMYPROPOSAL);
			this.getActivity().sendBroadcast(intent);
		}
	}
}
