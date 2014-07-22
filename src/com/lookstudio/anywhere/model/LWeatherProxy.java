package com.lookstudio.anywhere.model;

import com.amap.api.location.AMapLocation;
import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.model.LLocationManager.LLocationChangedListener;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.whether.LWeatherUpdater;

/**
 * 城市变化时更新天气
 * @author look
 *
 */
public class LWeatherProxy implements LLocationChangedListener{

	private static String lastCity = "";
	private LWeatherUpdater weatherUpdater;
	
	public LWeatherProxy(LWeatherUpdater weatherUpdater)
	{
		this.weatherUpdater = weatherUpdater;
		LLocationManager.getManager().setLocationListener(this);
	}
	
	public boolean  requestUpdate()
	{
		AMapLocation loc = LLocationManager.getManager().getLastKnownLocation();
		if(isNewCity((getCity(loc))))
		{
			weatherUpdater.update(getCity(loc));
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onLocationChanged(AMapLocation location) {

		String city = getCity(location);
		if(isNewCity(city))
		{
			ToastUtil.show(LApplication.appContext,"正在查询" + city + "的天气信息");
			weatherUpdater.update(city);
			lastCity = city;
		}	
	}
	
	private boolean isNewCity(String city)
	{
		return (!"".equals(city)) && (false == lastCity.equals(city));
	}
	
	private String getCity(AMapLocation location)
	{
		if((null != location) && (null != location.getCity()))
		{
			String cityName = location.getCity();
			if(0 >= cityName.length())
			{
				return "";
			}
			String name = cityName.substring(0, cityName.length()-1);
			
			return name;
		}
		
		return "";
	}
}
