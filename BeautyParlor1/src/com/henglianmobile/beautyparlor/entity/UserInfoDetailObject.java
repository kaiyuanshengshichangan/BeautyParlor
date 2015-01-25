package com.henglianmobile.beautyparlor.entity;

import java.io.Serializable;

/**
 * 用户个人信息对象
 * @author Administrator
 *
 */
public class UserInfoDetailObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1005305371619531149L;
	/** 用户ID */
	private int dnUserid;
	/** 用户名 */
	private String dcUserName;
	/** 昵称 */
	private String dcNickName;
	/** 用户类型 1美容院、 2管理者、3普通用户、4业务员 */
	private int dnType;
	/** 姓名 */
	private String dcRealName;
	/** 手机号 */
	private String dcCellPhone;
	/** 性别 */
	private int sex;
	/** 年龄 */
	private int age;
	/** 地址 */
	private String address;
	/** 地址 */
	private String dcEmail;
	/** 头像 */
	private String dcHeadImg;
	/** 背景 */
	private String dcBackImg;
	/** 签名 */
	private String dcSign;
	/** 备注 */
	private String dcContent;
	/** 银行信息 */
	private String bankName;
	/** 账户 */
	private String accountNo;
	/** 美容院是否审核通过（0未审核 1正常 2停用） */
	private int dnIsShow;

	/** 联系电话 */
	private String dcPhone;
	/** 管理员名字 */
	private String adminName;
	/** 公司人数 */
	private String peopleNum;
	/** 技师姓名 */
	private String technicName;
	/** 技师照片 */
	private String techniImg;
	/** 公司照片 */
	private String companyImg;
	/** 营业执照 */
	private String licenceImg;
	/** 资格证书 */
	private String certImg;
	
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
	public String getDcNickName() {
		return dcNickName;
	}
	public void setDcNickName(String dcNickName) {
		this.dcNickName = dcNickName;
	}
	public int getDnType() {
		return dnType;
	}
	public void setDnType(int dnType) {
		this.dnType = dnType;
	}
	public String getDcRealName() {
		return dcRealName;
	}
	public void setDcRealName(String dcRealName) {
		this.dcRealName = dcRealName;
	}
	public String getDcCellPhone() {
		return dcCellPhone;
	}
	public void setDcCellPhone(String dcCellPhone) {
		this.dcCellPhone = dcCellPhone;
	}
	public String getDcPhone() {
		return dcPhone;
	}
	public void setDcPhone(String dcPhone) {
		this.dcPhone = dcPhone;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDcEmail() {
		return dcEmail;
	}
	public void setDcEmail(String dcEmail) {
		this.dcEmail = dcEmail;
	}
	public String getDcHeadImg() {
		return dcHeadImg;
	}
	public void setDcHeadImg(String dcHeadImg) {
		this.dcHeadImg = dcHeadImg;
	}
	public String getDcBackImg() {
		return dcBackImg;
	}
	public void setDcBackImg(String dcBackImg) {
		this.dcBackImg = dcBackImg;
	}
	public String getDcSign() {
		return dcSign;
	}
	public void setDcSign(String dcSign) {
		this.dcSign = dcSign;
	}
	public String getDcContent() {
		return dcContent;
	}
	public void setDcContent(String dcContent) {
		this.dcContent = dcContent;
	}
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public int getDnIsShow() {
		return dnIsShow;
	}
	public void setDnIsShow(int dnIsShow) {
		this.dnIsShow = dnIsShow;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getPeopleNum() {
		return peopleNum;
	}
	public void setPeopleNum(String peopleNum) {
		this.peopleNum = peopleNum;
	}
	public String getTechnicName() {
		return technicName;
	}
	public void setTechnicName(String technicName) {
		this.technicName = technicName;
	}
	public String getTechniImg() {
		return techniImg;
	}
	public void setTechniImg(String techniImg) {
		this.techniImg = techniImg;
	}
	public String getCompanyImg() {
		return companyImg;
	}
	public void setCompanyImg(String companyImg) {
		this.companyImg = companyImg;
	}
	public String getLicenceImg() {
		return licenceImg;
	}
	public void setLicenceImg(String licenceImg) {
		this.licenceImg = licenceImg;
	}
	public String getCertImg() {
		return certImg;
	}
	public void setCertImg(String certImg) {
		this.certImg = certImg;
	}
	
	
}
