package com.henglianmobile.beautyparlor.ui.activity.saleman;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.saleman.MyBeautyParlorAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.saleman.MyBeautyParlorListObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 业务员-我的提成列表
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_saleman_my_commission)
public class MyCommissionActivity extends BaseActivity {
	@ViewById
	protected EditText et_search;
	@ViewById
	protected TextView tv_start_date, tv_end_date;
	@ViewById
	protected Button btn_search;
	@ViewById
	protected PullToRefreshListView lv_my_beautyparlor_list;

	private ListView lv;
	private MyBeautyParlorAdapter adapter;
	private List<MyBeautyParlorListObject> lists = new ArrayList<MyBeautyParlorListObject>();
	private int curPage = 1;
	private int tag = Constant.COMMISSIONTAG;

	private String shopName = "", startTime = "", endTime = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lists.clear();
		curPage = 1;
		getData();
		// getBaseData();
	}

	@AfterViews
	void setView() {
		lv_my_beautyparlor_list.setMode(Mode.BOTH);
		lv_my_beautyparlor_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						lv_my_beautyparlor_list.setMode(Mode.BOTH);
						lists.clear();
						curPage = 1;
						getData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 刷新完成
						lv_my_beautyparlor_list.setMode(Mode.BOTH);
						curPage++;
						getData();
					}
				});
		lv = lv_my_beautyparlor_list.getRefreshableView();
	}

	private void getData() {
		String url = Const.GETMYBEAUTYPARLORURL + "userId="
				+ TApplication.user.getUid() + "&shopName=" + shopName
				+ "&startTime=" + startTime + "&endTime=" + endTime + "&page="
				+ curPage + "&rows=" + Const.PAGEROWS;
		getHttpData(url);
	}

	private void getHttpData(String url) {
		LogUtil.i("url", "MyBeautyParlorActivity---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MyBeautyParlorActivity----res=" + arg2);
				Tools.showMsg(MyCommissionActivity.this, Tools.HTTP_ERROR);
				lv_my_beautyparlor_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MyBeautyParlorActivity----res="
							+ responseString);
					Type type = new TypeToken<List<MyBeautyParlorListObject>>() {
					}.getType();
					List<MyBeautyParlorListObject> myBeautyParlorListObjects = TApplication
							.getInstance().gson.fromJson(responseString, type);
					if (myBeautyParlorListObjects != null) {
						for (int i = 0; i < myBeautyParlorListObjects.size(); i++) {
							lists.add(myBeautyParlorListObjects.get(i));
						}
						if (myBeautyParlorListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(MyCommissionActivity.this,
										Tools.LOAD_ALL);
							}
						} else {
							lv_my_beautyparlor_list.setMode(Mode.BOTH);
						}
						if (adapter == null) {
							adapter = new MyBeautyParlorAdapter(
									MyCommissionActivity.this, lists, tag);
							lv.setAdapter(adapter);

						} else {
							adapter.changeData(lists);
						}
					}
					lv_my_beautyparlor_list.onRefreshComplete();
				}
			}
		});
	}

	@Click
	protected void btn_back() {
		this.finish();
	}

	@Click
	protected void et_search() {
		if (btn_search.getVisibility() == View.GONE) {
			btn_search.setVisibility(View.VISIBLE);
		} else {
			btn_search.setVisibility(View.GONE);
		}
	}

	@Click
	protected void btn_search() {
		shopName = et_search.getText().toString().trim();
		lists.clear();
		curPage = 1;
		getData();
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
				MyCommissionActivity.this,
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
		lists.clear();
		curPage = 1;
		getData();
	}
}
