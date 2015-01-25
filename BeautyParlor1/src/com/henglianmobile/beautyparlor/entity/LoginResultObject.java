package com.henglianmobile.beautyparlor.entity;
/**
 * 此类为登录返回结果对象
 * @author Administrator
 *
 */
public class LoginResultObject {
	private String response;
	private String uid;
	private String uname;
	private String nickname;
	private String utype;
	private String ushow;
	
	public String getUshow() {
		return ushow;
	}
	public void setUshow(String ushow) {
		this.ushow = ushow;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUtype() {
		return utype;
	}
	public void setUtype(String utype) {
		this.utype = utype;
	}
}
