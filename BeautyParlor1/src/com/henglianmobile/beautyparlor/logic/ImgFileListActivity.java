package com.henglianmobile.beautyparlor.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.util.Constant;

@EActivity(R.layout.pic_imgfilelist)
public class ImgFileListActivity extends BaseActivity implements OnItemClickListener{

	@ViewById
	protected ListView listView;
	private Util util;
	private ImgFileListAdapter listAdapter;
	private List<FileTraversal> locallist;
	/**选择照片的标记*/
	@Extra
	protected String photo;
	@AfterViews
	void init(){
		util=new Util(this);
		locallist=util.LocalImgFileList();
		List<HashMap<String, String>> listdata=new ArrayList<HashMap<String,String>>();
		Bitmap bitmap[] = null;
		if (locallist!=null) {
			bitmap=new Bitmap[locallist.size()];
			for (int i = 0; i < locallist.size(); i++) {
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("filecount", locallist.get(i).filecontent.size()+"张");
				map.put("imgpath", locallist.get(i).filecontent.get(0)==null?null:(locallist.get(i).filecontent.get(0)));
				map.put("filename", locallist.get(i).filename);
				listdata.add(map);
			}
		}
		listAdapter=new ImgFileListAdapter(this, listdata);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Bundle bundle=new Bundle();
		bundle.putParcelable("data", locallist.get(arg2));
		bundle.putString(Constant.PHOTOTAG, photo);
//		Intent intent=new Intent(this,ImgsActivity.class);
//		intent.putExtras(bundle);
//		startActivity(intent);
		ImgsActivity_.intent(this).extra("bundle", bundle).start();
		this.finish();
	}
	
}
