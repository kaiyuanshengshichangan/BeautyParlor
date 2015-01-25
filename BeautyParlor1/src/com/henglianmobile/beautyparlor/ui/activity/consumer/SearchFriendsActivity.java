package com.henglianmobile.beautyparlor.ui.activity.consumer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.consumer.SearchFriendsListAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.consumer.SearchFriendsListObject;
import com.henglianmobile.beautyparlor.ui.activity.MeiYouBaseInfoDetailActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 *  搜美友界面
 * @author Administrator
 *
 */ 
@EActivity(R.layout.activity_consumer_search_friends)
public class SearchFriendsActivity extends BaseActivity {
	@ViewById
	protected PullToRefreshListView lv_search_friends_list;
	@ViewById
	protected EditText et_search;
	private ListView lv;
	private SearchFriendsListAdapter adapter;
	private List<SearchFriendsListObject> lists = new ArrayList<SearchFriendsListObject>();
	private int curPage = 1;
	private String keyWord="";
	
	@ViewById
	protected Button btn_search;
	
	@AfterViews 
	protected void setView(){
		lv_search_friends_list.setMode(Mode.BOTH);
		lv_search_friends_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						lv_search_friends_list.setMode(Mode.BOTH);
						lists.clear();
						curPage = 1;
						getData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 刷新完成
						lv_search_friends_list.setMode(Mode.BOTH);
						curPage++;
						getData();
					}
				});
		lv = lv_search_friends_list.getRefreshableView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.i("info", "position=" + position);
				SearchFriendsListObject friendsListObject = lists
						.get(position - 1);
				int userId = friendsListObject.getDnUserid();
//				MeiYouBaseInfoDetailActivity_.intent(SearchFriendsActivity.this).extra("id", userId).start();
				Intent intent = new Intent(SearchFriendsActivity.this, AnnotationClassUtil.get(MeiYouBaseInfoDetailActivity.class));
				intent.putExtra("id", userId);
				SearchFriendsActivity.this.startActivity(intent);
			}
		});
		lists.clear();
		curPage = 1;
		getData();
	}
	private void getData() {
		String url = Const.SEARCHFRIENDSURL+"type=0&isChecked=1" + "&page=" + curPage + "&rows="
				+ Const.PAGEROWS + "&keyWord="+keyWord;
		getHttpData(url);
	}
	private void getHttpData(String url) {
		LogUtil.i("url", "SearchFriendsActivity---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "SearchFriendsActivity----res=" + arg2);
				Tools.showMsg(SearchFriendsActivity.this,
						Tools.HTTP_ERROR);
				lv_search_friends_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "SearchFriendsActivity----res="
							+ responseString);
					Type type = new TypeToken<List<SearchFriendsListObject>>() {
					}.getType();
					List<SearchFriendsListObject> friendsListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (friendsListObjects != null) {
						for (int i = 0; i < friendsListObjects.size(); i++) {
							lists.add(friendsListObjects.get(i));
						}
						if (friendsListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(
										SearchFriendsActivity.this,
										Tools.LOAD_ALL);
							}
						} else {
							lv_search_friends_list.setMode(Mode.BOTH);
							// pageNum = pageNum + 1;
						}
						if (adapter == null) {
							adapter = new SearchFriendsListAdapter(
									SearchFriendsActivity.this,
									lists);
							lv.setAdapter(adapter);

						} else {
							// adapter = null;
							// adapter = new HuanyouquanListAdapter(
							// HuanyouquanFragment.this.getActivity(), lists);
							// lv.setAdapter(adapter);
							adapter.changeData(lists);
						}
					}
					lv_search_friends_list.onRefreshComplete();
				}
			}
		});
	}
	@Click
	protected void btn_back(){
		this.finish();
	}
	@Click
	protected void et_search(){
		if(btn_search.getVisibility() == View.GONE){
			btn_search.setVisibility(View.VISIBLE);
		}else{
			btn_search.setVisibility(View.GONE);
		}
	}
	@Click//有问题============================================================================
	protected void btn_search(){
		keyWord = et_search.getText().toString().trim();
		lists.clear();
		curPage = 1;
		getData();
	}
	
}
