package com.app.shoppingzone.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	public String getUserName() {
		return username;
	}
	public void setUserName(String userName) {
		this.username = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}

