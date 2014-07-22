package com.lookstudio.anywhere.model;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.lookstudio.anywhere.http.AbstractRequest;
import com.lookstudio.anywhere.http.LResponse;
import com.lookstudio.anywhere.http.AbstractRequest.Builder;
import com.lookstudio.anywhere.util.LLog;

public class LUploadNewRecordRequest extends AbstractRequest {

	private LDriveRecord record;
	public LUploadNewRecordRequest(LDriveRecord record)
	{
		this.record = record;
	}
	
	@Override
	public HttpEntity getEntity() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("ori",record.start);
			jsonObj.put("dst",record.end);
			jsonObj.put("len",record.distanceInMeter);
			jsonObj.put("dur",record.timeInSeconds);
			
			StringBuilder builder = new StringBuilder();
			for(LLocation location : record.locations)
			{
				builder.append(location.longitude + "," + location.latitude);
				builder.append(",");
			}
			
			if(builder.length() > 0 )
			{
				jsonObj.put("coords", builder.subSequence(0, builder.length() - 1).toString());
			}
			jsonObj.put("ct",record.createTime);
			
			return getStringEntity(jsonObj);
			
		} catch (JSONException e) {
			LLog.error("��ɵ�¼����ʵ�����",e);
		}catch (UnsupportedEncodingException e) {
			LLog.error("��ɵ�¼����ʵ�����",e);
		}
		
		return null;
	}
	
	@Override
	public String getUrl() {
		
		Builder builder = new Builder();
		builder.addBaseUrl(BASE_URL);
		builder.addPath("/track/create");
		
		LSessionInfo session = (LSessionInfo)LCacheManager.get().get("session");
		LLog.info("create track,userid:" + session.getUserId() + " signature:" + session.getSignature());
		builder.addQueryString(commonQueryString(session.getUserId(),session.getSignature()));
		
		return builder.toUrl();
	}

	@Override
	public LResponse<LUploadNewRecordResp> parse(String result) {
		
		LResponse<LUploadNewRecordResp> response = new LResponse<LUploadNewRecordResp>();
		
		try {
			JSONObject jsonObj = new JSONObject(result);
			response.setResultCode(getResultCode(jsonObj));
			if(isResultOk(jsonObj))
			{
				LUploadNewRecordResp resp = new LUploadNewRecordResp(jsonObj.getString("id"),true);
				response.setBean(resp);
			}
		} catch (JSONException e) {
			LLog.error("parse error", e);
		}
		return response;
	}

}
