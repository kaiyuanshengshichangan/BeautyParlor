package com.henglianmobile.beautyparlor.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.Login;
import com.henglianmobile.beautyparlor.adapter.beautyparlor.MyGuangGaoListAdapter;
import com.henglianmobile.beautyparlor.adapter.beautyparlor.MyInfomationListAdapter;
import com.henglianmobile.beautyparlor.adapter.beautyparlor.MyProposalListAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.UserInfoDetailObject;
import com.henglianmobile.beautyparlor.entity.beautyparlor.MyGuangGaoListObject;
import com.henglianmobile.beautyparlor.entity.beautyparlor.MyInfomationsListObject;
import com.henglianmobile.beautyparlor.entity.beautyparlor.MyProposalListObject;
import com.henglianmobile.beautyparlor.ui.activity.beautyparlor.ExpenseDetailActivity;
import com.henglianmobile.beautyparlor.ui.activity.beautyparlor.GuangGaoPublishActivity;
import com.henglianmobile.beautyparlor.ui.activity.beautyparlor.MyGuangGaoDetailActivity;
import com.henglianmobile.beautyparlor.ui.activity.beautyparlor.MyProposalDetailActivity;
import com.henglianmobile.beautyparlor.ui.activity.beautyparlor.ProposalRequestDetailActivity;
import com.henglianmobile.beautyparlor.ui.activity.beautyparlor.RechargeActivity;
import com.henglianmobile.beautyparlor.ui.activity.beautyparlor.UpdateBeautyParlorActivity;
import com.henglianmobile.beautyparlor.util.AnnotationClassUtil;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.SPUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

@EFragment(R.layout.fragment_me_beauty_parlor)
public class MeBeautyParlorFragment extends Fragment {

	@ViewById
	protected ImageView iv_beautyparlor_photo;
	@ViewById
	protected TextView tv_beautyparlor_name, tv_beautyparlor_address,
			tv_beautyparlor_phone, tv_my_information, tv_publish_guanggao,
			tv_my_proposal, tv_my_account,tv_account_money;
	@ViewById 
	protected PullToRefreshListView lv_beautyparlor_me_list;
	@ViewById
	protected LinearLayout ll_publish, ll_my_account;

	private MyBeautyParlorFragmentReceiver receiver;
	private ListView lv;
	private int curPage = 1, meTag = 0;
	private List<MyGuangGaoListObject> guangGaoLists = new ArrayList<MyGuangGaoListObject>();
	private MyGuangGaoListAdapter guangGaoListAdapter;
	private List<MyInfomationsListObject> InfomationsListObjects = new ArrayList<MyInfomationsListObject>();
	private MyInfomationListAdapter infomationListAdapter;
	private List<MyProposalListObject> proposalListObjects = new ArrayList<MyProposalListObject>();
	private MyProposalListAdapter proposalListAdapter;

	private LayoutParams params; 

	private UserInfoDetailObject userInfoDetailObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		receiver = new MyBeautyParlorFragmentReceiver();
		IntentFilter filter = new IntentFilter(
				Constant.SHOUYEBEAUTYPARLORMYPROPOSALUPDATE);
		this.getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onResume() {
		super.onResume();
		setBaseData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		guangGaoListAdapter = null;
		infomationListAdapter = null;
		proposalListAdapter = null;
		lv = null;
		this.getActivity().unregisterReceiver(receiver);
	}


	private void setBaseData() {
		tv_beautyparlor_name.setText(TApplication.getInstance().userInfoDetailObject
				.getDcNickName());
		tv_beautyparlor_address.setText(TApplication.getInstance().userInfoDetailObject
				.getAddress());
		tv_beautyparlor_phone.setText(TApplication.getInstance().userInfoDetailObject
				.getDcPhone());
		String photoPath = TApplication.getInstance().userInfoDetailObject.getDcHeadImg();
		ImageLoader.getInstance().displayImage(photoPath,
				iv_beautyparlor_photo, TApplication.optionsImage,
				new MyAnimateFirstDisplayListener());
	}

	@AfterViews
	void setView() {

		lv_beautyparlor_me_list.setMode(Mode.BOTH);
		lv_beautyparlor_me_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						lv_beautyparlor_me_list.setMode(Mode.BOTH);
						guangGaoLists.clear();
						InfomationsListObjects.clear();
						proposalListObjects.clear();
						guangGaoListAdapter = null;
						infomationListAdapter = null;
						proposalListAdapter = null;
						curPage = 1;
						if (meTag == 0) {// 我的消息
							getInfomation();
						} else if (meTag == 1) {// 我的广告
							getGuangGao();
						} else if (meTag == 2) {// 我的方案
							getMyProposal();
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 刷新完成
						lv_beautyparlor_me_list.setMode(Mode.BOTH);
						curPage++;
						if (meTag == 0) {// 我的消息
							getInfomation();
						} else if (meTag == 1) {// 我的广告
							getGuangGao();
						} else if (meTag == 2) {// 我的方案
							getMyProposal();
						}
					}
				});
		lv = lv_beautyparlor_me_list.getRefreshableView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (meTag == 0) {// 我的消息
					MyInfomationsListObject myInfomationsListObject = InfomationsListObjects
							.get(position - 1);
					String type = myInfomationsListObject.getType();
					if (type.equals(Constant.INFO_FANGAN)) {
						int infoId = myInfomationsListObject.getId();
//						ProposalRequestDetailActivity_
//								.intent(MeBeautyParlorFragment.this
//										.getActivity()).extra("id", infoId)
//								.start();
						Intent intent = new Intent(MeBeautyParlorFragment.this.getActivity(), AnnotationClassUtil.get(ProposalRequestDetailActivity.class));
						intent.putExtra("id", infoId);
						MeBeautyParlorFragment.this.getActivity().startActivity(intent);
					}
				} else if (meTag == 1) {// 我的广告
					MyGuangGaoListObject myGuangGaoListObject = guangGaoLists
							.get(position - 1);
					int gId = myGuangGaoListObject.getDnAdid();
					int tType = myGuangGaoListObject.gettType();
//					MyGuangGaoDetailActivity_
//							.intent(MeBeautyParlorFragment.this.getActivity())
//							.extra("gId", gId).extra("tType", tType)
//							.start();
					Intent intent = new Intent(MeBeautyParlorFragment.this.getActivity(), AnnotationClassUtil.get(MyGuangGaoDetailActivity.class));
					intent.putExtra("gId", gId);
					intent.putExtra("tType", tType);
					MeBeautyParlorFragment.this.getActivity().startActivity(intent);
				} else if (meTag == 2) {// 我的方案
					MyProposalListObject myProposalListObject = proposalListObjects.get(position-1);
					int pId = myProposalListObject.getDnPid();
//					MyProposalDetailActivity_.intent(MeBeautyParlorFragment.this.getActivity())
//					.extra("pId", pId).start();
					
					Intent intent = new Intent(MeBeautyParlorFragment.this.getActivity(), AnnotationClassUtil.get(MyProposalDetailActivity.class));
					intent.putExtra("pId", pId);
					MeBeautyParlorFragment.this.getActivity().startActivity(intent);
				}
			}
		});
		tv_my_information();
	}

	// 修改美容院信息
	@Click
	protected void iv_beautyparlor_info_edit() {
//		UpdateBeautyParlorActivity_.intent(getActivity()).start();
		Intent intent = new Intent(MeBeautyParlorFragment.this.getActivity(), AnnotationClassUtil.get(UpdateBeautyParlorActivity.class));
		MeBeautyParlorFragment.this.getActivity().startActivity(intent);
	}

	@Click
	protected void btn_logout() {
		SPUtil.getUtil(MeBeautyParlorFragment.this.getActivity()).saveToSp(
				SPUtil.PWD, "");
		Intent intent = new Intent(this.getActivity(), Login.class);
		intent.putExtra("logout", "1");
		startActivity(intent);
//		LoginActivity_.intent(getActivity()).extra("logout", "1").start();
		TApplication.getInstance().exitActivities();
	}

	/**
	 * 我的消息
	 */
	@Click
	protected void tv_my_information() {
		btnInit();
		tv_my_information.setBackgroundColor(getResources().getColor(
				R.color.title_background));
		tv_my_information.setTextColor(getResources().getColor(
				R.color.color_fwhite));
		lv_beautyparlor_me_list.setVisibility(View.VISIBLE);
		meTag = 0;
		getInfomation();
	}

	private void getInfomation() {
		String url = Const.GETINFOMATIONSURL 
				+ "uid=" + TApplication.user.getUid() 
				+ "&page=" + curPage 
				+ "&rows=" + Const.PAGEROWS 
				+ "&userType=1" 
				+ "&keyword=";
		getInfomationListHttp(url);
	}

	/**
	 * 发布广告
	 */
	@Click
	protected void tv_publish_guanggao() {
		btnInit();

		ll_my_account.setVisibility(View.GONE);
		ll_publish.setVisibility(View.VISIBLE);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.49f);
		lv_beautyparlor_me_list.setLayoutParams(params);
		tv_publish_guanggao.setBackgroundColor(getResources().getColor(
				R.color.title_background));
		tv_publish_guanggao.setTextColor(getResources().getColor(
				R.color.color_fwhite));
		// 获取我的广告列表
		lv_beautyparlor_me_list.setVisibility(View.VISIBLE);
		meTag = 1;
		getGuangGao();
	}

	private void getGuangGao() {
		String url = Const.GETMYGUANGGAOLISTURL + "uid="
				+ TApplication.user.getUid() + "&page=" + curPage + "&rows="
				+ Const.PAGEROWS;
		getGuangGaoListHttp(url);
	}

	/**
	 * 我的方案
	 */
	@Click
	protected void tv_my_proposal() {
		btnInit();
		tv_my_proposal.setBackgroundColor(getResources().getColor(
				R.color.title_background));
		tv_my_proposal.setTextColor(getResources().getColor(
				R.color.color_fwhite));
		lv_beautyparlor_me_list.setVisibility(View.VISIBLE);
		meTag = 2;
		getMyProposal();
	}

	private void getMyProposal() {
		String url = Const.GETPROPOSALLISTDETAIL 
				+ "uid=" + TApplication.user.getUid() 
				+ "&toId=1&types=0"
				+ "&page=" + curPage + "&rows="
				+ Const.PAGEROWS + "&keyWord=";
		getMyProposalListHttp(url);
	}

	/**
	 * 我的账户
	 */
	@Click
	protected void tv_my_account() {
		btnInit();
		tv_my_account.setBackgroundColor(getResources().getColor(
				R.color.title_background));
		tv_my_account.setTextColor(getResources()
				.getColor(R.color.color_fwhite));
		ll_my_account.setVisibility(View.VISIBLE);
		//获取当前余额
		String url = Const.GETBALANCEURL+TApplication.user.getUid();
		getBalanceHttp(url);
	}

	private void getBalanceHttp(String url) {
		LogUtil.i("url", "MeiYouQuanFragment---getBalanceHttp=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MeiYouQuanFragment---getBalanceHttp-res=" + arg2);
				Tools.showMsg(MeBeautyParlorFragment.this.getActivity(),
						Tools.HTTP_ERROR);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
//				if (statusCode == 200) {
//					LogUtil.i("res", "MeBeautyParlorFragment---getBalanceHttp-res="
//							+ responseString);
//					Type type = new TypeToken<List<BalanceObject>>() {
//					}.getType();
//					List<BalanceObject> balanceObjects = TApplication.gson
//							.fromJson(responseString, type);
//					if (balanceObjects != null&&balanceObjects.size()!=0) {
//						BalanceObject balanceObject = balanceObjects.get(0);
//						tv_account_money.setText(balanceObject.getAmount()+"元");
//					}
//				} 
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						double response = jsonObject.getDouble("response");
//						int response = Integer.parseInt(jsonObject.getString("response"));
						tv_account_money.setText(response+"元");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
	}

	/**
	 * 发布广告-发布
	 */
	@Click
	protected void tv_publish() {
//		GuangGaoPublishActivity_.intent(getActivity()).start();
		Intent intent = new Intent(MeBeautyParlorFragment.this.getActivity(), AnnotationClassUtil.get(GuangGaoPublishActivity.class));
		MeBeautyParlorFragment.this.getActivity().startActivity(intent);
	}

	// ,,
	/**
	 * 消费明细
	 */
	@Click
	protected void ll_expense_details() {
//		ExpenseDetailActivity_.intent(getActivity()).start();
		Intent intent = new Intent(MeBeautyParlorFragment.this.getActivity(), AnnotationClassUtil.get(ExpenseDetailActivity.class));
		MeBeautyParlorFragment.this.getActivity().startActivity(intent);
	}

	/**
	 * 账户充值
	 */
	@Click
	protected void ll_recharge() {
//		RechargeActivity_.intent(getActivity()).start();
		Intent intent = new Intent(MeBeautyParlorFragment.this.getActivity(), AnnotationClassUtil.get(RechargeActivity.class));
		MeBeautyParlorFragment.this.getActivity().startActivity(intent);
	}

	/**
	 *获取我的方案列表
	 * @param url
	 */
	private void getMyProposalListHttp(String url) {
		LogUtil.i("url", "MeiYouQuanFragment---getMyProposalListHttp=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MeiYouQuanFragment---getMyProposalListHttp-res=" + arg2);
				Tools.showMsg(MeBeautyParlorFragment.this.getActivity(),
						Tools.HTTP_ERROR);
				lv_beautyparlor_me_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MeBeautyParlorFragment---getMyProposalListHttp-res="
							+ responseString);
					Type type = new TypeToken<List<MyProposalListObject>>() {
					}.getType();
					List<MyProposalListObject> myProposalListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (myProposalListObjects != null) {
						for (int i = 0; i < myProposalListObjects.size(); i++) {
							proposalListObjects.add(myProposalListObjects
									.get(i));
						}
						if (myProposalListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(MeBeautyParlorFragment.this
										.getActivity(), Tools.LOAD_ALL);
							}
						} else {
							lv_beautyparlor_me_list.setMode(Mode.BOTH);
							// pageNum = pageNum + 1;
						}
						if (proposalListAdapter == null) {
							proposalListAdapter = new MyProposalListAdapter(
									MeBeautyParlorFragment.this.getActivity(),
									proposalListObjects);
							lv.setAdapter(proposalListAdapter);

						} else {
							proposalListAdapter
									.changeData(proposalListObjects);
						}
					}
					lv_beautyparlor_me_list.onRefreshComplete();
				}
			}
		});
	}

	/**
	 * 获取我的消息列表
	 * @param url
	 */
	private void getInfomationListHttp(String url) {
		LogUtil.i("url", "MeiYouQuanFragment---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MeiYouQuanFragment----res=" + arg2);
				Tools.showMsg(MeBeautyParlorFragment.this.getActivity(),
						Tools.HTTP_ERROR);
				lv_beautyparlor_me_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MeBeautyParlorFragment----res="
							+ responseString);
					Type type = new TypeToken<List<MyInfomationsListObject>>() {
					}.getType();
					List<MyInfomationsListObject> myInfomationsListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (myInfomationsListObjects != null) {
						for (int i = 0; i < myInfomationsListObjects.size(); i++) {
							InfomationsListObjects.add(myInfomationsListObjects
									.get(i));
						}
						if (myInfomationsListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(MeBeautyParlorFragment.this
										.getActivity(), Tools.LOAD_ALL);
							}
						} else {
							lv_beautyparlor_me_list.setMode(Mode.BOTH);
							// pageNum = pageNum + 1;
						}
						if (infomationListAdapter == null) {
							infomationListAdapter = new MyInfomationListAdapter(
									MeBeautyParlorFragment.this.getActivity(),
									InfomationsListObjects,handler);
							lv.setAdapter(infomationListAdapter);

						} else {
							infomationListAdapter
									.changeData(InfomationsListObjects);
						}
					}
					lv_beautyparlor_me_list.onRefreshComplete();
				}
			}
		});
	}

	/**
	 * 获取广告列表
	 * @param url
	 */
	private void getGuangGaoListHttp(String url) {
		LogUtil.i("url", "MeiYouQuanFragment---url=" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LogUtil.i("res", "MeiYouQuanFragment----res=" + arg2);
				Tools.showMsg(MeBeautyParlorFragment.this.getActivity(),
						Tools.HTTP_ERROR);
				lv_beautyparlor_me_list.onRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("res", "MeBeautyParlorFragment----res="
							+ responseString);
					Type type = new TypeToken<List<MyGuangGaoListObject>>() {
					}.getType();
					List<MyGuangGaoListObject> myGuangGaoListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (myGuangGaoListObjects != null) {
						for (int i = 0; i < myGuangGaoListObjects.size(); i++) {
							guangGaoLists.add(myGuangGaoListObjects.get(i));
						}
						if (myGuangGaoListObjects.size() < Const.PAGEROWS) {
							if (curPage != 1) {
								Tools.showMsg(MeBeautyParlorFragment.this
										.getActivity(), Tools.LOAD_ALL);
							}
						} else {
							lv_beautyparlor_me_list.setMode(Mode.BOTH);
							// pageNum = pageNum + 1;
						}
						if (guangGaoListAdapter == null) {
							guangGaoListAdapter = new MyGuangGaoListAdapter(
									MeBeautyParlorFragment.this.getActivity(),
									guangGaoLists);
							lv.setAdapter(guangGaoListAdapter);

						} else {
							// adapter = null;
							// adapter = new HuanyouquanListAdapter(
							// HuanyouquanFragment.this.getActivity(),
							// guangGaoLists);
							// lv.setAdapter(adapter);
							guangGaoListAdapter.changeData(guangGaoLists);
						}
					}
					lv_beautyparlor_me_list.onRefreshComplete();
				}
			}
		});
	}

	private void btnInit() {
		// tv_my_information, tv_publish_guanggao,
		// tv_my_proposal, tv_my_account
		tv_my_information.setBackgroundColor(Color.WHITE);
		tv_publish_guanggao.setBackgroundColor(Color.WHITE);
		tv_my_proposal.setBackgroundColor(Color.WHITE);
		tv_my_account.setBackgroundColor(Color.WHITE);
		tv_my_information.setTextColor(Color.BLACK);
		tv_publish_guanggao.setTextColor(Color.BLACK);
		tv_my_proposal.setTextColor(Color.BLACK);
		tv_my_account.setTextColor(Color.BLACK);

		ll_my_account.setVisibility(View.GONE);
		lv_beautyparlor_me_list.setVisibility(View.GONE);
		ll_publish.setVisibility(View.GONE);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.55f);
		lv_beautyparlor_me_list.setLayoutParams(params);
		guangGaoLists.clear();
		if (guangGaoListAdapter != null) {
			guangGaoListAdapter.changeData(guangGaoLists);
			guangGaoListAdapter = null;
		}
		InfomationsListObjects.clear();
		if (infomationListAdapter != null) {
			infomationListAdapter.changeData(InfomationsListObjects);
			infomationListAdapter = null;
		}
		proposalListObjects.clear();
		if (proposalListAdapter != null) {
			proposalListAdapter.changeData(proposalListObjects);
			proposalListAdapter = null;
		}
	}

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			InfomationsListObjects.clear();
			curPage = 1;
			getInfomation();
		};
	};
	private class MyBeautyParlorFragmentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			btnInit();
			tv_my_proposal.setBackgroundColor(getResources().getColor(
					R.color.title_background));
			tv_my_proposal.setTextColor(getResources().getColor(
					R.color.color_fwhite));
			lv_beautyparlor_me_list.setVisibility(View.VISIBLE);
			meTag = 2;
			getMyProposal();
		}
	}
}
