package com.lookstudio.anywhere.view;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.model.LTravelProxy;
import com.lookstudio.anywhere.model.LTravelProxy.OnTravelStateChangedListener;
import com.lookstudio.anywhere.util.LScreenUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class LBackFromMapMediator {
 
	private View mBackButton;
	private Activity screen;
	private LTravelProxy traveProxy;
	private boolean isScanMode = false;
	private ViewGroup container;
	
	public LBackFromMapMediator(ViewGroup container,Activity screen,LTravelProxy traveProxy)
	{
		this.container = container;
		this.traveProxy = traveProxy;
		mBackButton = container.findViewById(R.id.view_back_from_map);
		mBackButton.setVisibility(View.VISIBLE);
		this.screen = screen;
		registerListeners();
	}
	
	private void registerListeners()
	{
		/*traveProxy.setOnTravelStateChangedListener(new OnTravelStateChangedListener()
		{

			@Override
			public void onStateChanged(boolean isStarted) {
				if(isStarted)
				{
					mBackButton.setVisibility(View.INVISIBLE);
				}
				else
				{
					mBackButton.setVisibility(View.VISIBLE);
				}
				
			}
			
		});*/
		mBackButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				onBack();
			}
			
		});
	}


	public void onBack()
	{
		if(true == isScanMode)
		{
			LScreenUtil.backward(screen);
		}
		else
		{
			if(traveProxy.isStarted())
			{
				showConfirmDialog();
			}
			else
			{
				LScreenUtil.backward(screen);
			}
		}
	}
	
	public void showConfirmDialog() {
		final View scanLayout = (RelativeLayout) LayoutInflater.from(screen).inflate(
				R.layout.map_error_dialog, null);
		View confirmBtn = scanLayout.findViewById(R.id.map_error_confirm);
		View cancelBtn  = scanLayout.findViewById(R.id.map_error_cancel);
		confirmBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view) {
				container.removeView(scanLayout);
				LScreenUtil.backward(screen);
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view) {
				container.removeView(scanLayout);
				
			}
			
		});
		scanLayout.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
				return true;
			}
			
		});

		container.addView(scanLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
/*	private void showDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(screen);
		builder.setTitle(R.string.str_title_driving_reminder);
		builder.setMessage(R.string.str_msg_driving_reminder);
		builder.setPositiveButton(R.string.str_button_confirm, new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which) {
				LScreenUtil.backward(screen);
			}
			
		});
		
		builder.setNegativeButton(R.string.str_button_cancel, null);
		builder.show();
	}*/
	public void setScanMode(boolean isScanMode)
	{
		this.isScanMode = isScanMode;
	}
}
