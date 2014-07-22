package com.lookstudio.anywhere.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lookstudio.anywhere.util.LLog;





/**
 * 
 * @author fanzhang
 * 
 */
public class LHttpCommunication {

	
	
	/** 瀛楃锟�*/
	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient mFMHttpClient = null;

	/**
	 * 浠嶤onnectionManager绠＄悊鐨勮繛鎺ユ睜涓彇鍑鸿繛鎺ョ殑瓒呮椂鏃堕棿锛屾澶勮缃负1锟�
	 * 瓒呮椂鎶涘嚭ConnectionPoolTimeoutException
	 */
	private static final int CONNECTION_POOL_TIMEOUT = 1000;
	/**
	 * 閫氳繃缃戠粶涓庢湇鍔″櫒寤虹珛杩炴帴鐨勮秴鏃舵椂闂达拷?Httpclient鍖呬腑閫氳繃锟�锟斤拷寮傛绾跨▼鍘诲垱寤轰笌鏈嶅姟鍣ㄧ殑socket杩炴帴锛岃繖灏辨槸璇ocket杩炴帴鐨勮秴鏃舵椂闂达紝
	 * 姝ゅ璁剧疆锟�锟�瓒呮椂鎶涘嚭ConnectionTimeoutException
	 */
	private static final int CONNECTION_TIMEOUT = 2000;
	/** Socket璇绘暟鎹殑瓒呮椂鏃堕棿锛屽嵆浠庢湇鍔″櫒鑾峰彇鍝嶅簲鏁版嵁锟�锟斤拷绛夊緟鐨勬椂闂达紝姝ゅ璁剧疆锟�锟�瓒呮椂鎶涘嚭 SocketTimeoutException */
	private static final int SOCKET_TIMEOUT = 4000;

	
	/** socket buffer size */
	private static final int SOCKET_BUFFER = 1024 * 8;

	/** http璇锋眰绔彛 */
	private static final int HTTP_PORT = 80;
	/** https璇锋眰绔彛 */
	private static final int HTTPS_PORT = 443;

	private LHttpCommunication() {

	}

	public static synchronized HttpClient getHttpClient() {
		if (null == mFMHttpClient) {
			HttpParams params = new BasicHttpParams();
		
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_0);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params,
					HttpProtocolParams.USER_AGENT);
		
			ConnManagerParams.setTimeout(params, CONNECTION_POOL_TIMEOUT);
			
			HttpConnectionParams.setConnectionTimeout(params,
					CONNECTION_TIMEOUT);
		
			HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
			HttpConnectionParams.setTcpNoDelay(params, true);
			HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER);
			
			
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), HTTP_PORT));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), HTTPS_PORT));

			
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			mFMHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return mFMHttpClient;
	}

	public static String post(LIRequest request) {
		LLog.info("post request:" + request);
		try {

			HttpPost post = new HttpPost(request.getUrl());
			
			post.addHeader("Content-Type", "application/json");  
			if (null != request.getEntity())
				post.setEntity(request.getEntity());
			
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				LLog.info("post result:" + result);
				return result;
			}
			else
			{
				LLog.warn("post error:" + response.getStatusLine().getStatusCode() + "--" + response.getStatusLine().getReasonPhrase());
			}
			
		} catch (Exception e) {
			LLog.error("POST请求出错", e);
		}
		return "";
	}

	public static String get(LIRequest request) {
		LLog.info("get request:" + request);
		HttpGet get = new HttpGet(request.getUrl());		
		HttpClient client = getHttpClient();
		try {
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					return EntityUtils.toString(response.getEntity());
				}
			} catch (Exception e) {
			LLog.error("GET请求出错", e);
		}
		
		return null;
	}
}
