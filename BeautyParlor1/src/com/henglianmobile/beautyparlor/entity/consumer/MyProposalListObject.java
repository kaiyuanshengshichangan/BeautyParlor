package com.henglianmobile.beautyparlor.entity.consumer;

import java.io.Serializable;

/**
 * 我的方案列表对象
 * @author Administrator
 *
 */
public class MyProposalListObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Row;
	private String dcUserName;
	private int dnPid;
	private String dcContent;
	private int dnUserid;
	private int toUserId;
	private int parentId;
	private String dtAddTime;
	private String typeName;
	public int getRow() {
		return Row;
	}
	public void setRow(int row) {
		Row = row;
	}
	public String getDcUserName() {
		return dcUserName;
	}
	public void setDcUserName(String dcUserName) {
		this.dcUserName = dcUserName;
	}
	public int getDnPid() {
		return dnPid;
	}
	public void setDnPid(int dnPid) {
		this.dnPid = dnPid;
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
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getDtAddTime() {
		return dtAddTime;
	}
	public void setDtAddTime(String dtAddTime) {
		this.dtAddTime = dtAddTime;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
