package com.henglianmobile.beautyparlor.entity;

import java.io.Serializable;

/**
 * �û�������Ϣ����
 * @author Administrator
 *
 */
public class UserInfoDetailObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1005305371619531149L;
	/** �û�ID */
	private int dnUserid;
	/** �û��� */
	private String dcUserName;
	/** �ǳ� */
	private String dcNickName;
	/** �û����� 1����Ժ�� 2�����ߡ�3��ͨ�û���4ҵ��Ա */
	private int dnType;
	/** ���� */
	private String dcRealName;
	/** �ֻ��� */
	private String dcCellPhone;
	/** �Ա� */
	private int sex;
	/** ���� */
	private int age;
	/** ��ַ */
	private String address;
	/** ��ַ */
	private String dcEmail;
	/** ͷ�� */
	private String dcHeadImg;
	/** ���� */
	private String dcBackImg;
	/** ǩ�� */
	private String dcSign;
	/** ��ע */
	private String dcContent;
	/** ������Ϣ */
	private String bankName;
	/** �˻� */
	private String accountNo;
	/** ����Ժ�Ƿ����ͨ����0δ��� 1���� 2ͣ�ã� */
	private int dnIsShow;

	/** ��ϵ�绰 */
	private String dcPhone;
	/** ����Ա���� */
	private String adminName;
	/** ��˾���� */
	private String peopleNum;
	/** ��ʦ���� */
	private String technicName;
	/** ��ʦ��Ƭ */
	private String techniImg;
	/** ��˾��Ƭ */
	private String companyImg;
	/** Ӫҵִ�� */
	private String licenceImg;
	/** �ʸ�֤�� */
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
