package com.lookstudio.anywhere.chweather;

import com.lookstudio.anywhere.whether.LWeatherInfo;



public interface WeatherQueryManage {
	/**
	 * ��ѯ��������
	 * @param CityId ��Ӧ���е�id
	 * @return ��ʱ����3�����������
	 */
	public LWeatherInfo weatherquery(String CityId);

}
