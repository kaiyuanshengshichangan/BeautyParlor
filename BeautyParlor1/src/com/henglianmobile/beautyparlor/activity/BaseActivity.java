package com.henglianmobile.beautyparlor.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.henglianmobile.beautyparlor.app.TApplication;
/**
 * 每个Activity的基类
 * @author Administrator
 *
 */
@WindowFeature({Window.FEATURE_NO_TITLE})
@EActivity
public abstract class BaseActivity extends Activity{
	public final String HTTP_ERROR = "网络不通，请查看您的网络环境再充重试！";
	public final String LOAD_ALL = "已经加载全部数据！";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TApplication.instance.addActivity(this);
	}
}
