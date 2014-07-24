package com.lookstudio.anywhere.model;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lookstudio.anywhere.http.AbstractRequest;
import com.lookstudio.anywhere.http.Constant;
import com.lookstudio.anywhere.http.LResponse;
import com.lookstudio.anywhere.util.JsonUtil2;
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

			JSONArray json = new JSONArray(createInfo.getCoords());
//			for(int i = 0; i<createInfo.getCoords().size(); i++){
//				json.put(createInfo.getCoords().get(i));
//			}
//			json = (JSONArray) createInfo.getCoords();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("len",createInfo.getLen());
			map.put("dur",createInfo.getDur());
			map.put("coords",createInfo.getCoords());
			
            
			jsonObj.put("ori",createInfo.getOri());
			jsonObj.put("dst",createInfo.getDst());
			jsonObj.put("len",createInfo.getLen());
			jsonObj.put("dur",createInfo.getDur());
//			jsonObj.put("coords",createInfo.getCoords());
			jsonObj.put("coords",json);
			jsonObj.put("ct",createInfo.getCt());
			
			
			return getStringEntity(jsonObj);
//			jsonObj.getJSONArray("coords");
			
		} catch (JSONException e) {
			LLog.error("create track request",e);
		}catch (UnsupportedEncodingException e) {
			LLog.error("create track request",e);
		}
		
		return null;
	}
	
	@Override
	public LResponse<LCreateTrackResponse> parse(String result) {
		LLog.info("LCreateTrackResponse:" + result);
		LResponse<LCreateTrackResponse> response = new LResponse<LCreateTrackResponse>();
		try {
			JSONObject jsonObj = new JSONObject(result);
			int resultCode = jsonObj.getInt(RESULT_CODE);
			response.setResultCode(resultCode);
			if(isResultOk(resultCode))
			{
				LCreateTrackResponse res = new LCreateTrackResponse();
				res.setId(jsonObj.getString("id"));
				
				response.setBean(res);
			}
		} catch (JSONException e) {
			LLog.error("JSONException",e);
		}
			
		
		
		return response;
		
	}
	@Override
	public String getUrl() {
		
		Builder builder = new Builder();
		builder.addBaseUrl(BASE_URL);
		builder.addPath("/track/create");
		builder.addQueryString(commonQueryString(Constant.uid, Constant.sk));
		
		Log.i("url----", builder.toUrl());
		return builder.toUrl();
	}


}
