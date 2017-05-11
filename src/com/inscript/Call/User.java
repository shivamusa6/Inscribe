package com.inscript.Call;

public class User {
	
	private String name,email,password,gender,initials;

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String name,String email,String gender,String initials) {
		this.name=name;
		this.email=email;
		this.gender=gender;
		this.initials=initials;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}
}
