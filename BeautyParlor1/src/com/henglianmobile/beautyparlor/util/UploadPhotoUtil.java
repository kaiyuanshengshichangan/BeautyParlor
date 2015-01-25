package com.henglianmobile.beautyparlor.util;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
/**
 * 上传头像工具类
 * 包含裁剪图片
 * @author Administrator
 *
 */
public class UploadPhotoUtil {

	private static String[] items = new String[] { "相册", "拍照" };
	/**
	 * 选择照片的提示框
	 */
	public static void publishPhotoDialog(final Context context) {

		// 创建一个对话提示框
		new AlertDialog.Builder(context).setTitle("照片")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intentFromGallery = null;
						Intent intentFromCapture = null;

						switch (which) {
						// 从相册获取图片
						case 0:
							intentFromGallery = new Intent(Intent.ACTION_PICK,
									null);
							intentFromGallery
									.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
							((Activity)context).startActivityForResult(
									intentFromGallery, 0);
							break;
						// 拍照存储到sd上，名字为image.jpg
						case 1:
							intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);

							if (hasSdCard()) {
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												"image.jpg")));

							}
							((Activity)context).startActivityForResult(
									intentFromCapture, 1);
							break;

						default:
							break;
						}
					}
				}).show();
	}
	/*
	 * 判断SDK的状态
	 */
	private static boolean hasSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static void startPhotoZoom(Uri uri,Context context) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		((Activity)context).startActivityForResult(intent, 2);
	}
}
