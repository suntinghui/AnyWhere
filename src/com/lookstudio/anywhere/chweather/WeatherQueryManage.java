package com.lookstudio.anywhere.chweather;

import com.lookstudio.anywhere.whether.LWeatherInfo;



public interface WeatherQueryManage {
	/**
	 * 查询天气方法
	 * @param CityId 对应城市的id
	 * @return 暂时返回3天的天气数据
	 */
	public LWeatherInfo weatherquery(String CityId);

}
