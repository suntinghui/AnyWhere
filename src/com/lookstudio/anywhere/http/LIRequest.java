package com.lookstudio.anywhere.http;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpEntity;

/**
 * 
 * @author fanzhang
 * 
 */
public interface LIRequest {

	
	public static final String RESULT_CODE = "rc";
	
	
	public static final String GET = "get";
	public static final String POST = "post";
	
	public static final int RESULT_OK = 1;


	/**
	 * 请求实体，POST专用
	 * @return
	 */
	HttpEntity   getEntity();
	
	/**
	 * http://域地址:端口/引用?queryString
	 * 
	 * @return
	 */
	String getUrl();

	<T> LResponse<T> parse(String result);
}
