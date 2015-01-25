package com.henglianmobile.beautyparlor.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.widget.ImageView;

import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.util.MyAnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;

@EActivity(R.layout.activity_show_picture)
public class ShowPictureActivity extends BaseActivity {

	@ViewById
	protected ImageView iv_picture;

	@Extra
	protected String picPath;

	@AfterViews
	protected void setPicture() {
		ImageLoader.getInstance().displayImage(picPath, iv_picture,
				TApplication.optionsImage, new MyAnimateFirstDisplayListener());
	}
	@Click
	protected void btn_back(){
		this.finish();
	}
	@Click
	protected void iv_picture(){
		this.finish();
	}
}
