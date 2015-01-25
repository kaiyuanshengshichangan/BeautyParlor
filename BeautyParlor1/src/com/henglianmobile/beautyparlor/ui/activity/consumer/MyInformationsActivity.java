package com.henglianmobile.beautyparlor.ui.activity.consumer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.beautyparlor.MyInfomationListAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.beautyparlor.MyInfomationsListObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * 我的消息
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_consumer_myinformations)
public class MyInformationsActivity extends BaseActivity {
	@ViewById
	protected PullToRefreshListView lv_myinformations_list;

	private ListView lv;
	private MyInfomationListAdapter adapter;
	private List<MyInfomationsListObject> lists = new ArrayList<MyInfomationsListObject>();
	private int curPage = 1;
	@Extra
	protected String userId;
	@Extra
	protected int userType;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			lists.clear();
			curPage = 1;
			getData();
		};
	};
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
		lv_myinformations_list.setMode(Mode.BOTH);
		lv_myinformations_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						lv_myinformations_list.setMode(Mode.BOTH);
						lists.clear();
						curPage = 1;
						getData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 刷新完成
						lv_myinformations_list.setMode(Mode.BOTH);
						curPage++;
						getData();
					}
				});
		lv = lv_myinformations_list.getRefreshableView();
	}
	private void getData() {
		String url = Const.GETINFORMATIONURL2 
				+ "userId=" + userId
				+ "&userType="+userType
				+ "&keyWord="
				+ "&page="+curPage+"&rows="+Const.PAGEROWS;
		getHttpData(url);
	}
	private void getHttpData(String url) {
		LogUtil.i("url", "MyInformationsActivity---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MyInformationsActivity----res=" + arg2);
				Tools.showMsg(MyInformationsActivity.this,
						Tools.HTTP_ERROR);
				lv_myinformations_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MeiYouQuanFragment----res="
							+ responseString);
					Type type = new TypeToken<List<MyInfomationsListObject>>() {
					}.getType();
					List<MyInfomationsListObject> myInfomationsListObjects = TApplication
							.getInstance().gson.fromJson(responseString, type);
					if (myInfomationsListObjects != null) {
						for (int i = 0; i < myInfomationsListObjects.size(); i++) {
							lists.add(myInfomationsListObjects.get(i));
						}
						if (myInfomationsListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(
										MyInformationsActivity.this,
										Tools.LOAD_ALL);
							}
						} else {
							lv_myinformations_list.setMode(Mode.BOTH);
						}
						if (adapter == null) {
							adapter = new MyInfomationListAdapter(
									MyInformationsActivity.this,
									lists,handler);
							lv.setAdapter(adapter);

						} else {
							adapter.changeData(lists);
						}
					}
					lv_myinformations_list.onRefreshComplete();
				}
			}
		});
	}
	@Click
	protected void btn_back(){
		this.finish();
	}
}
