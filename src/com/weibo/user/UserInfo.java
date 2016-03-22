/**   
* @Title: UserInfo.java
* @Package com.weibo.user
* @Description: TODO
* @author Huaigui   
* @date 2015年11月26日 下午8:48:51
* @version V1.0   
*/
package com.weibo.user;

import java.util.ArrayList;

/**
* @ClassName: UserInfo
* @Description: 用户信息类
* @author Huaigui
* @date 2015年11月26日 下午8:48:51
* 
*/
public class UserInfo {
	private String ID= "";				//ID号
	private String Addr= "";				//Addr-省-城市
	private String Nickname = "";		//昵称
	private String Certification = "";	//认证
	private String Sex= "";				// 性别,男-1  女-0 ,无性别--1
	private String BlogAddr = "";		//博客地址
	/** Domainhacks : 域名地址*/
	private String Domainhacks = "";
	private String Birthday  = "";		//生日
	private String RegTime = "";		//注册时间
	private String Brief  = "";			// 简介
	private String Label  = "";			//标签
	private String EducationBack = "";				// 学习经历
	private String Grade = "";				//会员等级
	private String Bagde = "";				//勋章信息
	private String FollowNum = "";				//关注人数
	private String Company = "";				//工作信息
	private String FollowList =""; 	//关注列表  是一个网址
	private String FansNum = "";	//粉丝人数
	private String FansList = "";	//粉丝列表
	private String PublishNum = "";	
	/** @Fields PublishList : 发表列表*/
	private String PublishList = ""; //发表微博数量
	private String FansFollowList="";//关注他的人还关注
	/** others : 其他信息*/
	private String others = "";
	

/*************************************************************/
	/** 
	* @Description: 设置和获取对象
	* @param @param str 
	*/
	/**  @return iD */ 
	public String getID() {
		return ID;
	}
	/** @param iD 要设置的 iD */ 
	public void setID(String iD) {
		ID = iD;
	}
	public void setNickname(String str) {
		this.Nickname = str;
	}
	public String getNickname() {
		return this.Nickname;
	}
	
	public void setRegTime(String str) {
		this.RegTime = str;
	}
	public String getRegTime() {
		return this.RegTime;
	}
	
	public void setCompany(String str) {
		this.Company = str;
	}
	public String getCompany() {
		return this.Company;
	}
	public void setCertification(String str) {
		this.Certification = str;
	}
	public String getCertification() {
		return this.Certification;		
	}
	
	public void setSex(String str) {
		this.Sex = str;
	}
	public String getSex() {
		return this.Sex;
	}
	
	public void setBlogAddr(String str) {
		this.BlogAddr = str;
	}
	public String getBlogAddr() {
		return this.BlogAddr;
	}
	
	public void setDomainhacks(String str) {
		this.Domainhacks = str;
	}
	public String getDomainhacks() {
		return this.Domainhacks;
	}
	
	public void setBrief(String str) {
		this.Brief = str;
	}
	public String getBrief() {
		return this.Brief;
	}
	
	public void setAddr(String str) {
		this.Addr = str;
	}
	public String getAddr() {
		return this.Addr;	
	}
	
	public void setLabel(String str) {
		this.Label = str;
	}
	public String getLabel() {
		return this.Label;
	}
	
	public void setBirthday(String str) {
		this.Birthday = str;
	}
	public String getBirthday() {
		return this.Birthday;
	}
	
	public void setEducationBack(String str) {
		if (this.EducationBack.equals("")) 
			this.EducationBack= str;
		else 
			this.EducationBack= this.EducationBack+" "+str;
	}
	public String getEducationBack() {
		return this.EducationBack;
	}
	
	public void setGrade(String str) {
		this.Grade = str;
	}
	public String getGrade() {
		return this.Grade;
	}
	
	public void setBagde(String str) {
		if(this.Bagde.equals(""))
			this.Bagde = str;
		else 
			this.Bagde = this.Bagde+","+str;
	}
	public String getBagde() {
		return this.Bagde;
	}
	
	public void setFollowNum(String str) {
		this.FollowNum = str;
	}
	public String getFollowNum() {
		return this.FollowNum;
	}
	
	public void setFollowList(String str) {
		this.FollowList = str;
	}
	public String getFollowList() {
		return this.FollowList;
	}
	
	public void setFansNum(String str) {
		this.FansNum = str;
	}
	public String getFansNum() {
		return this.FansNum;
	}
	
	public void setFansList(String str) {
		this.FansList = str;
	}
	public String getFansList() {
		return this.FansList;
	}
	
	public void setFansFollowList(String str) {
		this.FansFollowList = str;
	}
	public String getFansFollowList() {
		return this.FansFollowList;
	}
	
	public void setPublishNum(String str) {
		this.PublishNum= str;
	}
	public String getPublishNum() {
		return this.PublishNum;
	}

	public void setothers(String str) {
		if(this.others.equals(""))
			this.others = str;
		else 
			this.others= this.others+" | "+str;
	}
	public String getothers() {
		return this.others;
	}
	/**  @return publishList */ 
	public String getPublishList() {
		return PublishList;
	}
	/** @param publishList 要设置的 publishList */ 
	public void setPublishList(String publishList) {
		PublishList = publishList;
	}


}
