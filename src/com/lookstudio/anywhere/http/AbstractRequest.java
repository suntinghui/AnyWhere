package com.lookstudio.anywhere.http;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.lookstudio.anywhere.util.LLog;

public abstract class AbstractRequest implements LIRequest {

	public static final String CHARSET = "utf-8";
	public static final String CLIENTID_IOS = "10";
	public static final String CLENTID_ANDROID = "20";
	
	public static final String VER = "v";
	public static final String CLIENT_ID  = "cid";
	public static final String USER_ID    = "uid";
	public static final String SIGNATURE  = "sig";
	public static final String TIME       = "ts";
	
	public static final String BASE_URL = "http://115.47.56.228:80/motorapp/api";
	
	@Override
	public HttpEntity getEntity() {
		
		return null;
	}

	protected boolean isResultOk(JSONObject jsonObj) throws JSONException
	{
		return isResultOk(jsonObj.getInt(RESULT_CODE));
	}
	
	protected int getResultCode(JSONObject jsonObj)throws JSONException
	{
		return jsonObj.getInt(RESULT_CODE);
	}
	
	protected boolean isResultOk(int resultCode)
	{
		return RESULT_OK == resultCode;
	}
	
	protected HttpEntity getStringEntity(JSONObject obj) throws UnsupportedEncodingException
	{
		String entityString = obj.toString();
		LLog.info("getStringEntity:" + entityString);
		StringEntity entity = new StringEntity(entityString);
		return entity;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("url:" + getUrl());
		builder.append("\n");
		builder.append("entity:" + getEntity());
		
		return builder.toString();
	}
	
	protected String commonQueryString() {
		HashMap<String, String> map = new HashMap<String,String>();
		map.put(VER, "1");
		map.put(CLIENT_ID, CLENTID_ANDROID);
		map.put(TIME,"" + System.currentTimeMillis());
		
		return toString(map);
	}

	protected String commonQueryString(String userId,String sk) {
		HashMap<String, String> map = new HashMap<String,String>();
		map.put(VER, "1");
		map.put(CLIENT_ID, CLENTID_ANDROID);
		map.put(USER_ID,userId);
		map.put(SIGNATURE,getSignature(userId,sk,System.currentTimeMillis()));
		map.put(TIME,"" + System.currentTimeMillis());
		
		return toString(map);
	}
	
	private String getSignature(String userId,String sk,long ts)
	{
		try {
			String sig = md5(userId + "#" + sk + "#" + ts);
			LLog.info("getSignature = " + sig);
			return sig;
		} catch (NoSuchAlgorithmException e) {
			LLog.error("signature error",e);
		}
		
		return "";
	}
	
	private String md5(String value) throws NoSuchAlgorithmException
	{
		MessageDigest digester = MessageDigest.getInstance("MD5");
		digester.update(value.getBytes());
		
		return byteArrayToHex(digester.digest());
	}
	
	   private String byteArrayToHex(byte[] byteArray) {

		      // 首先初始化一个字符数组，用来存放每个16进制字符

		      char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };

		 

		      // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

		      char[] resultCharArray =new char[byteArray.length * 2];

		 

		      // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

		      int index = 0;

		      for (byte b : byteArray) {

		         resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];

		         resultCharArray[index++] = hexDigits[b& 0xf];

		      }

		 

		      // 字符数组组合成字符串返回

		      return new String(resultCharArray);

		}
	protected String toString(HashMap<String, String> params)
	{

		ArrayList<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
		String param = null;
		if (null != params) {
			for (Entry<String, String> entry : params.entrySet()) {
				NameValuePair tmp = new BasicNameValuePair(entry.getKey(),
						entry.getValue());
				formparams.add(tmp);
			}

			param = URLEncodedUtils.format(formparams, CHARSET);
		}
		return param;
	}

	
	public class Builder
	{
		String baseUrl = "";
		String path = "";
		String queryString = "";
		
		public Builder(){}
		public Builder(String baseUrl,String path,String queryString)
		{
			this.baseUrl = baseUrl;
			this.path      = path;
			this.queryString = queryString;
		}
		
		public Builder addBaseUrl(String baseUrl)
		{
			this.baseUrl = baseUrl;
			return this;
		}
		
		public Builder addPath(String path)
		{
			this.path = path;
			return this;
		}
		
		public Builder addQueryString(String queryString)
		{
			this.queryString = queryString;
			return this;
		}
		
		public String toUrl()
		{
			return baseUrl + path + "?" + queryString;
		}
	}
}
