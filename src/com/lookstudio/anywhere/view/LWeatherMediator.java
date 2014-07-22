package com.lookstudio.anywhere.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lookstudio.anywhere.MainActivity;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.chweather.LWeatherUtils;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.util.LConstant;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LScreenUtil;
import com.lookstudio.anywhere.whether.LWeatherInfo;
import com.lookstudio.anywhere.whether.LocalWeather;
import com.lookstudio.anywhere.whether.LocationSearch;

import com.lookstudio.anywhere.view.PullToRefreshListView;

/**
 * recognize data ,inflate view and fill view with data,
 * @author look
 *
 */
public class LWeatherMediator implements LMediator{

	private TextView mTempView;
	private TextView mDateView;
	private ImageView mIconView;
	private TextView mWeekdayView;
	private ImageView mWindmillView;
	private TextView  mWindDirView;
	private TextView  mWindSpeedView;

	private View      mGoDriveView;
	private Map<String,DayAndNight> weatherTypeRes = new HashMap<String,DayAndNight>();
	
	private Resources resources;
	private LayoutInflater inflater;
	private final boolean USE_DEFAULT_WEATHER = false;
	private static List<Drawable> drawables;
	private View container;
	public LWeatherMediator()
	{
		weatherTypeRes.put("Sunny", new DayAndNight(R.drawable.img_weather_1,R.drawable.img_weather_2));
		weatherTypeRes.put("Fair", new DayAndNight(R.drawable.img_weather_1,R.drawable.img_weather_2));
		weatherTypeRes.put("Clear", new DayAndNight(R.drawable.img_weather_3,R.drawable.img_weather_4));
		weatherTypeRes.put("Cloudy", new DayAndNight(R.drawable.img_weather_5,R.drawable.img_weather_6));
		weatherTypeRes.put("Shower", new DayAndNight(R.drawable.img_weather_8,R.drawable.img_weather_9));
		weatherTypeRes.put("Rain", new DayAndNight(R.drawable.img_weather_7,R.drawable.img_weather_7));
		weatherTypeRes.put("Lightning", new DayAndNight(R.drawable.img_weather_10,R.drawable.img_weather_10));
		weatherTypeRes.put("Snow", new DayAndNight(R.drawable.img_weather_11,R.drawable.img_weather_11));
		weatherTypeRes.put("Windy", new DayAndNight(R.drawable.img_weather_12,R.drawable.img_weather_12));
	}
	
	public boolean canHandle(Object data)
	{
		return data instanceof LWeatherInfo;
	}
	
	@Override
	public View getView(Context context)
	{
		if(null != container)
		{
			return container;
		}
		resources = context.getResources();
		inflater = LayoutInflater.from(context);
		
		container = inflater.inflate(R.layout.lyt_weather_panel,null);
		mTempView = (TextView)container.findViewById(R.id.view_temp);
		mDateView = (TextView)container.findViewById(R.id.view_date);
		mIconView = (ImageView)container.findViewById(R.id.view_weather_icon);
		mWeekdayView = (TextView)container.findViewById(R.id.view_weekday);
		mWindmillView = (ImageView)container.findViewById(R.id.view_windmill);
		mWindDirView  = (TextView)container.findViewById(R.id.view_wind_dir);
		mWindSpeedView = (TextView)container.findViewById(R.id.view_wind_speed);
		
		mGoDriveView = container.findViewById(R.id.view_go_drive);
		
		registerListeners(context);
		
		return container;
	}
	
	private void registerListeners(final Context context)
	{
		mGoDriveView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				
				Bundle bundle = new Bundle();
				bundle.putInt(LConstant.EXTRA_MAP_SCOPE,LConstant.MAP_SCOPE_GO_DRIVE);
				LScreenUtil.forward(context, MainActivity.class,bundle);
				
			}
			
		});
	}

	private void initDrawables()
	{
		int []resIds = {R.drawable.fc0001,R.drawable.fc0002,R.drawable.fc0003,R.drawable.fc0004,R.drawable.fc0005,R.drawable.fc0006,R.drawable.fc0007,
				R.drawable.fc0008,R.drawable.fc0009,R.drawable.fc0010,R.drawable.fc0011,R.drawable.fc0012,R.drawable.fc0013,R.drawable.fc0014,
				R.drawable.fc0015,R.drawable.fc0016,R.drawable.fc0017,R.drawable.fc0018,R.drawable.fc0019,R.drawable.fc0020,R.drawable.fc0021,
				R.drawable.fc0022,R.drawable.fc0023,R.drawable.fc0024,R.drawable.fc0025,R.drawable.fc0026,R.drawable.fc0027,R.drawable.fc0028,
				R.drawable.fc0029,R.drawable.fc0030,R.drawable.fc0031,R.drawable.fc0032,R.drawable.fc0033,R.drawable.fc0034,R.drawable.fc0035,
				R.drawable.fc0036,R.drawable.fc0037,R.drawable.fc0038,R.drawable.fc0039,R.drawable.fc0040,R.drawable.fc0041,R.drawable.fc0042,
				R.drawable.fc0043,R.drawable.fc0044,R.drawable.fc0045,R.drawable.fc0046,R.drawable.fc0047,R.drawable.fc0048,R.drawable.fc0049,
				R.drawable.fc0050,R.drawable.fc0051,R.drawable.fc0052,R.drawable.fc0053,R.drawable.fc0054,R.drawable.fc0055,R.drawable.fc0056,
				R.drawable.fc0057,R.drawable.fc0058,R.drawable.fc0059,R.drawable.fc0060,R.drawable.fc0061,R.drawable.fc0062,R.drawable.fc0063,
				R.drawable.fc0064,R.drawable.fc0065,R.drawable.fc0066,R.drawable.fc0067,R.drawable.fc0068,R.drawable.fc0069,R.drawable.fc0070};
		
		drawables = new ArrayList<Drawable>(resIds.length);
		for(int index = 0;index < resIds.length;index++)
		{
			drawables.add(resources.getDrawable(resIds[index]));	
		}
		
	}
	
	private AnimationDrawable getWindmill(double windspeed)
	{
		
		AnimationDrawable drawable = new AnimationDrawable();
		drawable.setOneShot(false);
		
		int duration = 200;
		
		int []speedDesc = {100,200,300,400,500,600,700,800,900,1000,1100};
		int    []durations  = {80  ,70,  60, 50,  40,  30, 25, 20,  15,  10,  5};
		for(int index = 0;index < speedDesc.length;index++)
		{
			if(windspeed <speedDesc[index])
			{
				duration = durations[index];
				break;
			}
		}
		
		if(null == drawables)
		{
			initDrawables();
		}
		
		if(windspeed < 1)
		{
			duration = 10000;
		}
		
		LLog.info("windspeed: " + windspeed + " duration:" + duration);
		for(Drawable d : drawables)
		{
			drawable.addFrame(d, duration);
		}
		
		return drawable;
	}

	@Override
	public void fillViewWithData(Object obj,Object extra,BaseAdapter adapter) {

		if(!(obj instanceof LWeatherInfo))
		{
			return;
		}
		LWeatherInfo info = (LWeatherInfo)obj;
		
		if(USE_DEFAULT_WEATHER)
		{
			info = LWeatherInfo.DEFAULT;
		}
		else
		{
			if(false == info.isValid)
			{
				return;
			}
		}
		
		mTempView.setText(info.temp_C);
		mDateView.setText(info.date);
		mIconView.setImageResource(getWeatherTypeResId(info.weatherCode));
		mWeekdayView.setText(info.weekday);
		if(LWeatherInfo.TEXT_UNKNOWN.equals(info.windDir))
		{
			mWindDirView.setText(LWeatherInfo.TEXT_UNKNOWN);
		}
		else
		{
			mWindDirView.setText(LWeatherUtils.getDirection(Integer.valueOf(info.windDir)) + "风");
		}
		
		AnimationDrawable animDrawable = null;
		if(LWeatherInfo.NUMBER_UNKNOWN == info.windspeedKmph)
		{
			mWindSpeedView.setText(LWeatherInfo.TEXT_UNKNOWN);
			animDrawable = getWindmill(0);
		}
		else
		{
			int value = (int)(info.windspeedKmph*100/100);
			if(0 == value)
			{
				mWindSpeedView.setText(info.windspeedKmph + "km/h");
			}
			else
			{
				mWindSpeedView.setText(value + "km/h");
			}
			
			animDrawable = getWindmill(info.windspeedKmph);
		}
		
		mWindmillView.setBackgroundDrawable(animDrawable);
		animDrawable.start();
	}
	
	
	public int getWeatherTypeResId(String type)
	{
		LLog.info("weather type is " + type);
		int resId = 0;
		DayAndNight dn = null;
		for(String key : weatherTypeRes.keySet())
		{
			if(type.contains(key) || (key.contains(type)))
			{
				dn = weatherTypeRes.get(key);
				break;
			}
		}
		
		if(null == dn)
		{
			dn = weatherTypeRes.get("Clear");
		}
		
		if(isDayNow())
		{
			return dn.day;
		}
		else
		{
			return dn.night;
		}
	}
	
	public boolean isDayNow()
	{
		boolean isDayNow = false;
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if((hour >= 6) && (hour <= 18))
		{
			isDayNow = true;
		}
		
		return isDayNow;
	}
	
	public class DayAndNight
	{
		int day;
		int night;
		
		public DayAndNight(){}
		public DayAndNight(int day,int night)
		{
			this.day = day;
			this.night = night;
		}
	}
}
