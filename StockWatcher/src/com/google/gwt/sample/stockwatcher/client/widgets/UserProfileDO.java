package com.google.gwt.sample.stockwatcher.client.widgets;

public class UserProfileDO {
	
	private String imageURL;
	private String name;
	private String mobile;
	private String email;
	private String web;
	private String bio;
	

	public UserProfileDO(String imageURL, String name, String mobile,
						 String email, String web, String bio) {
		super(); 
		this.imageURL = imageURL;
		this.name = name;
		this.mobile = mobile;
		this.email = email;
		this.web = web;
		this.bio = bio;
	}


	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	
	






}
