package com.henglianmobile.beautyparlor.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.henglianmobile.beautyparlor.app.TApplication;
/**
 * ÿ��Activity�Ļ���
 * @author Administrator
 *
 */
@WindowFeature({Window.FEATURE_NO_TITLE})
@EActivity
public abstract class BaseActivity extends Activity{
	public final String HTTP_ERROR = "���粻ͨ����鿴�������绷���ٳ����ԣ�";
	public final String LOAD_ALL = "�Ѿ�����ȫ�����ݣ�";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TApplication.instance.addActivity(this);
	}
}
