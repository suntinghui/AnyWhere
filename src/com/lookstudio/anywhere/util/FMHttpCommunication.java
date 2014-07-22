package com.lookstudio.anywhere.util;

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

import android.util.Log;



/**
 * 
 * @author fanzhang
 * 
 */
public class FMHttpCommunication {

	/** 字符�?*/
	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient mFMHttpClient = null;

	/**
	 * 从ConnectionManager管理的连接池中取出连接的超时时间，此处设置为1�?
	 * 超时抛出ConnectionPoolTimeoutException
	 */
	private static final int CONNECTION_POOL_TIMEOUT = 1000;
	/**
	 * 通过网络与服务器建立连接的超时时间�?Httpclient包中通过�?��异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间，
	 * 此处设置�?�?超时抛出ConnectionTimeoutException
	 */
	private static final int CONNECTION_TIMEOUT = 2000;
	/** Socket读数据的超时时间，即从服务器获取响应数据�?��等待的时间，此处设置�?�?超时抛出 SocketTimeoutException */
	private static final int SOCKET_TIMEOUT = 4000;

	/** socket buffer size */
	private static final int SOCKET_BUFFER = 1024 * 8;

	/** http请求端口 */
	private static final int HTTP_PORT = 80;
	/** https请求端口 */
	private static final int HTTPS_PORT = 443;

	private FMHttpCommunication() {

	}

	public static synchronized HttpClient getHttpClient() {
		if (null == mFMHttpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置�?��基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params,
					HttpProtocolParams.USER_AGENT);
			// 超时设置
			/* 从连接池中取连接的超时时�?*/
			ConnManagerParams.setTimeout(params, CONNECTION_POOL_TIMEOUT);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params,
					CONNECTION_TIMEOUT);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
			HttpConnectionParams.setTcpNoDelay(params, true);
			HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER);

			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), HTTP_PORT));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), HTTPS_PORT));

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			mFMHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return mFMHttpClient;
	}

	public static byte[] post(String url, HashMap<String, String> params) {
		if (null == url)
			return null;
		try {
			// 编码参数
			ArrayList<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
			UrlEncodedFormEntity entity = null;
			if (null != params) {
				for (Entry<String, String> entry : params.entrySet()) {
					NameValuePair tmp = new BasicNameValuePair(entry.getKey(),
							entry.getValue());
					formparams.add(tmp);
				}
				entity = new UrlEncodedFormEntity(formparams, CHARSET);
			}
			// 创建POST请求
			HttpPost request = new HttpPost(url);
			if (null != entity)
				request.setEntity(entity);
			// 发�?请求
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("request failed");
			}
			HttpEntity resEntity = response.getEntity();
			return (null == resEntity) ? null : EntityUtils
					.toByteArray(resEntity);
		} catch (Exception e) {
			return null;
		}

	}

	public static byte[] get(String url, HashMap<String, String> params) {
		if (null == url)
			return null;
		ArrayList<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
		String param = null;
		if (null != params) {
			for (Entry<String, String> entry : params.entrySet()) {
				NameValuePair tmp = new BasicNameValuePair(entry.getKey(),
						entry.getValue());
				formparams.add(tmp);
			}
			// 对参数编�?
			param = URLEncodedUtils.format(formparams, CHARSET);
		}
		// 创建GET请求
		Log.i("Http_get", url + (param != null ? "?" + param : ""));
		HttpGet request = new HttpGet(url + (param != null ? "?" + param : ""));
		
		HttpClient client = getHttpClient();
		try {
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("request failed");
			}
			HttpEntity resEntity = response.getEntity();
			return (null == resEntity) ? null : EntityUtils
					.toByteArray(resEntity);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
