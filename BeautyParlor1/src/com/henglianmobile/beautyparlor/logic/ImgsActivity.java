package com.henglianmobile.beautyparlor.logic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.logic.ImgsAdapter.OnItemClickClass;
import com.henglianmobile.beautyparlor.util.Constant;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
@EActivity(R.layout.pic_photogrally)
public class ImgsActivity extends BaseActivity {
	
	@ViewById
	protected GridView gridView1;
	@ViewById
	protected LinearLayout selected_image_layout;
	@ViewById
	protected RelativeLayout relativeLayout2;
	@ViewById
	protected Button button3;

	
	FileTraversal fileTraversal;
	
	ImgsAdapter imgsAdapter;
	Util util;
	
	HashMap<Integer, ImageView> hashImage;
	ArrayList<String> filelist;

	private Intent intent;
	/** 选择照片的标记 */
	private String tag;

	@Extra
	protected Bundle bundle;
	@AfterViews
	protected void initData(){
		fileTraversal = bundle.getParcelable("data");
		tag = bundle.getString(Constant.PHOTOTAG);
		LogUtil.i("info", "fileTraversal="+fileTraversal+",tag="+tag);
		imgsAdapter = new ImgsAdapter(this, fileTraversal.filecontent,
				onItemClickClass);
		gridView1.setAdapter(imgsAdapter);
		hashImage = new HashMap<Integer, ImageView>();
		filelist = new ArrayList<String>();
		// imgGridView.setOnItemClickListener(this);
		util = new Util(this);
	}

	class BottomImgIcon implements OnItemClickListener {

		int index;

		public BottomImgIcon(int index) {
			this.index = index;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}
	}

	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath, int index, CheckBox checkBox)
			throws FileNotFoundException {
		LinearLayout.LayoutParams params = new LayoutParams(
				relativeLayout2.getMeasuredHeight() - 10,
				relativeLayout2.getMeasuredHeight() - 10);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.ic_launcher);
		float alpha = 100;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath, checkBox));
		return imageView;
	}

	ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};

	class ImgOnclick implements OnClickListener {
		String filepath;
		CheckBox checkBox;

		public ImgOnclick(String filepath, CheckBox checkBox) {
			this.filepath = filepath;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			selected_image_layout.removeView(arg0);
			button3
					.setText("已选择(" + selected_image_layout.getChildCount() + ")张");
			filelist.remove(filepath);
		}
	}

	ImgsAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath = fileTraversal.filecontent.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				selected_image_layout.removeView(hashImage.get(Position));
				filelist.remove(filapath);
				button3.setText("已选择(" + selected_image_layout.getChildCount()
						+ ")张");
			} else {
				try {
					if((filelist.size()+TApplication.picsCount)>=9){
						Tools.showMsg(ImgsActivity.this, "最多发布九张图片，您已经选择了九张!");
						return;
					}
					checkBox.setChecked(true);
					Log.i("img", "img choise position->" + Position);
					ImageView imageView = iconImage(filapath, Position,
							checkBox);
					if (imageView != null) {
						hashImage.put(Position, imageView);
						filelist.add(filapath);
						selected_image_layout.addView(imageView);
						button3.setText("已选择("
								+ selected_image_layout.getChildCount() + ")张");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void tobreak(View view) {
		finish();
	}

	/**
	 * FIXME 亲只需要在这个方法把选中的文档目录已list的形式传过去即可
	 * 
	 * @param view
	 */
	public void sendfiles(View view) {
		// Intent intent =new Intent(this, MainActivity.class);
		// Bundle bundle=new Bundle();
		// bundle.putStringArrayList("files", filelist);
		// intent.putExtras(bundle);
		// startActivity(intent);
		// 发送广播
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("files", filelist);
		if (tag.equals(Constant.PIC_MEIYOUQUAN)) {
			intent = new Intent(Constant.ACTIONMEIYOUQUANSELECTPHOTO);
			intent.putExtras(bundle);
			sendBroadcast(intent);
		}else if (tag.equals(Constant.PIC_PROPOSALREQUEST)) {
			intent = new Intent(Constant.ACTIONCONSUMERPROPOSALREQUESTSELECTPHOTO);
			intent.putExtras(bundle);
			sendBroadcast(intent);
		} else if (tag.equals(Constant.PIC_PUBLISH_GUANGGAO)) {
			intent = new Intent(Constant.ACTIONBEAUTYPARLORPULISHGUANGGAOSELECTPHOTO);
			intent.putExtras(bundle);
			sendBroadcast(intent);
		} 
//		else if (tag.equals(Constant.PIC_DOCTOR_HEALTH_MANAGE)) {
//			intent = new Intent(
//					Constant.ACTIONDOCTORHEALTHMANAGEMETHODSELECTPHOTO);
//			intent.putExtras(bundle);
//			sendBroadcast(intent);
//		}else if (tag.equals(Constant.PIC_YAOFANG)) {
//			intent = new Intent(
//					Constant.ACTIONYAOFANGSELECTPHOTO);
//			intent.putExtras(bundle);
//			sendBroadcast(intent);
//		}
		this.finish();
	}
}
