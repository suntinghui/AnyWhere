package com.lookstudio.anywhere.model;

public class LRegisterResponse {

	private String userId;
	private String signature;
	
	public LRegisterResponse(){}
	public LRegisterResponse(String userId, String signature) {
		super();
		this.userId = userId;
		this.signature = signature;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "LRegisterResponse [userId=" + userId + ", signature="
				+ signature + "]";
	}

	
}
