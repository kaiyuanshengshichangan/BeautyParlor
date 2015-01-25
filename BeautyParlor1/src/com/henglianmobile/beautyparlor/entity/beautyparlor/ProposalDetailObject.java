package com.henglianmobile.beautyparlor.entity.beautyparlor;

import java.io.Serializable;

/**
 * 方案详情对象
 * @author Administrator
 *
 */
public class ProposalDetailObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1095436918926050228L;
	private int dnPid;
	private int dnUserid;
	private int dcClass;
	private String dcProTitle;
	private String dcContent;
	private String dtAddTime;
	private String typeName;
	private String dcNickName;
	private String dcCellPhone;
	private String dcHeadImg;
	private String toNickName;
	private String dcIpath;
	private int likeCount;
	private String toHeadImg;
	
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public String getToHeadImg() {
		return toHeadImg;
	}
	public void setToHeadImg(String toHeadImg) {
		this.toHeadImg = toHeadImg;
	}
	public int getDnPid() {
		return dnPid;
	}
	public void setDnPid(int dnPid) {
		this.dnPid = dnPid;
	}
	public int getDnUserid() {
		return dnUserid;
	}
	public void setDnUserid(int dnUserid) {
		this.dnUserid = dnUserid;
	}
	public int getDcClass() {
		return dcClass;
	}
	public void setDcClass(int dcClass) {
		this.dcClass = dcClass;
	}
	public String getDcProTitle() {
		return dcProTitle;
	}
	public void setDcProTitle(String dcProTitle) {
		this.dcProTitle = dcProTitle;
	}
	public String getDcContent() {
		return dcContent;
	}
	public void setDcContent(String dcContent) {
		this.dcContent = dcContent;
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
	public String getDcNickName() {
		return dcNickName;
	}
	public void setDcNickName(String dcNickName) {
		this.dcNickName = dcNickName;
	}
	public String getDcCellPhone() {
		return dcCellPhone;
	}
	public void setDcCellPhone(String dcCellPhone) {
		this.dcCellPhone = dcCellPhone;
	}
	public String getDcHeadImg() {
		return dcHeadImg;
	}
	public void setDcHeadImg(String dcHeadImg) {
		this.dcHeadImg = dcHeadImg;
	}
	public String getToNickName() {
		return toNickName;
	}
	public void setToNickName(String toNickName) {
		this.toNickName = toNickName;
	}
	public String getDcIpath() {
		return dcIpath;
	}
	public void setDcIpath(String dcIpath) {
		this.dcIpath = dcIpath;
	}
	
}
