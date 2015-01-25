package com.henglianmobile.beautyparlor.entity.consumer;
/**
 * 消费者发布征求方案时的选择分类，如：脸部整容。。。
 * @author Administrator
 *
 */
public class ProjectType {

	private int id;
	private String typeName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
