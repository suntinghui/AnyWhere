package com.lookstudio.anywhere.model;


import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.lookstudio.anywhere.http.AbstractRequest;
import com.lookstudio.anywhere.http.LResponse;
import com.lookstudio.anywhere.util.LLog;

public class LCreateTrackRequest extends AbstractRequest{

	private LCreateTrackInfo createInfo;
	
	
	
	public LCreateTrackRequest(){}
	public LCreateTrackRequest(LCreateTrackInfo createInfo){
		this.createInfo = createInfo;
	}
	
	@Override
	public HttpEntity getEntity() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("ori",createInfo.getOri());
			jsonObj.put("dst",createInfo.getDst());
			jsonObj.put("len",createInfo.getLen());
			jsonObj.put("dur",createInfo.getDur());
			jsonObj.put("coords",createInfo.getCoords());
			jsonObj.put("ct",createInfo.getCt());
			
			return getStringEntity(jsonObj);
			
		} catch (JSONException e) {
			LLog.error("create track request",e);
		}catch (UnsupportedEncodingException e) {
			LLog.error("create track request",e);
		}
		
		return null;
	}
	
	@Override
	public LResponse<LRegisterResponse> parse(String result) {
		LLog.info("ע����Ӧ���:" + result);
		LResponse<LRegisterResponse> response = new LResponse<LRegisterResponse>();
		try {
			JSONObject jsonObj = new JSONObject(result);
			int resultCode = jsonObj.getInt(RESULT_CODE);
			response.setResultCode(resultCode);
			if(isResultOk(resultCode))
			{
				LRegisterResponse res = new LRegisterResponse();
				res.setUserId(jsonObj.getString("uid"));
				res.setSignature(jsonObj.getString("sk"));
				
				response.setBean(res);
			}
		} catch (JSONException e) {
			LLog.error("����ע�������",e);
		}
			
		
		
		return response;
		
	}
	@Override
	public String getUrl() {
		
		Builder builder = new Builder();
		builder.addBaseUrl(BASE_URL);
		builder.addPath("/account/create");
		builder.addQueryString(commonQueryString());
		
		return builder.toUrl();
	}


}
