package com.henglianmobile.beautyparlor.ui.activity.saleman;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
import com.henglianmobile.beautyparlor.ui.activity.MeiYouBaseInfoDetailActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 业务员-我的美容院列表
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_saleman_my_beautyparlor)
public class MyBeautyParlorActivity extends BaseActivity {
	@ViewById
	protected PullToRefreshListView lv_my_beautyparlor_list;
 
	private ListView lv;
	private MyBeautyParlorAdapter adapter; 
	private List<MyBeautyParlorListObject> lists = new ArrayList<MyBeautyParlorListObject>();
	private int curPage = 1;
	private int tag = Constant.BEAUTYPARLORTAG;
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
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.i("info", "position=" + position);
				MyBeautyParlorListObject myBeautyParlorListObject = lists
						.get(position - 1);
				int userId = myBeautyParlorListObject.getDnUserid();
//				MeiYouBaseInfoDetailActivity_.intent(MyBeautyParlorActivity.this)
//				.extra("id", userId)
//				.extra("friend", 1).start();
				Intent intent = new Intent(MyBeautyParlorActivity.this, AnnotationClassUtil.get(MeiYouBaseInfoDetailActivity.class));
				intent.putExtra("id", userId);
				intent.putExtra("friend", 1);
				MyBeautyParlorActivity.this.startActivity(intent);
			}
		});
	}
	private void getData() {
		String url = Const.GETMYBEAUTYPARLORURL 
				+ "userId=" + TApplication.user.getUid()
				+ "&shopName=&startTime=&endTime="
				+ "&page="+curPage+"&rows="+Const.PAGEROWS;
		getHttpData(url);
	}
	private void getHttpData(String url) {
		LogUtil.i("url", "MyBeautyParlorActivity---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MyBeautyParlorActivity----res=" + arg2);
				Tools.showMsg(MyBeautyParlorActivity.this,
						Tools.HTTP_ERROR);
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
								Tools.showMsg(
										MyBeautyParlorActivity.this,
										Tools.LOAD_ALL);
							}
						} else {
							lv_my_beautyparlor_list.setMode(Mode.BOTH);
						}
						if (adapter == null) {
							adapter = new MyBeautyParlorAdapter(
									MyBeautyParlorActivity.this,
									lists,tag);
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
	protected void btn_back(){
		this.finish();
	}
}
