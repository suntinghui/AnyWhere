package com.lookstudio.anywhere.http;

/**
 * 
 * @author fanzhang
 * 
 * @param <T>
 */
public class LResponse<T> {

	private int mResultCode = 9003;

	private String mMsg = null;
	private boolean isResult_OK = false;
	
	private T bean = null;

	public int getResultCode() {
		return mResultCode;
	}

	public boolean isResultOk()
	{
		return LIRequest.RESULT_OK == mResultCode;
	}
	
	public void setResultCode(int resultCode) {
		this.mResultCode = resultCode;
	}

	public String getMsg() {
		return mMsg;
	}

	public void setMsg(String msg) {
		this.mMsg = msg;
	}

	public T getBean() {
		return bean;
	}

	public void setBean(T bean) {
		this.bean = bean;
	}

}
