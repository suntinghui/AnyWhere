package com.lookstudio.anywhere.whether;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lookstudio.anywhere.util.LLog;



public class LocalWeather extends WwoApi {
	public static final String FREE_API_ENDPOINT = "http://api.worldweatheronline.com/free/v1/weather.ashx";
	public static final String PREMIUM_API_ENDPOINT = "http://api.worldweatheronline.com/premium/v1/premium-weather-V2.ashx";

	LocalWeather(boolean freeAPI) {
		super(freeAPI);
		if(freeAPI) {
			apiEndPoint = FREE_API_ENDPOINT;
		} else {
			apiEndPoint = PREMIUM_API_ENDPOINT;
		}
	}
	
	Data callAPI(String query) {
		return getLocalWeatherData(getInputStream(apiEndPoint + query));
	}
	
	Data getLocalWeatherData(InputStream is) {
		Data weather = null;
		
		try {
			 LLog.info("getLocalWeatherData");
			
	        XmlPullParser xpp = getXmlPullParser(is);
	        
	        weather = new Data();
	        CurrentCondition cc = new CurrentCondition();
	        weather.current_condition = cc;
	
	        cc.temp_C = getTextForTag(xpp, "temp_C");
	        cc.weatherIconUrl = getDecode(getTextForTag(xpp, "weatherIconUrl"));
	        cc.weatherDesc = getDecode(getTextForTag(xpp, "weatherDesc"));
	        
	        LLog.info("getLocalWeatherData:"+cc.temp_C);
	        LLog.info("getLocalWeatherData:"+cc.weatherIconUrl);
	        LLog.info("getLocalWeatherData:"+cc.weatherDesc);
		} catch (Exception e) {
			
		}
	
		return weather;
	}
	
	class Params extends RootParams {
		String q;					//required
		String extra;
		String num_of_days="1";		//required
		String date;
		String fx="no";
		String cc;					//default "yes"
		String includeLocation;		//default "no"
		String format;				//default "xml"
		String show_comments="no";
		String callback;
		String key;					//required
		
		Params(String key) {
			num_of_days = "1";
			fx = "no";
			show_comments = "no";
			this.key = key;
		}
		
		Params setQ(String q) {
			this.q = q;
			return this;
		}
		
		Params setExtra(String extra) {
			this.extra = extra;
			return this;
		}
		
		Params setNumOfDays(String num_of_days) {
			this.num_of_days = num_of_days;
			return this;
		}
		
		Params setDate(String date) {
			this.date = date;
			return this;
		}
		
		Params setFx(String fx) {
			this.fx = fx;
			return this;
		}
		
		Params setCc(String cc) {
			this.cc = cc;
			return this;
		}
		
		Params setIncludeLocation(String includeLocation) {
			this.includeLocation = includeLocation;
			return this;
		}
		
		Params setFormat(String format) {
			this.format = format;
			return this;
		}
		
		Params setShowComments(String showComments) {
			this.show_comments = showComments;
			return this;
		}
		
		Params setCallback(String callback) {
			this.callback = callback;
			return this;
		}
		
		Params setKey(String key) {
			this.key = key;
			return this;
		}
	}
	
	public class Data {
		Request request;
		public CurrentCondition current_condition;
		public Weather weather;
	}
	
	class Request {
		String type;
		String query;
	}

	public class CurrentCondition {
	    public String observation_time;
	    public String temp_C;
	    public String weatherCode;
	    public String weatherIconUrl;
	    public String weatherDesc;
	    public String windspeedMiles;
	    public String windspeedKmph;
	    public String winddirDegree;
	    public String winddir16Point;
	    public String precipMM;
	    public String humidity;
	    public String visibility;
	    public String pressure;
	    public String cloudcover;
	    
	    @Override
	    public String toString()
	    {
	    	StringBuilder builder = new StringBuilder();
	    	
	    	builder.append("observation_time = " + observation_time);
	    	builder.append(",");
	    	builder.append("temp_C=" + temp_C);
	    	builder.append(",");
	    	builder.append("weatherCode=" + weatherCode);
	    	builder.append(",");
	    	builder.append("weatherIconUrl="+weatherIconUrl);
	    	builder.append(",");
	    	builder.append("weatherDesc=" + weatherDesc);
	    	builder.append(",");
	    	builder.append("windspeedMiles=" + windspeedMiles);
	    	builder.append(",");
	    	builder.append("windspeedKmph="+windspeedKmph);
	    	builder.append(",");
	    	builder.append("winddirDegree="+winddirDegree);
	    	builder.append(",");
	    	builder.append("winddir16Point="+winddir16Point);
	    	builder.append(",");
	    	builder.append("precipMM="+precipMM);
	    	builder.append(",");
	    	builder.append("humidity="+humidity);
	    	builder.append(",");
	    	builder.append("visibility="+visibility);
	    	builder.append(",");
	    	builder.append("pressure="+pressure);
	    	builder.append(",");
	    	builder.append("cloudcover=" +cloudcover);

	    	return builder.toString();
	    }
	}

	public class Weather {
		public String date; 
		public String tempMaxC;
		public String tempMaxF;
		public String tempMinC;
		public String tempMinF;
		public String windspeedMiles;
		public String windspeedKmph;
		public String winddirection;
		public String weatherCode;
		public String weatherIconUrl;
		public String weatherDesc;
		public String precipMM;
	    
	    @Override
	    public String toString()
	    {
	    	StringBuilder builder = new StringBuilder();
	    	
	    	builder.append("date="+date);
	    	builder.append(",");
	    	builder.append("tempMaxC="+tempMaxC);
	    	builder.append(",");
	    	builder.append("tempMaxF="+tempMaxF);
	    	builder.append(",");
	    	builder.append("tempMinC="+tempMinC);
	    	builder.append(",");
	    	builder.append("tempMinF="+tempMinF);
	    	builder.append(",");
	    	builder.append("windspeedMiles="+windspeedMiles);
	    	builder.append(",");
	    	builder.append("windspeedKmph="+windspeedKmph);
	    	builder.append(",");
	    	builder.append("winddirection="+winddirection);
	    	builder.append(",");
	    	builder.append("weatherCode="+weatherCode);
	    	builder.append(",");
	    	builder.append("weatherIconUrl="+weatherIconUrl);
	    	builder.append(",");
	    	builder.append("weatherDesc="+weatherDesc);
	    	builder.append(",");
	    	builder.append("precipMM="+precipMM);

	    	return builder.toString();
	    }
	}
}

