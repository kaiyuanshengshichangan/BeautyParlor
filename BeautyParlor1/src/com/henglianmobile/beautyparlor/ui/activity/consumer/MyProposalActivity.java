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
import com.henglianmobile.beautyparlor.adapter.consumer.MyProposalListAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.consumer.MyProposalListObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * Activity-我的方案
 * @author Administrator
 *
 */
@EActivity(R.layout.activity_consumer_myproposal)
public class MyProposalActivity extends BaseActivity {
	@ViewById
	protected PullToRefreshListView lv_myproposal_list;

	private ListView lv;
	private MyProposalListAdapter adapter;
	private List<MyProposalListObject> lists = new ArrayList<MyProposalListObject>();
	private int curPage = 1;
	@Extra
	protected String jumpfromshouye;
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
		lv_myproposal_list.setMode(Mode.BOTH);
		lv_myproposal_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						lv_myproposal_list.setMode(Mode.BOTH);
						lists.clear();
						curPage = 1;
						getData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 刷新完成
						lv_myproposal_list.setMode(Mode.BOTH);
						curPage++;
						getData();
					}
				});
		lv = lv_myproposal_list.getRefreshableView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.i("info", "position=" + position);
				MyProposalListObject myProposalListObject = lists.get(position - 1);
				int pId = myProposalListObject.getDnPid();
				ProposalDetailActivity_
						.intent(MyProposalActivity.this)
						.extra("pId", pId).start();
			}
		});
	}
	private void getData() {
		String url = Const.GETMYPROPOSALLISTURL + "toUserId=" + TApplication.user.getUid() + "&keyWord="
				+ "&page="+curPage+"&rows="+Const.PAGEROWS;
		getHttpData(url);
	}
	private void getHttpData(String url) {
		LogUtil.i("url", "MeiYouQuanFragment---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MeiYouQuanFragment----res=" + arg2);
				Tools.showMsg(MyProposalActivity.this,
						Tools.HTTP_ERROR);
				lv_myproposal_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MeiYouQuanFragment----res="
							+ responseString);
					Type type = new TypeToken<List<MyProposalListObject>>() {
					}.getType();
					List<MyProposalListObject> myProposalListObjects = TApplication
							.getInstance().gson.fromJson(responseString, type);
					if (myProposalListObjects != null) {
						for (int i = 0; i < myProposalListObjects.size(); i++) {
							lists.add(myProposalListObjects.get(i));
						}
						if (myProposalListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(
										MyProposalActivity.this,
										Tools.LOAD_ALL);
							}
						} else {
							lv_myproposal_list.setMode(Mode.BOTH);
						}
						if (adapter == null) {
							adapter = new MyProposalListAdapter(
									MyProposalActivity.this,
									lists);
							lv.setAdapter(adapter);

						} else {
							adapter.changeData(lists);
						}
					}
					lv_myproposal_list.onRefreshComplete();
				}
			}
		});
	}
	@Click
	protected void btn_back(){
		if("1".equals(jumpfromshouye)){
			//发布广播
			Intent intent = new Intent(Constant.SHOUYETOOTHERACTIVITY);
			sendBroadcast(intent);
		}
		this.finish();
	}
	
	@Click
	protected void btn_publish(){
		ProposalRequestPublishActivity_.intent(this).extra("jumpfromshouye", jumpfromshouye).start();
		this.finish();
	}
}
