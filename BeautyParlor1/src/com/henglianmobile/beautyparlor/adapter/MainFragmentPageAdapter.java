package com.henglianmobile.beautyparlor.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainFragmentPageAdapter extends FragmentStatePagerAdapter{
	//数据是fragment，把三个fragment放到viewPager
	ArrayList<Fragment> list;
	public MainFragmentPageAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		if(list==null){
			this.list=new ArrayList<Fragment>();
		}else{
			this.list=list;
		}
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
