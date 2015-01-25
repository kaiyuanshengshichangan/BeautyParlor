package com.henglianmobile.beautyparlor.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
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
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.MeiYouQuanListObject;
import com.henglianmobile.beautyparlor.ui.activity.MeiYouBaseInfoDetailActivity_;
import com.henglianmobile.beautyparlor.ui.activity.ShowPicturesActivity_;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.henglianmobile.beautyparlor.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MeiYouQuanListAdapter extends BaseAdapter{

	private Context context;
	private List<MeiYouQuanListObject> list;
	private LayoutInflater mInflater;
	
	private void setData(List<MeiYouQuanListObject> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<MeiYouQuanListObject>();
		}
	}

	public MeiYouQuanListAdapter(Context context, List<MeiYouQuanListObject> list) {
		this.context = context;
		this.setData(list);
		this.mInflater = LayoutInflater.from(this.context);

	}

	public void changeData(List<MeiYouQuanListObject> list){
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
	public MeiYouQuanListObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).getId();
	}
	//private TextView ,,tv_publish_time;
	//private ImageView ,,,
	//					,,,;
	//private GridView ;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.fragment_meiyouquan_list_item, null);
			holder.iv_meiyou_photo = (ImageView) convertView .findViewById(R.id.iv_meiyou_photo);
			holder.iv_meiyou_pic = (ImageView) convertView .findViewById(R.id.iv_meiyou_pic);
			holder.iv_meiyou_pic1 = (ImageView) convertView .findViewById(R.id.iv_meiyou_pic1);
			holder.iv_meiyou_pic2 = (ImageView) convertView .findViewById(R.id.iv_meiyou_pic2);
			holder.iv_pinglun = (ImageView) convertView .findViewById(R.id.iv_pinglun);
			holder.iv_zan = (ImageView) convertView .findViewById(R.id.iv_zan);
			holder.iv_share = (ImageView) convertView .findViewById(R.id.iv_share);
			holder.tv_meiyou_nick = (TextView) convertView .findViewById(R.id.tv_meiyou_nick);
			holder.tv_meiyou_topic = (TextView) convertView.findViewById(R.id.tv_meiyou_topic);
			holder.tv_publish_time = (TextView) convertView .findViewById(R.id.tv_publish_time);
			holder.gv_meiyou_pics = (GridView) convertView.findViewById(R.id.gv_meiyou_pics);
			holder.ll_meiyou_pics = (LinearLayout) convertView.findViewById(R.id.ll_meiyou_pics);

			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MeiYouQuanListObject meiYouListObject = list.get(position);
		if (meiYouListObject != null) {
			LogUtil.i("info", "name===="+meiYouListObject.getDcNickName());
			holder.tv_meiyou_nick.setText(meiYouListObject.getDcNickName());
			holder.tv_meiyou_topic.setText(meiYouListObject.getDcContent());
			holder.tv_publish_time.setText(Tools.DateStrToDateStr(meiYouListObject.getDtAddTime()));
			//ªÒ»°Õº∆¨
			
			String photoPic = meiYouListObject.getDcHeadImg().trim();
			if (photoPic != null&&!TextUtils.isEmpty(photoPic)) {
				ImageLoader.getInstance().displayImage(photoPic,
						holder.iv_meiyou_photo, TApplication.optionsImage,new MyAnimateFirstDisplayListener());
			}
			
			int userId = meiYouListObject.getDnUserid();
			holder.iv_meiyou_photo.setTag(userId);
			holder.iv_meiyou_photo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = (Integer) v.getTag();
					MeiYouBaseInfoDetailActivity_.intent(context).extra("id", id).start();
				}
			});
			
			String topicPics = meiYouListObject.getDcIpath().trim();
			LogUtil.i("info", "topicPics===="+topicPics);
			List<String> pics = new ArrayList<String>();
			if(!"".equals(topicPics)){
				
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
		private ImageView iv_meiyou_photo,iv_meiyou_pic,iv_meiyou_pic1,iv_meiyou_pic2
							,iv_pinglun,iv_zan,iv_share;
		private LinearLayout ll_meiyou_pics;
		private GridView gv_meiyou_pics;
	}
}
