package com.lookstudio.anywhere.model;


import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.lookstudio.anywhere.http.AbstractRequest;
import com.lookstudio.anywhere.http.Constant;
import com.lookstudio.anywhere.http.LResponse;
import com.lookstudio.anywhere.util.LLog;

public class LLoginRequest extends AbstractRequest {

	private LLoginInfo loginInfo;
	
	public LLoginRequest(LLoginInfo loginInfo)
	{
		this.loginInfo = loginInfo;
	}
	
	@Override
	public HttpEntity getEntity() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("account",loginInfo.getUsername());
			jsonObj.put("password",loginInfo.getPassword());
			
			return getStringEntity(jsonObj);
			
		} catch (JSONException e) {
			LLog.error("get login entity error",e);
		}catch (UnsupportedEncodingException e) {
			LLog.error("get login entity error",e);
		}
		
		return null;
	}

	@Override
	public LResponse<LLoginResponse> parse(String result) {
		LLog.info("log in result = " + result);
		LResponse<LLoginResponse> response = new LResponse<LLoginResponse>();
		
		try {
			JSONObject jsonObj = new JSONObject(result);
			response.setResultCode(getResultCode(jsonObj));
			if(isResultOk(jsonObj))
			{
				LLoginResponse res = new LLoginResponse();
				res.setUserId(jsonObj.getString("uid"));
				res.setSignature(jsonObj.getString("sk"));
				Constant.uid = jsonObj.getString("uid");
				Constant.sk = jsonObj.getString("sk");
				
				response.setBean(res);
			}
		} catch (JSONException e) {
			LLog.error("error on parse:" + result, e);
		}
		return response;
	}

	@Override
	public String getUrl() {
		
		Builder builder = new Builder();
		builder.addBaseUrl(BASE_URL);
		builder.addPath("/session/login");
		builder.addQueryString(commonQueryString());
		
		return builder.toUrl();
	}

	

}
