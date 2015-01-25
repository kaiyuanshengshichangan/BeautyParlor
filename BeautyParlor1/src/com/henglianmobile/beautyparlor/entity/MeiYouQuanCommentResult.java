package com.henglianmobile.beautyparlor.entity;

import java.util.List;
/**
 * 美友圈评论对象
 * @author Administrator
 *
 */
public class MeiYouQuanCommentResult {
	private int response;
	private List<MeiYouQuanCommentListObject> list;
	public int getResponse() {
		return response;
	}
	public void setResponse(int response) {
		this.response = response;
	}
	public List<MeiYouQuanCommentListObject> getList() {
		return list;
	}
	public void addList(MeiYouQuanCommentListObject list) {
		this.list.add(list);
	}
	
}
