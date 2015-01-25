package com.henglianmobile.beautyparlor.adapter.saleman;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.beautyparlor.MyInfomationsListObject;
import com.henglianmobile.beautyparlor.entity.saleman.MyBeautyParlorListObject;
import com.henglianmobile.beautyparlor.ui.activity.MeiYouBaseInfoDetailActivity_;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyBeautyParlorAdapter extends BaseAdapter {

	private Context context;
	private List<MyBeautyParlorListObject> list;
	private LayoutInflater mInflater;
	private int tag;

	private void setData(List<MyBeautyParlorListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<MyBeautyParlorListObject>();
		}
	}

	public MyBeautyParlorAdapter(Context context,
			List<MyBeautyParlorListObject> list, int tag) {
		this.context = context;
		this.setData(list);
		this.mInflater = LayoutInflater.from(this.context);
		this.tag = tag;
	}

	public void changeData(List<MyBeautyParlorListObject> list) {
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
	public MyBeautyParlorListObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.activity_saleman_my_beautypralor_list_item, null);
			holder.iv_photo = (ImageView) convertView
					.findViewById(R.id.iv_photo);
			holder.tv_beautyparlor_name = (TextView) convertView
					.findViewById(R.id.tv_beautyparlor_name);
			holder.tv_beautyparlor_address = (TextView) convertView
					.findViewById(R.id.tv_beautyparlor_address);
			holder.tv_beautyparlor_phone = (TextView) convertView
					.findViewById(R.id.tv_beautyparlor_phone);
			holder.ll_commission = (LinearLayout) convertView
					.findViewById(R.id.ll_commission);
			holder.tv_beautyparlor_proposal_count = (TextView) convertView
					.findViewById(R.id.tv_beautyparlor_proposal_count);
			holder.tv_proposal_commission = (TextView) convertView
					.findViewById(R.id.tv_proposal_commission);
			holder.tv_beautyparlor_guanggao_count = (TextView) convertView
					.findViewById(R.id.tv_beautyparlor_guanggao_count);
			holder.tv_guanggao_commission = (TextView) convertView
					.findViewById(R.id.tv_guanggao_commission);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyBeautyParlorListObject myBeautyParlorListObject = list.get(position);
		LogUtil.i("info", "id============================");
		if (myBeautyParlorListObject != null) {
			holder.tv_beautyparlor_name.setText(myBeautyParlorListObject
					.getDcNickName());
			holder.tv_beautyparlor_address.setText(myBeautyParlorListObject
					.getAddress());
			holder.tv_beautyparlor_phone.setText(myBeautyParlorListObject
					.getDcPhone());
			String headImgPath = myBeautyParlorListObject.getDcHeadImg();
			ImageLoader.getInstance().displayImage(headImgPath,
					holder.iv_photo, TApplication.optionsImage,
					new MyAnimateFirstDisplayListener());
			if (tag == 1) {
				// 显示提成信息
				holder.ll_commission.setVisibility(View.VISIBLE);
				holder.tv_beautyparlor_proposal_count.setText(myBeautyParlorListObject.getProgNum()+"");
				holder.tv_proposal_commission.setText(myBeautyParlorListObject.getSumProgPercent()+"");
				holder.tv_beautyparlor_guanggao_count.setText(myBeautyParlorListObject.getAdNum()+"");
				holder.tv_guanggao_commission.setText(myBeautyParlorListObject.getSumAdPercent()+"");
			}else{
				holder.ll_commission.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_beautyparlor_name, tv_beautyparlor_address,
				tv_beautyparlor_phone, tv_beautyparlor_proposal_count,
				tv_proposal_commission, tv_beautyparlor_guanggao_count,
				tv_guanggao_commission;
		private ImageView iv_photo;
		private LinearLayout ll_commission;
	}
}
