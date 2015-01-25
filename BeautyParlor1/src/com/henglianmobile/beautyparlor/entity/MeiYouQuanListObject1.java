package com.henglianmobile.beautyparlor.entity;

import java.io.Serializable;

import org.androidannotations.annotations.EBean;

@EBean
public class MeiYouQuanListObject1 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4373249514530625245L;
	/**帖子id*/
	private int dnTid;
	/**帖子标题*/
	private String dcTopicTitle;
	/**帖子内容*/
	private String dcTopicContent;
	/**帖子封面图片*/
	private String dcImgPath;
	/**是否显示*/
	private int dnIsShow;
	/**用户ID*/
	private int dnUserid;
	/**发表人昵称*/
	private String dcUserName;
	/**转发自用户ID*/
	private int dnUid;
	/**转发自用户昵称*/
	private String dcUName;
	/**赞的次数*/
	private int dnSupNum;
	/**添加时间*/
	private String dtAddTime;
	/**修改时间*/
	private String dtEditTime;
	/**发表人的头像*/
	private String dcHeadImg;
	/**帖子图片*/
	private String dcIpath;
	public int getDnTid() {
		return dnTid;
	}
	public void setDnTid(int dnTid) {
		this.dnTid = dnTid;
	}
	public String getDcTopicTitle() {
		return dcTopicTitle;
	}
	public void setDcTopicTitle(String dcTopicTitle) {
		this.dcTopicTitle = dcTopicTitle;
	}
	public String getDcTopicContent() {
		return dcTopicContent;
	}
	public void setDcTopicContent(String dcTopicContent) {
		this.dcTopicContent = dcTopicContent;
	}
	public String getDcImgPath() {
		return dcImgPath;
	}
	public void setDcImgPath(String dcImgPath) {
		this.dcImgPath = dcImgPath;
	}
	public int getDnIsShow() {
		return dnIsShow;
	}
	public void setDnIsShow(int dnIsShow) {
		this.dnIsShow = dnIsShow;
	}
	public int getDnUserid() {
		return dnUserid;
	}
	public void setDnUserid(int dnUserid) {
		this.dnUserid = dnUserid;
	}
	public String getDcUserName() {
		return dcUserName;
	}
	public void setDcUserName(String dcUserName) {
		this.dcUserName = dcUserName;
	}
	public int getDnUid() {
		return dnUid;
	}
	public void setDnUid(int dnUid) {
		this.dnUid = dnUid;
	}
	public String getDcUName() {
		return dcUName;
	}
	public void setDcUName(String dcUName) {
		this.dcUName = dcUName;
	}
	public int getDnSupNum() {
		return dnSupNum;
	}
	public void setDnSupNum(int dnSupNum) {
		this.dnSupNum = dnSupNum;
	}
	public String getDtAddTime() {
		return dtAddTime;
	}
	public void setDtAddTime(String dtAddTime) {
		this.dtAddTime = dtAddTime;
	}
	public String getDtEditTime() {
		return dtEditTime;
	}
	public void setDtEditTime(String dtEditTime) {
		this.dtEditTime = dtEditTime;
	}
	public String getDcHeadImg() {
		return dcHeadImg;
	}
	public void setDcHeadImg(String dcHeadImg) {
		this.dcHeadImg = dcHeadImg;
	}
	public String getDcIpath() {
		return dcIpath;
	}
	public void setDcIpath(String dcIpath) {
		this.dcIpath = dcIpath;
	}
	
}
