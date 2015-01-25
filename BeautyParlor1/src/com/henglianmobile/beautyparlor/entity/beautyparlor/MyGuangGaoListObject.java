package com.henglianmobile.beautyparlor.entity.beautyparlor;

import java.io.Serializable;

/**
 * 美容院-我的广告列表
 * @author Administrator
 *
 */
public class MyGuangGaoListObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3421650502241435308L;
	/**  */
	private int Row;
	/** 广告id */
	private int dnAdid;
	/**广告标题 */
	private String dcAdTitle;
	/** 广告内容 */
	private String dcContent;
	/** 用户ID */
	private int dnUserid;
	/** 添加时间 */
	private String dtAddTime;
	/** 类型 */
	private int tType;
	/** 其它图片（用“，”分开） */
	private String dcIpath;
	public int getRow() {
		return Row;
	}
	public void setRow(int row) {
		Row = row;
	}
	public int getDnAdid() {
		return dnAdid;
	}
	public void setDnAdid(int dnAdid) {
		this.dnAdid = dnAdid;
	}
	public String getDcAdTitle() {
		return dcAdTitle;
	}
	public void setDcAdTitle(String dcAdTitle) {
		this.dcAdTitle = dcAdTitle;
	}
	public String getDcContent() {
		return dcContent;
	}
	public void setDcContent(String dcContent) {
		this.dcContent = dcContent;
	}
	public int getDnUserid() {
		return dnUserid;
	}
	public void setDnUserid(int dnUserid) {
		this.dnUserid = dnUserid;
	}
	public String getDtAddTime() {
		return dtAddTime;
	}
	public void setDtAddTime(String dtAddTime) {
		this.dtAddTime = dtAddTime;
	}
	public int gettType() {
		return tType;
	}
	public void settType(int tType) {
		this.tType = tType;
	}
	public String getDcIpath() {
		return dcIpath;
	}
	public void setDcIpath(String dcIpath) {
		this.dcIpath = dcIpath;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
