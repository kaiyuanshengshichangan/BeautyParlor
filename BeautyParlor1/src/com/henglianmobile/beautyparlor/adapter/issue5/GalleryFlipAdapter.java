package com.henglianmobile.beautyparlor.adapter.issue5;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aphidmobile.flip.FlipViewController;

public class GalleryFlipAdapter extends BaseAdapter {
//	private String Url;
//	private List<String> pics = new ArrayList<String>();

  private ArrayList<GalleryPage> galleryPageList;

  private Context mContext;

  private FlipViewController controller;

  public GalleryFlipAdapter(Context context, FlipViewController controller,List<String> pics) {
    this.mContext = context;
    this.controller = controller;

    ArrayList<GalleryPage> list = new ArrayList<GalleryPage>();
    for(int i = 0;i < pics.size();i++){
    	Log.i("aaa", "url+pics.get(i)="+pics.get(i));
    	list.add(new GalleryPage("",
              pics.get(i),
              "",
              ""));
    }
//    list.add(new GalleryPage("",
//                             "http://www.hotel-chantecler.be/new-images/grand_place_building.jpg",
//                             "",
//                             ""));

    galleryPageList = list;
  }

  public int getCount() {
    return galleryPageList.size();
  }

  public Object getItem(int position) {
    return galleryPageList.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      Log.i("GalleryFlipAdapter", "convertView == null");
      convertView =
          new GalleryFlipItem(mContext, galleryPageList.get(position), controller, position);
    } else {
      Log.i("GalleryFlipAdapter", "convertView != null");
      ((GalleryFlipItem) convertView)
          .refreshView(galleryPageList.get(position), controller, position);
    }
    return convertView;
  }

  public void clear() {
    galleryPageList.clear();
  }
}
