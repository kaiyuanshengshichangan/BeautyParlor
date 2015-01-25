package com.henglianmobile.beautyparlor.ui.activity;

import java.lang.reflect.Type;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.henglianmobile.beautyparlor.R;
import com.henglianmobile.beautyparlor.activity.BaseActivity;
import com.henglianmobile.beautyparlor.adapter.MeiYouQuanPinglunAdapter;
import com.henglianmobile.beautyparlor.app.TApplication;
import com.henglianmobile.beautyparlor.entity.MeiYouQuanCommentListObject;
import com.henglianmobile.beautyparlor.util.Const;
import com.henglianmobile.beautyparlor.util.HttpUtil;
import com.henglianmobile.beautyparlor.util.ListViewUtil;
import com.henglianmobile.beautyparlor.util.LogUtil;
import com.henglianmobile.beautyparlor.util.Tools;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * ��Ѷ����
 * 
 * @author Administrator
 * 
 */
@EActivity(R.layout.activity_zixun_detail)
public class ZiXunDetailActivity extends BaseActivity {

	@ViewById
	protected TextView tv_pinglun_count, tv_zan_count;
	@ViewById
	protected WebView wv_zixun;
	@ViewById
	protected LinearLayout ll_meiyou_pics, ll_submit_commit, ll_bottom;
	@ViewById
	protected EditText et_comment;
	@ViewById
	protected ListView lv_pinglun;
	@Extra
	protected int id,tType;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//��ȡ����
//		String url1 = Const.GETMEIYOUQUANDETAILCOMMENTURL + id + "&type="
//				+ tType;
//		getCommentHttp(url1);
//	}
	@AfterViews
	protected void setData() {
		String url = Const.GETZIXUNDETAILURL+"id="+id+"&type="+tType;
		LogUtil.i("url", "url="+url);
		WebSettings webSettings = wv_zixun.getSettings();
		// ����WebView���ԣ��ܹ�ִ��Javascript�ű�
		webSettings.setJavaScriptEnabled(true);
		// ���ÿ��Է����ļ�
		webSettings.setAllowFileAccess(true);
		// ����֧������
		webSettings.setBuiltInZoomControls(true);
		// ������Ҫ��ʾ����ҳ
		wv_zixun.loadUrl(url);
		// ����Web��ͼ
		wv_zixun.setWebViewClient(new MyWebViewClient());
		//��ȡ����
		String url1 = Const.GETMEIYOUQUANDETAILCOMMENTURL + id + "&type="
				+ tType;
		getCommentHttp(url1);
	}
	private void getCommentHttp(String url) {
		LogUtil.i("url", "ZiXunDetailActivity��ȡ����----url==" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String responseString) {
				if (arg0 == 200) {
					LogUtil.i("hck", responseString);
					Type type = new TypeToken<List<MeiYouQuanCommentListObject>>() {
					}.getType();
					List<MeiYouQuanCommentListObject> meiYouQuanCommentListObjects = TApplication.gson
							.fromJson(responseString, type);
					if (meiYouQuanCommentListObjects != null) {

						MeiYouQuanPinglunAdapter adapter = new MeiYouQuanPinglunAdapter(
								ZiXunDetailActivity.this,
								meiYouQuanCommentListObjects);
						lv_pinglun.setAdapter(adapter);
						tv_pinglun_count.setText(meiYouQuanCommentListObjects
								.size() + "");
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(ZiXunDetailActivity.this, HTTP_ERROR);
			}
		});
	}
	// Web��ͼ
	private class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	@Click
	protected void btn_back(){
		this.finish();
	}
	@Click
	protected void iv_comment_bottom() {
		if (ll_submit_commit.getVisibility() == View.GONE) {
			ll_submit_commit.setVisibility(View.VISIBLE);
			ll_bottom.setVisibility(View.GONE);
		} else {
			ll_submit_commit.setVisibility(View.GONE);
		}
	}
	@Click
	protected void btn_send() {// ��������
		String comment = et_comment.getText().toString().trim();
		if (TextUtils.isEmpty(comment)) {
			Tools.showMsg(this, "�������ݲ���Ϊ��!");
			return;
		}
		String url = Const.ADDMEIYOUQUANCOMMENTURL + "tid=" + id + "&type="
				+ tType + "&uid=" + TApplication.user.getUid() + "&fname="
				+ TApplication.user.getNickname() + "&content=" + comment;
		sendHttpGet(url);
	}

	private void sendHttpGet(String url) {
		LogUtil.i("url", "ZiXunDetailActivity��������----url==" + url);
		HttpUtil.get(url, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String responseString) {
				if (arg0 == 200) {
					LogUtil.i("hck", responseString);
					try {
						JSONObject jsonObject = new JSONObject(responseString);
						int response = jsonObject.getInt("response");
						if (response == 0) {
							Tools.showMsg(ZiXunDetailActivity.this,
									"��������ʧ��!");
						} else if (response > 0) {
							Tools.showMsg(ZiXunDetailActivity.this,
									"���۷��ͳɹ�!");
							ll_submit_commit.setVisibility(View.GONE);
							ll_bottom.setVisibility(View.VISIBLE);
							et_comment.setText("");
							String url = Const.GETMEIYOUQUANDETAILCOMMENTURL
									+ id + "&type=" + tType;
							getCommentHttp(url);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Tools.showMsg(ZiXunDetailActivity.this, HTTP_ERROR);
			}
		});
	}
}
