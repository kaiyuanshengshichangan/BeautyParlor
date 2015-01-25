package com.henglianmobile.beautyparlor.entity;

import java.io.Serializable;

import org.androidannotations.annotations.EBean;

@EBean
public class MeiYouQuanListObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -566412237049051721L;
	private int Row;
	private int id;
	private String title;
	private int dnUserid;
	private String dtAddTime;
	private String dcContent;
	private int tType;
	private String dcIpath;
	private String dcNickName;
	private String dcHeadImg;
	private String dcSign;
	
	public String getDcContent() {
		return dcContent;
	}
	public void setDcContent(String dcContent) {
		this.dcContent = dcContent;
	}
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
	public String getDcNickName() {
		return dcNickName;
	}
	public void setDcNickName(String dcNickName) {
		this.dcNickName = dcNickName;
	}
	public String getDcHeadImg() {
		return dcHeadImg;
	}
	public void setDcHeadImg(String dcHeadImg) {
		this.dcHeadImg = dcHeadImg;
	}
	public String getDcSign() {
		return dcSign;
	}
	public void setDcSign(String dcSign) {
		this.dcSign = dcSign;
	}
	
}
