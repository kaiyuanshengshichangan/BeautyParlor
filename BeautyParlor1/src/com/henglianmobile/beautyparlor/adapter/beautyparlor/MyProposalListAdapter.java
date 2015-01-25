package com.henglianmobile.beautyparlor.adapter.beautyparlor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.entity.beautyparlor.MyProposalListObject;
import com.henglianmobile.beautyparlor.util.Tools;

public class MyProposalListAdapter extends BaseAdapter {
	private List<MyProposalListObject> list;
	private LayoutInflater mInflater;

	private void setData(List<MyProposalListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<MyProposalListObject>();
		}
	}

	public MyProposalListAdapter(Context context,
			List<MyProposalListObject> list) {
		this.setData(list);
		this.mInflater = LayoutInflater.from(context);

	}

	public void changeData(List<MyProposalListObject> list) {
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
	public MyProposalListObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).getDnPid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.activity_beautyparlor_myproposal_list_item, null);
			holder.tv_proposal_title = (TextView) convertView
					.findViewById(R.id.tv_proposal_title);
			holder.tv_proposal_content = (TextView) convertView
					.findViewById(R.id.tv_proposal_content);
			holder.tv_publish_time = (TextView) convertView
					.findViewById(R.id.tv_publish_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyProposalListObject myProposalListObject = list.get(position);
		if (myProposalListObject != null) {
			String title = "给" + myProposalListObject.getDcUserName() + "推出的整容"
					+ myProposalListObject.getTypeName() + "部的方案";
			holder.tv_proposal_title.setText(title);
			holder.tv_proposal_content.setText(myProposalListObject
					.getDcContent());
			String dateStr = Tools
					.DateStrToDateStr(myProposalListObject.getDtAddTime());
			String date = dateStr.substring(0, 10);
			holder.tv_publish_time.setText(date);
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_proposal_title, tv_proposal_content,
				tv_publish_time;
	}
}
