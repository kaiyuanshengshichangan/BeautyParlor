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
	 * ѹ��ͼƬ�����Ĭ��100k
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		return compressImage(image, 1024 * 100);
	}

	/**
	 * ѹ��ͼƬ���
	 * 
	 * @param image
	 * @param size
	 *            ѹ�����ͼƬ���
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, int size) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��  
		int options = 90;
		while (baos.toByteArray().length > size) { //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��         
			baos.reset();//����baos�����baos  
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��  
			options -= 10;//ÿ�ζ�����10  
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��  
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ  
		return bitmap;
	}

	/**
	 * ѹ��ͼƬ�ֱ���
	 * 
	 * @param srcPath
	 *            ͼƬ·��
	 * @param width
	 *            ��׼���
	 * @param height
	 *            ��׼�߶�
	 * @return
	 */
	public static Bitmap getimage(String srcPath, float width, float height) {
		if (srcPath == null || srcPath.trim().equals("")) {
			return null;
		}
		float ww, hh;
		if (width < 0 || height <= 0) {
			//���������ֻ��Ƚ϶���800*480�ֱ���
			ww = 480f;
			hh = 800f;
		} else {
			hh = width;
			ww = height;
		}
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��  
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//��ʱ����bmΪ��  

		newOpts.inJustDecodeBounds = false;
		newOpts.inPreferredConfig = Config.RGB_565;//����ͼƬ��ARGB888��RGB565
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		//���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��  
		int be = (int) ((w / ww + h / hh) / 2);
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//�������ű���  
		//���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��  
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;//ѹ���ñ�����С���ٽ�������ѹ��  
	}
	
	public static Bitmap readBitMap(String fileName,int n) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inSampleSize = n; // width��hight��Ϊԭ����ʮ��һ
        opt.inPurgeable = true;
        opt.inInputShareable = true; // ��ȡ��ԴͼƬ
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
	 * ѹ��ͼƬ�ֱ���
	 * 
	 * @param image
	 *            ͼƬ����
	 * @param width
	 *            ��׼���
	 * @param height
	 *            ��׼�߶�
	 * @return
	 */
	public static Bitmap getimage(Bitmap image, float width, float height) {
		if (image == null) {
			return null;
		}
		float ww, hh;
		if (width < 0 || height <= 0) {
			//���������ֻ��Ƚ϶���800*480�ֱ���
			ww = 480f;
			hh = 800f;
		} else {
			hh = width;
			ww = height;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {//�ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���    
			baos.reset();//����baos�����baos  
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//����ѹ��50%����ѹ��������ݴ�ŵ�baos��  
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��  
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		newOpts.inPreferredConfig = Config.RGB_565;//����ͼƬ��ARGB888��RGB565
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		//���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��  
		int be = (int) ((w / ww + h / hh) / 2);
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//�������ű���  
		//���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��  
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return bitmap;//ѹ���ñ�����С���ٽ�������ѹ��  
	}
}
