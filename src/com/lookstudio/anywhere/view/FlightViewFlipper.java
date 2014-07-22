package com.lookstudio.anywhere.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class FlightViewFlipper extends ViewFlipper {

	public FlightViewFlipper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public FlightViewFlipper(Context context,AttributeSet attrSet)
	{
		super(context,attrSet);
	}

	@Override
	protected void onDetachedFromWindow()
	{
		try
		{
			super.onDetachedFromWindow();
		}
		catch(IllegalArgumentException e)
		{
			stopFlipping();
		}
	}
}
