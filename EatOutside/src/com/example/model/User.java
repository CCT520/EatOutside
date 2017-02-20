package com.example.model;

import java.io.Serializable;
import java.util.Date;

import java.text.DateFormat;

public class User implements Serializable {
	private String userName;
	private String gender;
	private Date birth;
	public User() {
		
	}
	public User(String userName, String gender, Date birth) {
		this.userName = userName;
		this.gender = gender;
		this.birth = birth;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	@Override
	public String toString() {
		return userName + " " + gender + " " + DateFormat.getDateInstance().format(birth);
	}
	
	
}
