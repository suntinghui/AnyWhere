package com.lookstudio.anywhere.chweather;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.whether.LWeatherInfo;

import android.util.Log;

public class WeatherQueryManageImpl implements WeatherQueryManage {
	private final String TAG = "message";
	
	
	@Override
	public LWeatherInfo weatherquery(String CityId) {
		
		LWeatherInfo weather = new LWeatherInfo();

		String URL = "http://m.weather.com.cn/data/"+CityId+".html";
		String Weather_Result="";
		HttpGet httpRequest = new HttpGet(URL);

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
				Weather_Result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());

			return weather;
	}

	if(null!=Weather_Result&&!"".equals(Weather_Result)){
			try {
				JSONObject JO = new JSONObject(Weather_Result).getJSONObject("weatherinfo");
				//String date_y = JO.getString("date_y");
				
				weather.isValid = true;
				weather.date = LWeatherUtils.currentDate();
				weather.weatherCode = JO.getString("weather1");
				weather.weekday = LWeatherUtils.getWeekday(LApplication.appContext);
	
				String temp = JO.getString("temp1");
				int index = temp.indexOf("~");
				if(0 < index)
				{
					weather.temp_C = temp.substring(0,index);
				}
				else
				{
					weather.temp_C = temp;
				}
				
				weather.windDir = JO.getString("fx1");
				weather.windspeedKmph = Double.valueOf(JO.getString("fl1"));
				} catch (JSONException e) {
					return weather;
			}
		}
		return weather;
	}

}
