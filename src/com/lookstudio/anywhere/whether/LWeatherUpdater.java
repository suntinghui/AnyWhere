package com.lookstudio.anywhere.whether;




import com.lookstudio.anywhere.chweather.LWeatherUtils;
import com.lookstudio.anywhere.chweather.WeatherForm;
import com.lookstudio.anywhere.chweather.WeatherQueryManage;
import com.lookstudio.anywhere.chweather.WeatherQueryManageImpl;
import com.lookstudio.anywhere.util.ToastUtil;

import android.content.Context;
import android.os.Handler;


public class LWeatherUpdater {

	private LocalWeather.Data weather;
	private LocationSearch.Data loc;
	private Handler uiHandler;
	private Context context;
	private static boolean isUpdating = false;
	private OnGotWeatherInfoListener listener;
	private String cityId;
	public interface OnGotWeatherInfoListener
	{
		public void onGot(LWeatherInfo info);
	}
	
	public LWeatherUpdater(Handler uiHandler,Context context)
	{
		this.uiHandler = uiHandler;
		this.context = context;
	}
	
	public void setOnGotWeatherInfoListener(OnGotWeatherInfoListener listener)
	{
		this.listener = listener;
	}
	
	public static boolean isUpdating()
	{
		return isUpdating;
	}
	
	public boolean update(String cityName)
	{
		if((null == cityName) || ("".equals(cityName)))
		{
			return false;
		}
		
		String tempCityId = LWeatherUtils.findCityCodeByName(cityName, context);
		if(null != tempCityId)
		{
			cityId = tempCityId;
		}
		else
		{
			String name = cityName.substring(0,cityName.length() - 1);
			tempCityId = LWeatherUtils.findCityCodeByName(name,context);
			if(null != tempCityId)
			{
				cityId = tempCityId;
			}
			else
			{
				return false;
			}
		}
		
		isUpdating = true;
		new Thread()
		{
			@Override
			public void run()
			{
				WeatherForm[] WF = new WeatherForm[3];
				WeatherQueryManage WQM = new WeatherQueryManageImpl();
				//查询天气，返回3天的天气信息
				LWeatherInfo info = WQM.weatherquery(cityId);
				
				uiHandler.post(new ViewUpdater(info));
		    	isUpdating = false;
			}
		}.start();
		
        return true;
	}
	
	private class ViewUpdater implements Runnable
	{
		private LWeatherInfo info;
		public ViewUpdater(LWeatherInfo info)
		{
			this.info = info;
		}
		@Override
		public void run() {
			
			//更新天气UI
			if(null != listener)
			{
				//LWeatherInfo info = new LWeatherInfo(weather.current_condition, weather.weather);
				listener.onGot(info);
			}
		}
		
	}
}
