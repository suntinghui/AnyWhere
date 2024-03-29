package com.lookstudio.anywhere.model;

import java.io.Serializable;

public class LRegisterInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3694316176929323804L;
	private String username;
	private String password;
	
	public LRegisterInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LRegisterInfo(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public LLoginInfo getLoginInfo()
	{
		return new LLoginInfo(username,password);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LRegisterInfo other = (LRegisterInfo) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "LRegisterInfo [username=" + username + ", password=" + password
				+ "]";
	}
	
}
