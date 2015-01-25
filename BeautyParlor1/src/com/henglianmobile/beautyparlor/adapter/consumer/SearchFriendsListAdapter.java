package com.henglianmobile.beautyparlor.adapter.consumer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.consumer.SearchFriendsListObject;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SearchFriendsListAdapter extends BaseAdapter {
	private List<SearchFriendsListObject> list;
	private LayoutInflater mInflater;

	private void setData(List<SearchFriendsListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<SearchFriendsListObject>();
		}
	}

	public SearchFriendsListAdapter(Context context,
			List<SearchFriendsListObject> list) {
		this.setData(list);
		this.mInflater = LayoutInflater.from(context);

	}

	public void changeData(List<SearchFriendsListObject> list) {
		if (list != null) {
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
	public SearchFriendsListObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).getDnUserid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.activity_consumer_search_friends_list_item, null);
			holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
			holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SearchFriendsListObject friendsListObject = list.get(position);
		if (friendsListObject != null) {
			holder.tv_nick.setText(friendsListObject.getDcNickName());
			String type = friendsListObject.getDnType()+"";
			if(type.equals(Constant.BEAUTYPARLOR)){
				holder.tv_sign.setText(friendsListObject.getDcContent());
			}else{
				holder.tv_sign.setText(friendsListObject.getDcSign());
			}
			String path = friendsListObject.getDcHeadImg();
			ImageLoader.getInstance().displayImage(path, holder.iv_pic,
					TApplication.optionsImage,
					new MyAnimateFirstDisplayListener());
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView iv_pic;
		private TextView tv_nick, tv_sign;
	}
}
