package com.lookstudio.anywhere.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.model.LMapController;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LSetting;

public class LControlMapTypeMediator implements OnClickListener{

	private View mShowView;
	private View mHideView;
	private ImageView mNormalView;
	private ImageView mSignalView;
	//private ImageView m3DView;
	private CheckBox  mTrafficView;
	private LMapController mController;
	private View      mSetPanel;
	private View      mGroup;
	private boolean   isShowed = false;
	
	public LControlMapTypeMediator(View group,LMapController controller)
	{
		mController = controller;
		mGroup      = group;
		
		mGroup.setBackgroundResource(0);
		mGroup.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return isShowed;
			}
			
		});
		mShowView = group.findViewById(R.id.view_show_map_type);
		mHideView = group.findViewById(R.id.view_hide_map_type);
		mHideView.setVisibility(View.GONE);
		mNormalView = (ImageView)group.findViewById(R.id.view_normal_map_type);
		mSignalView = (ImageView)group.findViewById(R.id.view_signal_map_type);
		//m3DView = (ImageView)group.findViewById(R.id.view_3d_map_type);
		mTrafficView = (CheckBox)group.findViewById(R.id.view_traffic_info);
		mSetPanel = group.findViewById(R.id.group_set_map_type);
		mSetPanel.setVisibility(View.GONE);
		mGroup = group;
		
		init();
	}
	
	private void init()
	{
		mShowView.setVisibility(View.VISIBLE);
		mHideView.setVisibility(View.GONE);
		
		mTrafficView.setChecked(mController.get().isTrafficEnabled());
		mTrafficView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				LLog.info("traffic, onCheckedChanged isChecked:" + isChecked);
				LSetting.instance().setTrafficEnabled(isChecked);
				mController.get().setTrafficEnabled(isChecked);
			}
		});
		
		int mapType = mController.get().getMapType();
		if(AMap.MAP_TYPE_NORMAL == mapType)
		{
			onClick(mNormalView);
		}
		else if(AMap.MAP_TYPE_SATELLITE == mapType)
		{
			onClick(mSignalView);
		}
		
		mShowView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				
				switchChanged(v);
			}
			
		});
		mHideView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				
				switchChanged(v);
			}
			
		});
		mNormalView.setOnClickListener(this);
		mSignalView.setOnClickListener(this);
		
	} 
	
	
	private void switchChanged(View view)
	{
		
		if(view == mShowView)
		{
			isShowed = true;
			mShowView.setVisibility(View.GONE);
			mSetPanel.setVisibility(View.VISIBLE);
			Animation openAnim = AnimationUtils.loadAnimation(mGroup.getContext(), R.anim.anim_scale_open);
			mSetPanel.startAnimation(openAnim);
			
			mHideView.setVisibility(View.VISIBLE);
			mGroup.setBackgroundResource(R.drawable.bg_translucent);
		}
		else
		{
			isShowed = false;
			mShowView.setVisibility(View.VISIBLE);
			mSetPanel.setVisibility(View.GONE);
			Animation closeAnim = AnimationUtils.loadAnimation(mGroup.getContext(), R.anim.anim_scale_close);
			mSetPanel.startAnimation(closeAnim);
			mHideView.setVisibility(View.GONE);
			mGroup.setBackgroundResource(0);
		}
	}

	
	@Override
	public void onClick(View view) {
		
		mNormalView.setBackgroundResource(R.drawable.img_normal_map);
		mSignalView.setBackgroundResource(R.drawable.img_satellite_map);
		
		int mapType = 0;
		if(mNormalView == view)
		{
			mNormalView.setBackgroundResource(R.drawable.lytlist_normal_map_selected);
			mapType = AMap.MAP_TYPE_NORMAL;
		}
		else if(mSignalView == view)
		{
			mSignalView.setBackgroundResource(R.drawable.lytlist_satellite_map_selected);
			mapType = AMap.MAP_TYPE_SATELLITE;
		}
		/*else if(m3DView == view)
		{
			
		}*/
		
		mController.get().setMapType(mapType);
		LSetting.instance().setMapType(mapType);
	}
	
	public void hide()
	{
		mGroup.setVisibility(View.GONE);
	}
}
