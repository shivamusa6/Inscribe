package com.inscript.Call;

public class MultiUser {

	private String dob,mobile,profileURL,memberType;
	private int uid,instituteID,courseID,semesterID;

	public MultiUser() {
		// TODO Auto-generated constructor stub
	}
	
	public MultiUser(int uid,String dob,String mobile,String profileURL,int instituteID,int courseID,int semesterID,String memberType) {
		this.uid=uid;
		this.dob=dob;
		this.mobile=mobile;
		this.profileURL=profileURL;
		this.instituteID=instituteID;
		this.courseID=courseID;
		this.semesterID=semesterID;
		this.memberType=memberType;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProfileURL() {
		return profileURL;
	}

	public void setProfileURL(String profileURL) {
		this.profileURL = profileURL;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getInstituteID() {
		return instituteID;
	}

	public void setInstituteID(int instituteID) {
		this.instituteID = instituteID;
	}

	public int getCourseID() {
		return courseID;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}

	public int getSemesterID() {
		return semesterID;
	}

	public void setSemesterID(int semesterID) {
		this.semesterID = semesterID;
	}
}
