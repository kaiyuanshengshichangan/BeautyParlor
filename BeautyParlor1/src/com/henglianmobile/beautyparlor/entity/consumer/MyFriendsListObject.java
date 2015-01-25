package com.henglianmobile.beautyparlor.entity.consumer;
/**
 * 我的好友列表对象
 * @author Administrator
 *
 */
public class MyFriendsListObject {

	private int Row;
	private int dnFid;
	private int toUserId;
	private int dnType;
	private String dcContent;
	private String dcUserName;
	private String dcNickName;
	private String dcHeadImg;
	private String dcSign;
	
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	public int getDnType() {
		return dnType;
	}
	public void setDnType(int dnType) {
		this.dnType = dnType;
	}
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
	public int getDnFid() {
		return dnFid;
	}
	public void setDnFid(int dnFid) {
		this.dnFid = dnFid;
	}
	public String getDcUserName() {
		return dcUserName;
	}
	public void setDcUserName(String dcUserName) {
		this.dcUserName = dcUserName;
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
