package com.henglianmobile.beautyparlor.adapter.beautyparlor;

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
import com.henglianmobile.beautyparlor.entity.beautyparlor.ZiXunListObject;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZiXunListAdapter extends BaseAdapter {
	private List<ZiXunListObject> list;
	private LayoutInflater mInflater;

	private void setData(List<ZiXunListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<ZiXunListObject>();
		}
	}

	public ZiXunListAdapter(Context context, List<ZiXunListObject> list) {
		this.setData(list);
		this.mInflater = LayoutInflater.from(context);

	}

	public void changeData(List<ZiXunListObject> list) {
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
	public ZiXunListObject getItem(int position) {
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
					R.layout.fragment_zixun_list_item, null);
			holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.tv_publish_time = (TextView) convertView
					.findViewById(R.id.tv_publish_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ZiXunListObject ziXunListObject = list.get(position);
		if (ziXunListObject != null) {
			holder.tv_title.setText(ziXunListObject.getTitle());
			holder.tv_content.setText(ziXunListObject.getDcContent());
			holder.tv_publish_time.setText(Tools
					.DateStrToDateStr(ziXunListObject.getDtAddTime()));
			String path = ziXunListObject.getDcImgPath();
			ImageLoader.getInstance().displayImage(path, holder.iv_pic,
					TApplication.optionsImage,
					new MyAnimateFirstDisplayListener());
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView iv_pic;
		private TextView tv_title, tv_content, tv_publish_time;
	}
}
