package com.henglianmobile.beautyparlor.ui.activity.consumer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.consumer.MyFriendsListAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.consumer.MyFriendsListObject;
import com.henglianmobile.beautyparlor.ui.activity.MeiYouBaseInfoDetailActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
/**
 * Activity-我的好友
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_consumer_myfriends)
public class MyFriends extends BaseActivity {
	@ViewById
	protected PullToRefreshListView lv_myfriends_list;
	private ListView lv;
	private MyFriendsListAdapter adapter;
	private List<MyFriendsListObject> lists = new ArrayList<MyFriendsListObject>();
	private int curPage = 1;
	private int friendId;
	
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	   
	@AfterViews 
	protected void setView(){
		lv_myfriends_list.setMode(Mode.BOTH);
		lv_myfriends_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

		 			@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						lv_myfriends_list.setMode(Mode.BOTH);
						lists.clear();
						curPage = 1;
						getData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 刷新完成
						lv_myfriends_list.setMode(Mode.BOTH);
						curPage++;
						getData();
					}
				});
		lv = lv_myfriends_list.getRefreshableView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.i("info", "position=" + position);
				MyFriendsListObject myFriendsListObject = lists
						.get(position - 1);
				int userId = myFriendsListObject.getToUserId();
//				MeiYouBaseInfoDetailActivity_.intent(MyFriends.this)
//				.extra("id", userId)
//				.extra("friend", 1).start();
				Intent intent = new Intent(MyFriends.this, AnnotationClassUtil.get(MeiYouBaseInfoDetailActivity.class));
				intent.putExtra("id", userId);
				intent.putExtra("friend", 1);
				MyFriends.this.startActivity(intent);
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				MyFriendsListObject myFriendsListObject = lists
						.get(position - 1);
				friendId = myFriendsListObject.getDnFid();
				dialog = showDialog();
				dialog.show();
				return true;
			}
		});
		lists.clear();
		curPage = 1;
		getData();
	}
	protected AlertDialog showDialog() {
		builder = new AlertDialog.Builder(this);
		builder.setTitle("你真的要删除该好友吗?");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String url = Const.DELETEFRIENDSURL+friendId;
				deleteFriendHttp(url);
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}
	/**
	 * 删除好友
	 * @param url
	 */
	protected void deleteFriendHttp(String url) {
		LogUtil.i("url", "MyFriends-deleteFriendHttp-url="+url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = Integer.parseInt(jsonObject.getString("response"));
						if(response == 0){
							Tools.showMsg(MyFriends.this, "删除失败!");
						}else if(response == 1){
							Tools.showMsg(MyFriends.this, "删除成功!");
							lists.clear();
							curPage = 1;
							getData();
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
				Tools.showMsg(MyFriends.this, HTTP_ERROR);
			}
		});
	}
	private void getData() {
		String url = Const.GETFRIENDSURL+"uid="+TApplication.user.getUid() + "&page=" + curPage + "&rows="
				+ Const.PAGEROWS + "&toUserName=";
		getHttpData(url);
	}
	private void getHttpData(String url) {
		LogUtil.i("url", "MyFriends---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MyFriends----res=" + arg2);
				Tools.showMsg(MyFriends.this,
						Tools.HTTP_ERROR);
				lv_myfriends_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MyFriends----res="
							+ responseString);
					Type type = new TypeToken<List<MyFriendsListObject>>() {
					}.getType();
					List<MyFriendsListObject> myFriendsListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (myFriendsListObjects != null) {
						for (int i = 0; i < myFriendsListObjects.size(); i++) {
							lists.add(myFriendsListObjects.get(i));
						}
						if (myFriendsListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(
										MyFriends.this,
										Tools.LOAD_ALL);
							}
						} else {
							lv_myfriends_list.setMode(Mode.BOTH);
							// pageNum = pageNum + 1;
						}
						if (adapter == null) {
							adapter = new MyFriendsListAdapter(
									MyFriends.this,
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
					lv_myfriends_list.onRefreshComplete();
				}
			}
		});
	}
	@Click
	protected void btn_back(){
		this.finish();
	}
	@Click
	protected void btn_search_friends(){
		SearchFriendsActivity_.intent(this).start();
	}
}
