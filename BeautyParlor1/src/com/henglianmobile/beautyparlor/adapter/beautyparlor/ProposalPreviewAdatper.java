package com.henglianmobile.beautyparlor.adapter.beautyparlor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.util.LogUtil;

public class ProposalPreviewAdatper extends BaseAdapter {
	private FinalBitmap fb;
	private Context mContext;
	private List<String> mList = null;
	private LayoutInflater inflater = null;
//	public static File tempOutFile;
//	public static final String DCIM = Environment
//			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//			.toString();
//	public static final String DCIM_DIRECTORY = DCIM + "/Camera/";


	public ProposalPreviewAdatper(Context context,
			List<String> list) {
		this.mContext = context;
		if (list == null) {
			mList = new ArrayList<String>();
		} else {
			this.mList = list;
		}
		inflater = LayoutInflater.from(mContext);
		fb = FinalBitmap.create(mContext);// ³õÊ¼»¯FinalBitmapÄ£¿é
		fb.configLoadingImage(R.drawable.ic_launcher);
		fb.configBitmapMaxWidth(100);
		fb.configBitmapMaxHeight(100);
		fb.configBitmapLoadThreadSize(10);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		Holder holder;
//		if (convertView == null) {
//
//			convertView = inflater
//					.inflate(
//							R.layout.activity_beautyparlor_proposal_preview_adapter_item_result,
//							null);
//			holder = new Holder();
//			holder.img = (ImageView) convertView
//					.findViewById(R.id.iv_friend_photo);
//
//		} else {
//			holder = (Holder) convertView.getTag();
//		}
//		String pic = mList.get(position);
//		LogUtil.i("info", "pic="+pic);
//		Uri uri = Uri.fromFile(new File(pic));
//		holder.img.setImageURI(uri);
////		fb.display(holder.img, pic);
//
//		return convertView;
		ImageView imageview;
		if (convertView == null) {
			imageview = new ImageView(mContext);
			imageview.setLayoutParams(new GridView.LayoutParams(200, 200));
			imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
			// imageview.setFocusable(false);
		} else {
			imageview = (ImageView) convertView;
		}
		String pic = mList.get(position);
		LogUtil.i("info", "pic="+pic);
		Uri uri = Uri.fromFile(new File(pic));
		imageview.setImageURI(uri);
		return imageview;
	}

	private class Holder {
		public ImageView img = null;
	}
}
