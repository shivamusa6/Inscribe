package com.inscript.Call;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserSessionProfile {
	private String id, email, date;

	public UserSessionProfile(String email) {
		this.email = email;
		
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		this.date = df.format(dateobj);
		
		String[] getValuesOf = {"id", "initials"};
		String userData = Clear.getVariables(email, getValuesOf);

		String[] parts = this.date.split("/");
		double today = Double.parseDouble(parts[0]);
		today = Math.atan(today);
		try {
			String hash = Clear.hash(String.valueOf(today)+				//date
					userData.split(":")[0]+
					parts[1]+											//month
					userData.split(":")[1]+
					parts[2]);											//year
		
			this.id = hash.split(":", 2)[1];
		} catch (Exception e){
			this.id = null;
			e.printStackTrace();
		}
		
	}
	
	public UserSessionProfile(String email, String date) {
		this.email = email;
		
		this.date = date;
		
		String[] getValuesOf = {"id", "initials"};
		String userData = Clear.getVariables(email, getValuesOf);

		String[] parts = this.date.split("/");
		double today = Double.parseDouble(parts[0]);
		today = Math.atan(today);										// TODO user some other function than atan()
		try {
			String hash = Clear.hash(String.valueOf(today)+				//date
					userData.split(":")[0]+
					parts[1]+											//month
					userData.split(":")[1]+
					parts[2]);											//year
		
			this.id = hash.split(":", 2)[1];
		} catch (Exception e){
			this.id = null;
			e.printStackTrace();
		}
		
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getDate() {
		return date;
	}
}
