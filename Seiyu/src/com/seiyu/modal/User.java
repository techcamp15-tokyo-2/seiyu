package com.seiyu.modal;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = -162902065250272211L;
	private String uid;
	private String name;
	private String email;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
