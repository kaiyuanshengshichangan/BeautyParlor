package com.henglianmobile.beautyparlor.ui.activity.beautyparlor;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.widget.GridView;
import android.widget.TextView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.beautyparlor.ProposalPreviewAdatper;
import com.henglianmobile.beautyparlor.entity.beautyparlor.ProposalDetailObject;
import com.henglianmobile.beautyparlor.util.LogUtil;
/**
 * 方案详情预览
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_beautyparlor_proposal_detail_preview)
public class ProposalDetailPreviewActivity extends BaseActivity {

	@ViewById
	protected TextView tv_title,tv_content,rl_title;
	@ViewById
	protected GridView gv_patient_pics;
	
	@Extra
	protected ProposalDetailObject proposalDetailObject;
	@Extra
	protected ArrayList<String> pics;
	@Extra
	protected String content,title;
	@Extra
	protected int tag;
	
	private ProposalPreviewAdatper adapter;
	private String title1;
	
	@AfterViews
	protected void initView(){
		if(tag == 1){
			rl_title.setText("广告预览");
			title1 = title;
		}else if(tag == 2){
			rl_title.setText("方案预览");
			title1 = "给"+proposalDetailObject.getDcNickName()+"推出的整容"+proposalDetailObject.getTypeName()+"部的方案";
		}
		tv_title.setText(title1);
		LogUtil.i("info", "content="+content);
		tv_content.setText(content);
		
		adapter = new ProposalPreviewAdatper(this, pics);
		gv_patient_pics.setAdapter(adapter);
	}
	@Click
	protected void btn_back(){
		this.finish();
	}
}
