package com.lookstudio.anywhere.whether;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.chweather.LWeatherUtils;
import com.lookstudio.anywhere.chweather.WeatherForm;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.interfaces.LMediatorable;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.SpeedLevel;
import com.lookstudio.anywhere.view.LWeatherMediator;
import com.lookstudio.anywhere.whether.LocalWeather.CurrentCondition;
import com.lookstudio.anywhere.whether.LocalWeather.Weather;

public class LWeatherInfo implements LMediatorable,Serializable{

	public static final String TEXT_UNKNOWN = "unknown";
	public static final int    NUMBER_UNKNOWN  = -1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 310738497437835511L;
	public boolean isValid;
	public String temp_C;
	public String date;
	public String weatherCode;
	public String weekday;
	public String windDir;
	public double windspeedKmph;
	
	private transient LWeatherMediator mediator;
	public static final LWeatherInfo DEFAULT = new LWeatherInfo(true,"18°","2014/03/06","clear","星期三","东北风",18.9);
	
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd",Locale.CHINA);
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((DATE_FORMAT == null) ? 0 : DATE_FORMAT.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (isValid ? 1231 : 1237);
		result = prime * result + ((temp_C == null) ? 0 : temp_C.hashCode());
		result = prime * result
				+ ((weatherCode == null) ? 0 : weatherCode.hashCode());
		result = prime * result + ((weekday == null) ? 0 : weekday.hashCode());
		result = prime * result + ((windDir == null) ? 0 : windDir.hashCode());
		long temp;
		temp = Double.doubleToLongBits(windspeedKmph);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LWeatherInfo other = (LWeatherInfo) obj;
		if (DATE_FORMAT == null) {
			if (other.DATE_FORMAT != null)
				return false;
		} else if (!DATE_FORMAT.equals(other.DATE_FORMAT))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (isValid != other.isValid)
			return false;
		if (temp_C == null) {
			if (other.temp_C != null)
				return false;
		} else if (!temp_C.equals(other.temp_C))
			return false;
		if (weatherCode == null) {
			if (other.weatherCode != null)
				return false;
		} else if (!weatherCode.equals(other.weatherCode))
			return false;
		if (weekday == null) {
			if (other.weekday != null)
				return false;
		} else if (!weekday.equals(other.weekday))
			return false;
		if (windDir == null) {
			if (other.windDir != null)
				return false;
		} else if (!windDir.equals(other.windDir))
			return false;
		if (Double.doubleToLongBits(windspeedKmph) != Double
				.doubleToLongBits(other.windspeedKmph))
			return false;
		return true;
	}

	public static LWeatherInfo getDefault()
	{
		LWeatherInfo info = new LWeatherInfo();
		
		info.isValid = true;
		info.temp_C  = TEXT_UNKNOWN;
		info.date    = LWeatherUtils.currentDate();
		info.weatherCode = TEXT_UNKNOWN;
		info.weekday  = LWeatherUtils.getWeekday(LApplication.appContext);
		info.windDir  = TEXT_UNKNOWN;
		info.windspeedKmph = NUMBER_UNKNOWN;
		
		return info;
	}
	
	public LWeatherInfo(){}
	public LWeatherInfo(WeatherInfo weatherInfo){
		isValid = true;
		temp_C = weatherInfo.getCurrentTempC() + "°";
		weatherCode = weatherInfo.getCurrentText();
		ParsePosition pp = new ParsePosition(0);
		//String str = "Wed, 19 Mar 2014 9:59 pm CST";
		SimpleDateFormat f = new SimpleDateFormat("E, d MMM yyyy h:m a",Locale.US);
		Date outerDate = f.parse(weatherInfo.getCurrentConditionDate(),pp);
		date = DATE_FORMAT.format(outerDate);
		
		/*SimpleDateFormat dayFormat = new SimpleDateFormat("c",Locale.CHINA);
		weekday    = dayFormat.format(outerDate);*/
		Calendar cal = Calendar.getInstance();
		cal.setTime(outerDate);
		String[] weekdayArray = LApplication.appContext.getResources().getStringArray(R.array.str_array_weekday);
		weekday = weekdayArray[cal.get(Calendar.DAY_OF_WEEK)];
		
		
		windDir = weatherInfo.getWindDirection();
		windspeedKmph = Double.valueOf(weatherInfo.getWindSpeed());
	}
	

	
	public LWeatherInfo(boolean isValid,String temp_C,String date,String weatherCode,String weekday,String windDir,double windspeedKmph)
	{
		this.isValid = isValid;
		this.temp_C  = temp_C;
		this.date    = date;
		this.weatherCode = weatherCode;
		this.weekday = weekday;
		this.windDir = windDir;
		this.windspeedKmph = windspeedKmph;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("isValid : " + isValid);
		builder.append(",");
		builder.append("temp_C : " + temp_C);
		builder.append(",");
		builder.append("date : " + date);
		builder.append(",");
		builder.append("weatherCode : " + weatherCode);
		builder.append(",");
		builder.append("weekday : " + weekday);
		builder.append(",");
		builder.append("windDir : " + windDir);
		builder.append(",");
		builder.append("windspeedKmph : " + windspeedKmph);
		
		return builder.toString();
	}

	@Override
	public LMediator createMediator() {
		if(null == mediator)
		{
			mediator = new LWeatherMediator();
		}
		return mediator;
	}
	
	
}
