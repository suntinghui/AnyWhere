package com.lookstudio.anywhere.view;


import com.amap.api.location.AMapLocation;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.model.LLocationManager;
import com.lookstudio.anywhere.model.LLocationManager.LLocationChangedListener;
import com.lookstudio.anywhere.util.LLog;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class LMyLocationMediator implements LLocationChangedListener{

	private TextView myLocationView;
	
	public LMyLocationMediator(View container)
	{
		myLocationView = (TextView)container.findViewById(R.id.view_my_location);
		
		AMapLocation loc = LLocationManager.getManager().getLastKnownLocation();
		if(null != loc)
		{
			setLocation(loc);
		}
		
		LLocationManager.getManager().setLocationListener(this);
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		
		setLocation(location);
	}
	
	 
	private void setLocation(AMapLocation loc)
	{
		//LLog.info("setLocation,city = " + loc);
		//LLog.info("provice:" + loc.getProvince() + " city = " + loc.getCity());
		String locText = loc.getCity();
		if(false == TextUtils.isEmpty(locText))
		{
			myLocationView.setText(locText);
		}
		
	}
	
	public String getCity()
	{
		return myLocationView.getText().toString();
	}
}
