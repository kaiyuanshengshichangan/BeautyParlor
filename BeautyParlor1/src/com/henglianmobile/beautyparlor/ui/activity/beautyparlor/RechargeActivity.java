package com.henglianmobile.beautyparlor.ui.activity.beautyparlor;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.util.LogUtil;
/**
 * ’Àªß≥‰÷µ
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_beautyparlor_recharge)
public class RechargeActivity extends BaseActivity {
	@ViewById
	protected RadioGroup rg_recharge_type;
	private int payType;

	@AfterViews
	protected void initView(){
		rg_recharge_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rbtn_alipay) {
					// ÷ß∏∂±¶÷ß∏∂
					payType = 0;
				}
//				else if (checkedId == R.id.rbtn_mobile_bank) {
//					// ”‡∂Ó÷ß∏∂
//					payType = 1;
//				}
			}
		});
	}
	
	@Click
	protected void btn_submit(){
		if(payType == 0){
			//÷ß∏∂±¶÷ß∏∂
			LogUtil.i("info", "÷ß∏∂±¶÷ß∏∂");
		}else if(payType == 1){
			//”‡∂Ó÷ß∏∂
			LogUtil.i("info", "”‡∂Ó÷ß∏∂");
		}
	}
	@Click 
	protected void btn_back(){
		this.finish();
	}
}
