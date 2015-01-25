package com.henglianmobile.beautyparlor.entity;
/**
 * 美友圈评论列表对象
 * @author Administrator
 *
 */
public class MeiYouQuanCommentListObject {
	/** 数据id */
	private int dnCid;
	/** 帖子id */
	private int dnTieId;
	/** 评论人的id */
	private int dnFaid;
	/** 评论人的用户昵称 */
	private String dcFaName;
	/** 评论内容 */
	private String dcComment;
	/** 评论人头像 */
	private String dcHeadImg;
	/** 添加时间 */
	private String dtAddTime;
	public int getDnCid() {
		return dnCid;
	}
	public void setDnCid(int dnCid) {
		this.dnCid = dnCid;
	}
	public int getDnTieId() {
		return dnTieId;
	}
	public void setDnTieId(int dnTieId) {
		this.dnTieId = dnTieId;
	}
	public int getDnFaid() {
		return dnFaid;
	}
	public void setDnFaid(int dnFaid) {
		this.dnFaid = dnFaid;
	}
	public String getDcFaName() {
		return dcFaName;
	}
	public void setDcFaName(String dcFaName) {
		this.dcFaName = dcFaName;
	}
	public String getDcComment() {
		return dcComment;
	}
	public void setDcComment(String dcComment) {
		this.dcComment = dcComment;
	}
	public String getDcHeadImg() {
		return dcHeadImg;
	}
	public void setDcHeadImg(String dcHeadImg) {
		this.dcHeadImg = dcHeadImg;
	}
	public String getDtAddTime() {
		return dtAddTime;
	}
	public void setDtAddTime(String dtAddTime) {
		this.dtAddTime = dtAddTime;
	}
}
