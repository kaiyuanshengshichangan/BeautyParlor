package com.henglianmobile.beautyparlor.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.henglianmobile.beautyparlor.adapter.beautyparlor.ZiXunListAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.beautyparlor.ZiXunListObject;
import com.henglianmobile.beautyparlor.ui.activity.ZiXunDetailActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
@EFragment(R.layout.fragment_zixun)
public class ZiXunFragment extends Fragment {
	@ViewById
	protected PullToRefreshListView lv_zixun_list;

	private ListView lv;
	private ZiXunListAdapter adapter;
	private List<ZiXunListObject> lists = new ArrayList<ZiXunListObject>();
	private int curPage = 1;
 
	private boolean notAddHeadView = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
 
	@Override  
	public void onDestroy() {
		super.onDestroy();
		lists.clear();
		adapter = null;
		// lv.removeHeaderView(headerVeiw);
		lv = null;
		notAddHeadView = true;
	}

	@AfterViews
	void setView() { 
		if (notAddHeadView) {
			lv_zixun_list.setMode(Mode.BOTH);
			lv_zixun_list
					.setOnRefreshListener(new OnRefreshListener2<ListView>() {

						@Override
						public void onPullDownToRefresh(
								PullToRefreshBase<ListView> refreshView) {
							lv_zixun_list.setMode(Mode.BOTH);
							lists.clear();
							curPage = 1;
							getData();
						}

						@Override
						public void onPullUpToRefresh(
								PullToRefreshBase<ListView> refreshView) {
							// Ë¢ÐÂÍê³É
							lv_zixun_list.setMode(Mode.BOTH);
							curPage++;
							getData();
						}
					});
			lv = lv_zixun_list.getRefreshableView();

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					LogUtil.i("info", "position=" + position);
					ZiXunListObject ziXunListObject = lists
							.get(position - 1);
					int zixunId = ziXunListObject.getId();
					int tType = ziXunListObject.gettType();
//					ZiXunDetailActivity_
//							.intent(ZiXunFragment.this.getActivity())
//							.extra("id", zixunId).extra("tType", tType)
//							.start();
					Intent intent = new Intent(ZiXunFragment.this.getActivity(), AnnotationClassUtil.get(ZiXunDetailActivity.class));
					intent.putExtra("id", zixunId);
					intent.putExtra("tType", tType);
					ZiXunFragment.this.getActivity().startActivity(intent);
				}
			});
			lists.clear();
			curPage = 1;
			getData();
			notAddHeadView = false;
		}
	}

	private void getData() {
		String url = Const.GETZIXUNLISTURL+"userId=0&type=0" + "&page=" + curPage + "&rows="
				+ Const.PAGEROWS;
		getHttpData(url);
	}

	private void getHttpData(String url) {
		LogUtil.i("url", "MeiYouQuanFragment---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MeiYouQuanFragment----res=" + arg2);
				Tools.showMsg(ZiXunFragment.this.getActivity(),
						Tools.HTTP_ERROR);
				lv_zixun_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MeiYouQuanFragment----res="
							+ responseString);
					Type type = new TypeToken<List<ZiXunListObject>>() {
					}.getType();
					List<ZiXunListObject> ziXunListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (ziXunListObjects != null) {
						for (int i = 0; i < ziXunListObjects.size(); i++) {
							lists.add(ziXunListObjects.get(i));
						}
						if (ziXunListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(
										ZiXunFragment.this.getActivity(),
										Tools.LOAD_ALL);
							}
						} else {
							lv_zixun_list.setMode(Mode.BOTH);
							// pageNum = pageNum + 1;
						}
						if (adapter == null) {
							adapter = new ZiXunListAdapter(
									ZiXunFragment.this.getActivity(),
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
					lv_zixun_list.onRefreshComplete();
				}
			}
		});
	}
}
