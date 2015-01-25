package com.henglianmobile.beautyparlor.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.EActivity;

import android.os.Bundle;

import com.aphidmobile.flip.FlipViewController;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.issue5.GalleryFlipAdapter;
import com.henglianmobile.beautyparlor.util.LogUtil;

@EActivity
public class ShowPicturesActivity extends BaseActivity {
	private FlipViewController flipView;
	
	private int position;
	private List<String> pics = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntent().getStringExtra("url");
		position = getIntent().getIntExtra("position", 0);
		pics = getIntent().getStringArrayListExtra("pics");
		LogUtil.i("info", "youdongxima===" + ",position=" + position
				+ ",pics=" + pics);
		
		flipView = new FlipViewController(this,FlipViewController.HORIZONTAL);
		flipView.setAdapter(new GalleryFlipAdapter(this, flipView,pics), position);
	    setContentView(flipView);
	}
}
