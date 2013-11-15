package com.seiyu.modal;

import java.util.List;

public class RecommendItem {

	private String userId;
	private String userName;
	private String email;
	private List<RecommendPicItem> infos;
	
	public RecommendItem(String userId,String userName,String email,List<RecommendPicItem> infos){
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.infos = infos;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<RecommendPicItem> getInfos() {
		return infos;
	}
	public void setInfos(List<RecommendPicItem> infos) {
		this.infos = infos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
