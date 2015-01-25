package com.henglianmobile.beautyparlor.ui.activity.beautyparlor;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 选择支付方式
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_beautyparlor_pay_type_select)
public class PayTypeSelectActivity extends BaseActivity {
	
	@ViewById
	protected RadioGroup rg_pay_type;
	private int payType;
	private Intent intent;

	@Extra
	protected int expenseType;
	@Extra
	protected float price;
	 
	private String url;
	@AfterViews
	protected void initView(){
		rg_pay_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rbtn_alipay) {
					// 支付宝支付
					payType = 0;
				} else if (checkedId == R.id.rbtn_balance) {
					// 手机银行支付
					payType = 1;
				}
			}
		});
	}
	
	@Click
	protected void btn_submit(){
		if(payType == 0){
			//支付宝支付
			LogUtil.i("info", "支付宝支付");
			Tools.showMsg(this, "暂不支持支付宝支付，请使用余额支付!");
		}else if(payType == 1){
			//余额支付
			LogUtil.i("info", "余额支付");
			url = Const.PAYFROMBALANCEURL 
					+ "userId="+TApplication.user.getUid() 
					+ "&type=" + expenseType 
					+ "&amount=" + price;
			Tools.showProgressDialog(this, "正在支付...");
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					payHttp(url);
				}
			}, 2000);
		}
	}
	/**
	 * 支付
	 * @param url
	 */
	private void payHttp(String url) {
		Tools.closeProgressDialog();
		LogUtil.i("url", "PayTypeSelectActivity--url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = Integer.parseInt(jsonObject.getString("response"));
						if(response == -1){
							Tools.showMsg(PayTypeSelectActivity.this, "余额不足，请及时充值!");
						}else if(response == 0){
							Tools.showMsg(PayTypeSelectActivity.this, "支付失败!");
						}else if(response > 0){
							Tools.showMsg(PayTypeSelectActivity.this, "支付成功!");
							int payId = response;
							if(expenseType == Constant.PUBLISHGUANGGAO){
								//发布广告
								intent = new Intent(Constant.ACTIONPUSHGUANGGAOSUCCESS);
							}else if(expenseType == Constant.PUSHPROPOSAL){
								//推出方案 ，发送广播
								intent = new Intent(Constant.ACTIONPUSHPROPOSALSUCCESS);
							}
							intent.putExtra("payId", payId);
							PayTypeSelectActivity.this.sendBroadcast(intent);
							PayTypeSelectActivity.this.finish();
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
				Tools.showMsg(PayTypeSelectActivity.this, HTTP_ERROR);
			}
		});
	}

	@Click
	protected void btn_back(){
		this.finish();
	}
}
