package com.henglianmobile.beautyparlor.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

public class BitmapUtil {
	/**
	 * 
	 * @param context
	 * @param uri
	 * @param imageView
	 *            通过imageView得到imageView控件的宽和高，进行缩放填充
	 * @return
	 */
	public static Bitmap getBitmapFromUri(Context context, Uri uri, ImageView imageView) {
		ContentResolver cr = context.getContentResolver();

		try {
			InputStream in = cr.openInputStream(uri);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int mWidth = options.outWidth;
			int mHeight = options.outHeight;
			int screenWidth = imageView.getWidth();
			int screenHeight = imageView.getHeight();

			float scale = 1;
			if (mWidth > screenWidth || mHeight > screenHeight) {
				if (mWidth / mHeight > screenWidth / screenHeight) {
					scale = mWidth / (float) screenWidth;
				} else {
					scale = mHeight / (float) screenHeight;
				}
			}

			options = new BitmapFactory.Options();
			options.inSampleSize = Math.round(scale);

			in = cr.openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);

			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	/**
	 * 判断SDK的状态
	 */
	public static boolean hasSdCard() {
		try {
			return Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
