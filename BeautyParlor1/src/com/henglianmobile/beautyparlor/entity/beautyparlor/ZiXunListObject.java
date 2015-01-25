package com.henglianmobile.beautyparlor.entity.beautyparlor;
/**
 * 资讯列表返回内容接口
 * @author Administrator
 *
 */
public class ZiXunListObject {

	private int Row;
	private int id;
	private String title;
	private String dcContent;
	private int dnUserid;
	private String dtAddTime;
	private int tType;
	private String dcImgPath;
	public int getRow() {
		return Row;
	}
	public void setRow(int row) {
		Row = row;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getDcImgPath() {
		return dcImgPath;
	}
	public void setDcImgPath(String dcImgPath) {
		this.dcImgPath = dcImgPath;
	}
}
