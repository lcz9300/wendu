package com.HWDTEMPT.model;

public class UserInfo {

	private int id;
	private String userName,userPwd,phone,userAge,userSex,userWeight,userHeight,userNum;
	
    public String getUserNum() {
        return userNum;
    }
    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPwd() {
        return userPwd;
    }
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getUserAge() {
        return userAge;
    }
    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }
    public String getUserSex() {
        return userSex;
    }
    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }
    public String getUseWeight() {
        return userWeight;
    }
    public void setUseWeight(String useWeight) {
        this.userWeight = useWeight;
    }
    public String getUserHeight() {
        return userHeight;
    }
    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }
    public UserInfo() {
		super();	
	}
    
    
    
	public UserInfo(String userName, String userPwd,String phone,String height,String weight,
		 String userAge, String userSex,String userNum) {
		super();
		this.phone=phone;
		this.userName = userName;
		this.userPwd = userPwd;
		this.userAge = userAge;
		this.userSex = userSex;
		this.userHeight=height;
		this.userWeight=weight;
		this.userNum = userNum;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", userName=" + userName + ", userPwd="
				+ userPwd + ", phone=" + phone + ",height=" + userHeight + ",weight=" + userWeight + ", userAge=" + userAge
				+ ", userNum= "+ userNum + ", userSex=" + userSex + "]";
	}
	
	
}
