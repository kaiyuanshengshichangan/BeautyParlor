package com.henglianmobile.beautyparlor.ui.activity.beautyparlor;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.beautyparlor.ExpenseDetailObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 消费明细界面
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_beautyparlor_expense_detail)
public class ExpenseDetailActivity extends BaseActivity {
	
	@ViewById
	protected TextView tv_start_date, tv_end_date,tv_guanggao_expense,tv_proposal_expense;
	private String startTime = "", endTime = "";
	
	@AfterViews
	protected void initView(){
		getData();
	}
	private void getData() {
		String url = Const.GETEXPENSEDETAILURL + "userId="
				+ TApplication.user.getUid()
				+ "&startTime=" + startTime + "&endTime=" + endTime;
		getHttpData(url);
	}
	private void getHttpData(String url) {
		LogUtil.i("url", "ExpenseDetailActivity---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "ExpenseDetailActivity----res=" + arg2);
				Tools.showMsg(ExpenseDetailActivity.this, Tools.HTTP_ERROR);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "ExpenseDetailActivity----res="
							+ responseString);
					Type type = new TypeToken<List<ExpenseDetailObject>>() {
					}.getType();
					List<ExpenseDetailObject> expenseDetailObjects = TApplication.gson.fromJson(responseString, type);
					if (expenseDetailObjects != null&&expenseDetailObjects.size()==2) {
						ExpenseDetailObject detailObject1 = expenseDetailObjects.get(0);
						ExpenseDetailObject detailObject2 = expenseDetailObjects.get(1);
						if(detailObject1.getType() == 1){
							tv_guanggao_expense.setText(detailObject1.getSumPrice()+"元");
						}
						if(detailObject2.getType() == 2){
							tv_proposal_expense.setText(detailObject2.getSumPrice()+"元");
						}
					}else if (expenseDetailObjects != null&&expenseDetailObjects.size()==1) {
						ExpenseDetailObject detailObject1 = expenseDetailObjects.get(0);
						if(detailObject1.getType() == 1){
							tv_guanggao_expense.setText(detailObject1.getSumPrice()+"元");
							tv_proposal_expense.setText("0.0元");
						}
					}else {
						tv_guanggao_expense.setText("0.0元");
						tv_proposal_expense.setText("0.0元");
					}
				}
			}
		});
	}
	@Click 
	protected void btn_back(){
		this.finish();
	}
	@Click
	protected void tv_start_date() {
		datePicker(tv_start_date);
	}

	@Click
	protected void tv_end_date() {
		datePicker(tv_end_date);
	}

	/**
	 * 显示选择日期对话框到文本框中
	 * 
	 * @param context
	 * @param textView
	 */
	public void datePicker(final TextView textView) {
		Calendar c = Calendar.getInstance();

		// 日期对话框
		DatePickerDialog dialog = // 继承了AlertDialog
		new DatePickerDialog(
				ExpenseDetailActivity.this,
				new OnDateSetListener() {

					// 回调函数
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						textView
								.setText(year
										+ "-"
										+ ((monthOfYear + 1) < 10 ? "0"
												+ (monthOfYear + 1)
												: (monthOfYear + 1))
										+ "-"
										+ (dayOfMonth < 10 ? "0" + dayOfMonth
												: dayOfMonth));
						searchData();
					}
				},// 回调
				c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));

		// 显示对话框
		dialog.show();
	}

	private long lStartTime;
	private long lEndTime;
	private void searchData() {
		startTime = tv_start_date.getText().toString();
		endTime = tv_end_date.getText().toString();
		if(!TextUtils.isEmpty(startTime)){
			startTime = startTime+" 00:00:00";
		}
		if(!TextUtils.isEmpty(endTime)){
			endTime = endTime+" 23:59:59";
		}
		try {
			lStartTime = Tools.StringDateToLong(startTime,"yyyy-MM-dd dd:hh:ss");//毫秒
			lEndTime = Tools.StringDateToLong(endTime,"yyyy-MM-dd dd:hh:ss");//毫秒
			LogUtil.i("info", "startTime="+startTime+",endTime="+endTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!TextUtils.isEmpty(startTime)&&!TextUtils.isEmpty(endTime)){
			if (lStartTime>lEndTime) {
				Tools.showMsg(this, "您选择的结束时间小于开始时间，请重新选择!");
				return;
			}
		}
		getData();
	}
}
