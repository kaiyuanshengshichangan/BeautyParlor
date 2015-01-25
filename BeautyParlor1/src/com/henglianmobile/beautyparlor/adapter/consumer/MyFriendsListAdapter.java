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
import com.henglianmobile.beautyparlor.entity.consumer.MyFriendsListObject;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyFriendsListAdapter extends BaseAdapter {
	private List<MyFriendsListObject> list;
	private LayoutInflater mInflater;

	private void setData(List<MyFriendsListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<MyFriendsListObject>();
		}
	}

	public MyFriendsListAdapter(Context context,
			List<MyFriendsListObject> list) {
		this.setData(list);
		this.mInflater = LayoutInflater.from(context);

	}

	public void changeData(List<MyFriendsListObject> list) {
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
	public MyFriendsListObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).getDnFid();
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
		MyFriendsListObject myFriendsListObject = list.get(position);
		if (myFriendsListObject != null) {
			holder.tv_nick.setText(myFriendsListObject.getDcNickName());
			String type = myFriendsListObject.getDnType()+"";
			if(type.equals(Constant.BEAUTYPARLOR)){
				holder.tv_sign.setText(myFriendsListObject.getDcContent());
			}else{
				holder.tv_sign.setText(myFriendsListObject.getDcSign());
			}
			String path = myFriendsListObject.getDcHeadImg();
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
