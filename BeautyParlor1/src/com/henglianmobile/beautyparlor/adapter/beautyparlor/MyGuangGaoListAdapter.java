package com.henglianmobile.beautyparlor.adapter.beautyparlor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.adapter.GridViewAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.beautyparlor.MyGuangGaoListObject;
import com.henglianmobile.beautyparlor.ui.activity.ShowPicturesActivity_;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyGuangGaoListAdapter extends BaseAdapter{

	private Context context;
	private List<MyGuangGaoListObject> list;
	private LayoutInflater mInflater;
	
	private void setData(List<MyGuangGaoListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<MyGuangGaoListObject>();
		}
	}

	public MyGuangGaoListAdapter(Context context, List<MyGuangGaoListObject> list) {
		this.context = context;
		this.setData(list);
		this.mInflater = LayoutInflater.from(this.context);

	}

	public void changeData(List<MyGuangGaoListObject> list){
		if(list!=null){
			LogUtil.i("info", "list.size===="+list.size());
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
	public MyGuangGaoListObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).getDnAdid();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.fragment_beautypralor_my_guanggao_list_item, null);
			holder.iv_meiyou_pic = (ImageView) convertView .findViewById(R.id.iv_meiyou_pic);
			holder.iv_meiyou_pic1 = (ImageView) convertView .findViewById(R.id.iv_meiyou_pic1);
			holder.iv_meiyou_pic2 = (ImageView) convertView .findViewById(R.id.iv_meiyou_pic2);
			holder.tv_meiyou_nick = (TextView) convertView .findViewById(R.id.tv_meiyou_nick);
			holder.tv_meiyou_topic = (TextView) convertView.findViewById(R.id.tv_meiyou_topic);
			holder.tv_publish_time = (TextView) convertView .findViewById(R.id.tv_publish_time);
			holder.gv_meiyou_pics = (GridView) convertView.findViewById(R.id.gv_meiyou_pics);
			holder.ll_meiyou_pics = (LinearLayout) convertView.findViewById(R.id.ll_meiyou_pics);

			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyGuangGaoListObject myGuangGaoListObject = list.get(position);
		if (myGuangGaoListObject != null) {
			LogUtil.i("info", "name===="+myGuangGaoListObject.getDcAdTitle());
			holder.tv_meiyou_nick.setText(myGuangGaoListObject.getDcAdTitle());
			holder.tv_meiyou_topic.setText(myGuangGaoListObject.getDcContent());
			holder.tv_publish_time.setText(Tools.DateStrToDateStr(myGuangGaoListObject.getDtAddTime()));
			//ªÒ»°Õº∆¨
			String topicPics = myGuangGaoListObject.getDcIpath();
			LogUtil.i("info", "topicPics===="+topicPics);
			List<String> pics = new ArrayList<String>();
			if(!"".equals(topicPics)&&topicPics!=null){
				
				String[] topics = topicPics.split(",");
				for(int i=0;i<topics.length;i++){
					pics.add(topics[i]);
				}
				if(topics.length == 1){
					holder.iv_meiyou_pic.setVisibility(View.VISIBLE);
					holder.ll_meiyou_pics.setVisibility(View.GONE);
					holder.gv_meiyou_pics.setVisibility(View.GONE);
					String picUrlone = topics[0];
					ImageLoader.getInstance().displayImage(picUrlone,
							holder.iv_meiyou_pic, TApplication.optionsImage,new MyAnimateFirstDisplayListener());
					
					holder.iv_meiyou_pic.setTag(pics);
					holder.iv_meiyou_pic.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ArrayList<String> pics = (ArrayList<String>) v.getTag();
							ShowPicturesActivity_.intent(context).
							extra("position", 0).
							stringArrayListExtra("pics", (ArrayList<String>) pics).start();
						}
					});
					
				}else if(topics.length == 2){
					holder.ll_meiyou_pics.setVisibility(View.VISIBLE);
					holder.iv_meiyou_pic.setVisibility(View.GONE);
					holder.gv_meiyou_pics.setVisibility(View.GONE);
					ImageLoader.getInstance().displayImage(topics[0],
							holder.iv_meiyou_pic1, TApplication.optionsImage,new MyAnimateFirstDisplayListener());
					ImageLoader.getInstance().displayImage(topics[1],
							holder.iv_meiyou_pic2, TApplication.optionsImage,new MyAnimateFirstDisplayListener());
					
					holder.iv_meiyou_pic1.setTag(pics);
					holder.iv_meiyou_pic1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ArrayList<String> pics = (ArrayList<String>) v.getTag();
							ShowPicturesActivity_.intent(context).
							extra("position", 0).
							stringArrayListExtra("pics", (ArrayList<String>) pics).start();
						}
					});
					
					holder.iv_meiyou_pic2.setTag(pics);
					holder.iv_meiyou_pic2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ArrayList<String> pics = (ArrayList<String>) v.getTag();
							ShowPicturesActivity_.intent(context).
							extra("position", 1).
							stringArrayListExtra("pics", (ArrayList<String>) pics).start();
						}
					});
					
				}else if(topics.length >= 3){
					holder.gv_meiyou_pics.setVisibility(View.VISIBLE);
					holder.iv_meiyou_pic.setVisibility(View.GONE);
					holder.ll_meiyou_pics.setVisibility(View.GONE);
					
					GridViewAdapter adapter = new GridViewAdapter(context, pics);
					holder.gv_meiyou_pics.setAdapter(adapter);
					holder.gv_meiyou_pics.setTag(pics);
					holder.gv_meiyou_pics.setOnItemClickListener(new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							ArrayList<String> pics = (ArrayList<String>) parent.getTag();
							ShowPicturesActivity_.intent(context).
							extra("position", position).
							stringArrayListExtra("pics", (ArrayList<String>) pics).start();
						}
					});
				}
			}else{
				holder.gv_meiyou_pics.setVisibility(View.GONE);
				holder.iv_meiyou_pic.setVisibility(View.GONE);
				holder.ll_meiyou_pics.setVisibility(View.GONE);
			}
		}
		return convertView;
	}
	class ViewHolder {
		private TextView tv_meiyou_nick,tv_meiyou_topic,tv_publish_time;
		private ImageView iv_meiyou_pic,iv_meiyou_pic1,iv_meiyou_pic2;
		private LinearLayout ll_meiyou_pics;
		private GridView gv_meiyou_pics;
	}
}
