package com.seiyu.modal;

import java.io.Serializable;

public class FeedItem implements Serializable{

	
	private static final long serialVersionUID = 4377564304887019262L;
	private String imageUrl;
	private String seiyuName;
	private String seiyuId;
	private String timeSmap;
	private String blogUrl;
	private String gender;
	private String followed;
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSeiyuName() {
		return seiyuName;
	}
	public void setSeiyuName(String seiyuName) {
		this.seiyuName = seiyuName;
	}
	public String getSeiyuId() {
		return seiyuId;
	}
	public void setSeiyuId(String seiyuId) {
		this.seiyuId = seiyuId;
	}
	public String getTimeSmap() {
		return timeSmap;
	}
	public void setTimeSmap(String timeSmap) {
		this.timeSmap = timeSmap;
	}
	public String getBlogUrl() {
		return blogUrl;
	}
	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getFollowed() {
		return followed;
	}
	public void setFollowed(String followed) {
		this.followed = followed;
	}
	
}
