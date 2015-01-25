package com.henglianmobile.beautyparlor.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.henglianmobile.beautyparlor.crouton.Crouton;
import com.henglianmobile.beautyparlor.crouton.Style;

/**
 * 此类为一工具类，包含了各种常用共用方法
 * 1、将文件转为Base64字符串
 * 2、将Bitmap转为Base64字符串
 * 3、保存Bitmap到文件
 * 4、将日期转换为一定格式的字符串
 * 5、将一定格式的日期转换为毫秒数
 * 6、显示提示信息
 * 7、弹出警示对话框，并且有两个按钮
 * 8、显示选择日期对话框到文本框中
 * 9、显示时间对话框 只显示时分秒
 * 10、显示一个滚动进度条条对话框
 * 11、关闭滚动进度条对话框
 * 12、XXXX-XX-XX格式的日期字符串转成星期几
 * @author Administrator
 * 
 */
public class Tools {
	public static final String HTTP_ERROR = "网络不通，请查看您的网络环境再充重试！";
	public static final String LOAD_ALL = "已经加载全部数据！";

	/**
	 * 将文件转为Base64字符串
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64File(File file) throws Exception {
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	/**
	 * 将Bitmap转为Base64字符串
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64File(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		// 将bitmap做成输出流，保证图片质量100%
		bitmap.compress(CompressFormat.PNG, 100, baos);
		// 转换成byte数组
		byte[] appicon = baos.toByteArray();// 转为byte数组
		// 以base64编码将byte数组转换成字符串
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}

	/**
	 * 保存Bitmap到文件
	 * 
	 * @param bm
	 * @param newFile
	 * @return
	 */
	public static boolean saveBitmapToFile(Bitmap bm, File newFile) {
		if (newFile == null) {
			return false;
		}
		if (newFile.exists()) {
			newFile.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(newFile);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			LogUtil.i("info",
					"photo save success,path is '" + newFile.getAbsolutePath()
							+ "'");
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.i("info",
					"photo save failed,path is '" + newFile.getAbsolutePath()
							+ "'");
			return false;
		}
		return true;
	}

	/**
	 * 将日期转换为一定格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String DateFormat(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	/**
	 * 将一定格式的日期转换为毫秒数
	 * 如：Tools.StringDateToLong(startDate,"yyyy-MM-dd")
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long StringDateToLong(String date, String format)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date).getTime();// 毫秒
	}

	/**
	 * 显示提示信息
	 * 
	 * @param context
	 * @param text
	 */
	public static void showMsg(Context context, String text) {
		
		try {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void showMsg(Context context, int resId) {
		
		try {
			Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 弹出警示对话框，并且有两个按钮
	 * 
	 * @param c
	 *            要弹出的页面
	 * @param title
	 *            标题内容
	 * @param msg
	 *            提示内容
	 * @param neutralBtnText
	 *            确定按钮内容
	 * @param neutralBtnListen
	 *            确定按钮触发的事件
	 * @param egativeBtnText
	 *            取消按钮内容
	 * @param egativeBtnListen
	 *            取消按钮触发的事件
	 */
	public static void showDialog(Context c, String title, String msg,
			String neutralBtnText, OnClickListener neutralBtnListen,
			String egativeBtnText, OnClickListener egativeBtnListen) {
		new AlertDialog.Builder(c).setTitle(title).setMessage(msg)
				.setNeutralButton(neutralBtnText, neutralBtnListen)
				.setNegativeButton(egativeBtnText, egativeBtnListen).show();

	}

	/**
	 * 显示选择日期对话框到文本框中
	 * 
	 * @param context
	 * @param textView
	 */
	public static void datePicker(Context context, final TextView textView) {
		Calendar c = Calendar.getInstance();

		// 日期对话框
		DatePickerDialog dialog = // 继承了AlertDialog
		new DatePickerDialog(context, new OnDateSetListener() {

			// 回调函数
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				textView.setText(year
						+ "-"
						+ ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1)
								: (monthOfYear + 1)) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth
										: dayOfMonth));
			}
		},// 回调
				c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));

		// 显示对话框
		dialog.show();
	}

	/**
	 * 显示时间对话框 只显示时分秒
	 * */
	public static void timePicker(Context context, final TextView textView) {
		Calendar c = Calendar.getInstance();
		TimePickerDialog d = new TimePickerDialog(context,
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {

						textView.setText((hourOfDay < 10 ? "0" + hourOfDay
								: hourOfDay)
								+ ":"
								+ (minute < 10 ? "0" + minute : minute));
					}
				}, // 回调函数
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
		d.show();
	}

	public static ProgressDialog progressDialog;

	/**
	 * 显示一个滚动条对话框
	 * */
	public static void showProgressDialog(Context context, String message) {
		try {
			if (progressDialog == null) {
				progressDialog = ProgressDialog.show(context, null, message);
				// progressDialog.setCancelable(true);
			} else if (progressDialog != null) {
				if (!progressDialog.isShowing())
					progressDialog.show();
				progressDialog.setMessage(message);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 关闭滚动对话框
	 */
	public static void closeProgressDialog() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.cancel();
				progressDialog =null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * XXXX-XX-XX格式的日期字符串转成星期几
	 * 
	 * @param dateString
	 * @return
	 */
	public static String showWeek(String dateString) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int weekofday = c.get(Calendar.DAY_OF_WEEK);
		String returnResault = "";
		switch (weekofday) {
		case 1:
			returnResault = "星期日";
			break;
		case 2:
			returnResault = "星期一";
			break;
		case 3:
			returnResault = "星期二";
			break;
		case 4:
			returnResault = "星期三";
			break;
		case 5:
			returnResault = "星期四";
			break;
		case 6:
			returnResault = "星期五";
			break;
		case 7:
			returnResault = "星期六";
			break;
		}
		return returnResault;
	}

	/**
	 * 对返回的日期进行处理
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String DateStrToDateStr(String dateStr) {
		if(dateStr!=null){
			if(dateStr.length()>18){
				String date = dateStr.substring(0, 10);
				String time = dateStr.substring(11, 19);
				return date + " " + time;
			}else{
				return dateStr;
			}
		}else{
			return "";
		}
	}

	public static void showCustomToast(Context context,String msg,ViewGroup viewGroup) {
		Crouton.makeText((Activity)context, msg, Style.ALERT, viewGroup).show();
	}
}
