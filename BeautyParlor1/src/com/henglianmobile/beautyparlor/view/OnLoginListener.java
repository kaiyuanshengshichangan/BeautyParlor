package com.henglianmobile.beautyparlor.view;

import java.util.HashMap;

import com.henglianmobile.beautyparlor.entity.UserInfo;

public interface OnLoginListener {
	/** ��Ȩ��ɵ��ô˽ӿڣ�������Ȩ���ݣ������Ҫע�ᣬ�򷵻�true */
	public boolean onSignin(String platform, HashMap<String, Object> res);
	
	/** ��д��ע����Ϣ����ô˽ӿڣ�����true��ʾ���ݺϷ���ע��ҳ����Թر� */
	public boolean onSignUp(UserInfo info) ;
	
}
