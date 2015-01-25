package com.henglianmobile.beautyparlor.ui.wxapi;

import cn.sharesdk.framework.Platform;
/**
 * ShareSDK工具
 * @author Administrator
 *
 */
public class ShareSDKUtil {
	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
			case Platform.ACTION_AUTHORIZING: return "ACTION_AUTHORIZING";
			case Platform.ACTION_GETTING_FRIEND_LIST: return "ACTION_GETTING_FRIEND_LIST";
			case Platform.ACTION_FOLLOWING_USER: return "ACTION_FOLLOWING_USER";
			case Platform.ACTION_SENDING_DIRECT_MESSAGE: return "ACTION_SENDING_DIRECT_MESSAGE";
			case Platform.ACTION_TIMELINE: return "ACTION_TIMELINE";
			case Platform.ACTION_USER_INFOR: return "ACTION_USER_INFOR";
			case Platform.ACTION_SHARE: return "ACTION_SHARE";
			default: {
				return "UNKNOWN";
			}
		}
	}
}
