package com.lookstudio.anywhere.model;

public class LUploadNewRecordResp {

	private boolean successful;
	private String id;
	public LUploadNewRecordResp(String id,boolean successful)
	{
		this.id = id;
		this.successful = successful;
	}
	
	public String getId()
	{
		return id;
	}

	public boolean isSuccessful()
	{
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
