package com.henglianmobile.beautyparlor.adapter;

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
import com.henglianmobile.beautyparlor.entity.MeiYouQuanCommentListObject;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MeiYouQuanPinglunAdapter extends BaseAdapter {
	private List<MeiYouQuanCommentListObject> list;
	private LayoutInflater mInflater;

	private void setData(List<MeiYouQuanCommentListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<MeiYouQuanCommentListObject>();
		}
	}

	public MeiYouQuanPinglunAdapter(Context context,
			List<MeiYouQuanCommentListObject> list) {
		this.setData(list);
		this.mInflater = LayoutInflater.from(context);

	}

	public void changeData(List<MeiYouQuanCommentListObject> list) {
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
	public MeiYouQuanCommentListObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).getDnCid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.fragment_meiyouquan_pinglun_list_item, null);
			holder.iv_comment_photo = (ImageView) convertView
					.findViewById(R.id.iv_comment_photo);
			holder.tv_comment_nick = (TextView) convertView
					.findViewById(R.id.tv_comment_nick);
			holder.tv_comment_content = (TextView) convertView
					.findViewById(R.id.tv_comment_content);
			holder.tv_comment_time = (TextView) convertView
					.findViewById(R.id.tv_comment_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MeiYouQuanCommentListObject meiYouQuanCommentListObject = list.get(position);
		if (meiYouQuanCommentListObject != null) {
			holder.tv_comment_nick.setText(meiYouQuanCommentListObject.getDcFaName());
			holder.tv_comment_content.setText(meiYouQuanCommentListObject.getDcComment());
			holder.tv_comment_time.setText(Tools.DateStrToDateStr(meiYouQuanCommentListObject.getDtAddTime()));
			String photoPath = meiYouQuanCommentListObject.getDcHeadImg();
			ImageLoader.getInstance().displayImage(photoPath,
					holder.iv_comment_photo, TApplication.optionsImage,
					new MyAnimateFirstDisplayListener());
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView iv_comment_photo;
		private TextView tv_comment_nick, tv_comment_content, tv_comment_time;
	}
}
