package com.henglianmobile.beautyparlor.adapter.beautyparlor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.beautyparlor.MyInfomationsListObject;
import com.henglianmobile.beautyparlor.ui.activity.MeiYouBaseInfoDetailActivity_;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyInfomationListAdapter extends BaseAdapter {

	private Context context;
	private List<MyInfomationsListObject> list;
	private LayoutInflater mInflater;
	private Handler handler;

	private void setData(List<MyInfomationsListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<MyInfomationsListObject>();
		}
	}

	public MyInfomationListAdapter(Context context,
			List<MyInfomationsListObject> list, Handler handler) {
		this.context = context;
		this.setData(list);
		this.mInflater = LayoutInflater.from(this.context);
		this.handler = handler;
	}

	public void changeData(List<MyInfomationsListObject> list) {
		if (list != null) {
			LogUtil.i("info", "list.size====" + list.size());
			this.setData(list);
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public MyInfomationsListObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.fragment_beautypralor_my_information_list_item,
					null);
			holder.iv_info_photo = (ImageView) convertView
					.findViewById(R.id.iv_info_photo);
			holder.tv_info_title = (TextView) convertView
					.findViewById(R.id.tv_info_title);
			holder.tv_info_nickname = (TextView) convertView
					.findViewById(R.id.tv_info_nickname);
			holder.tv_publish_time = (TextView) convertView
					.findViewById(R.id.tv_publish_time);
			holder.tv_agree = (TextView) convertView
					.findViewById(R.id.tv_agree);
			holder.tv_have_agreed = (TextView) convertView
					.findViewById(R.id.tv_have_agreed);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			resetViewHolder(holder);
		}
		MyInfomationsListObject myInfomationsListObject = list.get(position);
		LogUtil.i("info", "id============================");
		if (myInfomationsListObject != null) {
			String type = myInfomationsListObject.getType();
			if (type.equals(Constant.INFO_FANGAN)) {
				holder.tv_info_nickname.setText(myInfomationsListObject
						.getDcNickName());
				holder.tv_info_title
						.setText(myInfomationsListObject.getTitle());
			} else if (type.equals(Constant.INFO_FRIENDS)) {
				holder.tv_info_title.setText(myInfomationsListObject
						.getDcNickName() + "请求添加您为好友");
				int isAgree = myInfomationsListObject.getIsAgree();
				if (isAgree == 1) {
					holder.tv_agree.setVisibility(View.GONE);
					holder.tv_have_agreed.setVisibility(View.VISIBLE);
				} else if (isAgree == 0) {
					holder.tv_agree.setVisibility(View.VISIBLE);
					holder.tv_have_agreed.setVisibility(View.GONE);
				}
				int id = myInfomationsListObject.getId();
				LogUtil.i("info", "id=" + id + ",isAgree=" + isAgree);
				holder.tv_agree.setTag(id);
				holder.tv_agree.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int id = (Integer) v.getTag();
						// 同意添加好友
						String url = Const.DEALFRIENDSREQUESTURL + "fid=" + id
								+ "&isAgree=1";
						dealFriendsRequest(url);
					}
				});
				int userId = myInfomationsListObject.getDnUserid();
				holder.iv_info_photo.setTag(userId);
				holder.iv_info_photo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int userId = (Integer) v.getTag();
						MeiYouBaseInfoDetailActivity_.intent(context)
								.extra("id", userId).extra("friend", 1).start();
					}
				});
			} else if (type.equals(Constant.INFO_SYSTEM)) {
				holder.tv_info_nickname.setText("系统消息");
				holder.tv_info_title
						.setText(myInfomationsListObject.getTitle());
			}
			holder.tv_publish_time.setText(Tools
					.DateStrToDateStr(myInfomationsListObject.getDtAddTime()));
			// 获取图片
			String picPath = myInfomationsListObject.getDcHeadImg();
			ImageLoader.getInstance().displayImage(picPath,
					holder.iv_info_photo, TApplication.optionsImage,
					new MyAnimateFirstDisplayListener());
		}
		return convertView;
	}

	protected void dealFriendsRequest(String url) {
		LogUtil.i("url", "MyInfomationListAdapter-dealFriendsRequest-url="
				+ url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (statusCode == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = jsonObject.getInt("response");
						if (response == 0) {
							Tools.showMsg(context, "网络请求失败，请稍后再试!");
						} else if (response == 1) {
							// 发送消息
							handler.sendEmptyMessage(0);
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
				Tools.showMsg(context, Tools.HTTP_ERROR);
			}
		});
	}

	class ViewHolder {
		private TextView tv_info_title, tv_info_nickname, tv_publish_time,
				tv_agree, tv_have_agreed;
		private ImageView iv_info_photo;
	}
	/**
	 * 
	 * @param vh
	 */
	private void resetViewHolder(ViewHolder vh) {
		vh.tv_agree.setVisibility(View.GONE);
		vh.tv_have_agreed.setVisibility(View.GONE);
	}
}
