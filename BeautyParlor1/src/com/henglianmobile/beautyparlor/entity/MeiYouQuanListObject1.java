package com.henglianmobile.beautyparlor.entity;

import java.io.Serializable;

import org.androidannotations.annotations.EBean;

@EBean
public class MeiYouQuanListObject1 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4373249514530625245L;
	/**����id*/
	private int dnTid;
	/**���ӱ���*/
	private String dcTopicTitle;
	/**��������*/
	private String dcTopicContent;
	/**���ӷ���ͼƬ*/
	private String dcImgPath;
	/**�Ƿ���ʾ*/
	private int dnIsShow;
	/**�û�ID*/
	private int dnUserid;
	/**�������ǳ�*/
	private String dcUserName;
	/**ת�����û�ID*/
	private int dnUid;
	/**ת�����û��ǳ�*/
	private String dcUName;
	/**�޵Ĵ���*/
	private int dnSupNum;
	/**���ʱ��*/
	private String dtAddTime;
	/**�޸�ʱ��*/
	private String dtEditTime;
	/**�����˵�ͷ��*/
	private String dcHeadImg;
	/**����ͼƬ*/
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
