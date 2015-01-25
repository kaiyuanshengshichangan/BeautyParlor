package com.henglianmobile.beautyparlor.app;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.entity.LoginResultObject;
import com.henglianmobile.beautyparlor.entity.UserInfoDetailObject;
import com.henglianmobile.beautyparlor.parse.ParseXml;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class TApplication extends Application {

	public static TApplication instance;
	public static Gson gson = new Gson();
	public static LoginResultObject user;
	public UserInfoDetailObject userInfoDetailObject = new UserInfoDetailObject();
	/** 处于活动状态的activity集合 */
	private static List<Activity> activeActivities;

	/** 发布图片的数量 */
	public static int picsCount;
	/** DisplayImageOptions是用于设置图片显示的类 */
	public static DisplayImageOptions optionsImage;
	
	/** 版本更新 */
	public static int localVersion;// 本地安装版本
	public static String localVersionName;// 本地安装版本号
	public static int serverVersion;// 服务器版本
	public static String downloadDir = "beautyparlor/";// 安装目录
	public static String downloadUrl;
	private HashMap<String, String> map = new HashMap<String, String>();
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			HashMap<String, String> map=new HashMap<String, String>();
			map=(HashMap<String, String>) msg.obj;
			serverVersion=Integer.valueOf(map.get("versionCode"));
			LogUtil.i("info", "serverVersion="+serverVersion);
			downloadUrl=map.get("url");
			LogUtil.i("info", "downloadUrl"+downloadUrl);
		};
	};
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		activeActivities = new ArrayList<Activity>();
		initImageLoader(getApplicationContext());
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		optionsImage = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.load)
				.showImageForEmptyUri(R.drawable.mine_avatar)
				.showImageOnFail(R.drawable.mine_avatar).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
		getLocationVersion();
		new Thread(){
			public void run() {
				try {
					//联网获取服务器上的版本信息
					URL url=new URL(Const.APK_VERSION_URL);
					HttpURLConnection conn =  (HttpURLConnection) url.openConnection();   
					conn.setConnectTimeout(5000);  
					InputStream inputStream =conn.getInputStream();
					map=ParseXml.parseXml(inputStream);
					Message msg=Message.obtain();
					msg.obj=map;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
			};
		}.start();
	}

	/**
	 * 将activity添加到集合
	 * 
	 * @param paramActivity
	 */
	public void addActivity(Activity paramActivity) {
		activeActivities.add(paramActivity);
	}

	/**
	 * 退出所有Activity
	 */
	public void exitActivities() {
		for (Activity activity : activeActivities) {
			activity.finish();
			LogUtil.i("info", "activity" + activity);
		}
		// 把应用全清掉,完全退出
		System.exit(0);
	}

	/**
	 * 获取TApplication实例
	 * 
	 * @return
	 */
	public static TApplication getInstance() {
		return instance;
	}

	/**
	 * 加载ImageLoader
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(3)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级  
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(10 * 512 * 512).memoryCacheSize(10 * 512 * 512)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	/**
	 * 获取清单文件中的版本号
	 */
	private void getLocationVersion() {
		try {
			//获取清单文件中的版本号
			PackageInfo packageInfo = getApplicationContext()
					.getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
			localVersionName=packageInfo.versionName;
			LogUtil.i("info", "localVersion=" + localVersion);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
