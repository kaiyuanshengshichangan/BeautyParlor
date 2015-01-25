package com.henglianmobile.beautyparlor.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.TypedValue;

public class Util {

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	/**
	 * zoom bitmap to certain size
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (bitmap == null) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
		matrix.postScale(scale, scale);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * 压缩图片体积，默认100k
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		return compressImage(image, 1024 * 100);
	}

	/**
	 * 压缩图片体积
	 * 
	 * @param image
	 * @param size
	 *            压缩后的图片体积
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, int size) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
		int options = 90;
		while (baos.toByteArray().length > size) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
			baos.reset();//重置baos即清空baos  
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
			options -= 10;//每次都减少10  
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
		return bitmap;
	}

	/**
	 * 压缩图片分辨率
	 * 
	 * @param srcPath
	 *            图片路径
	 * @param width
	 *            标准宽度
	 * @param height
	 *            标准高度
	 * @return
	 */
	public static Bitmap getimage(String srcPath, float width, float height) {
		if (srcPath == null || srcPath.trim().equals("")) {
			return null;
		}
		float ww, hh;
		if (width < 0 || height <= 0) {
			//现在主流手机比较多是800*480分辨率
			ww = 480f;
			hh = 800f;
		} else {
			hh = width;
			ww = height;
		}
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了  
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空  

		newOpts.inJustDecodeBounds = false;
		newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
		int be = (int) ((w / ww + h / hh) / 2);
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例  
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;//压缩好比例大小后再进行质量压缩  
	}
	
	public static Bitmap readBitMap(String fileName,int n) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inSampleSize = n; // width，hight设为原来的十分一
        opt.inPurgeable = true;
        opt.inInputShareable = true; // 获取资源图片
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(fis, null, opt);
    }


	/**
	 * 压缩图片分辨率
	 * 
	 * @param image
	 *            图片对象
	 * @param width
	 *            标准宽度
	 * @param height
	 *            标准高度
	 * @return
	 */
	public static Bitmap getimage(Bitmap image, float width, float height) {
		if (image == null) {
			return null;
		}
		float ww, hh;
		if (width < 0 || height <= 0) {
			//现在主流手机比较多是800*480分辨率
			ww = 480f;
			hh = 800f;
		} else {
			hh = width;
			ww = height;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
			baos.reset();//重置baos即清空baos  
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中  
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了  
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
		int be = (int) ((w / ww + h / hh) / 2);
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例  
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return bitmap;//压缩好比例大小后再进行质量压缩  
	}
}
