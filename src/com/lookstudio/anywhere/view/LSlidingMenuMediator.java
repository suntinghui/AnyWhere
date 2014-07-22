package com.lookstudio.anywhere.view;

import java.util.ArrayList;
import java.util.List;

import com.lookstudio.anywhere.AboutUsActivity;
import com.lookstudio.anywhere.HomeActivity;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.model.LLoginProxy;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LScreenUtil;
import com.lookstudio.anywhere.view.LViewSlidingMenu.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class LSlidingMenuMediator{

	private LViewSlidingMenu mSlidingMenu;
	private View             mMenuSwitchBtn;
	private LInterceptTouchEventLayout        mContentView;
	private Context  context;
	
	public void setBackgroundResource(int resId)
	{
		mContentView.setBackgroundResource(resId);
	}
	
	public LSlidingMenuMediator(View container)
	{
		context = container.getContext();
		
		List<MenuItem> items = new ArrayList<MenuItem>(4);
	
		
		items.add(new MenuItem(R.drawable.img_menu_advice,context.getString(R.string.str_title_advice),new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				
				LCommonUtil.sendMailByIntent(context);
			}
			
		}));
		

		items.add(new MenuItem(R.drawable.img_menu_mark,context.getString(R.string.str_title_remark),new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				

			}
			
		}));
		
		items.add(new MenuItem(R.drawable.img_menu_aboutus,context.getString(R.string.str_title_about_us),new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				
				LScreenUtil.forward(context, AboutUsActivity.class);
			}
			
		}));
		items.add(new MenuItem(R.drawable.img_menu_location,context.getString(R.string.str_title_logout),new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				LLoginProxy proxy = new LLoginProxy();
				proxy.logout(context);
				context.sendBroadcast(new Intent(HomeActivity.ACTION_LOGOUT));
			}
			
		}));
		
		mContentView = (LInterceptTouchEventLayout)container.findViewById(R.id.view_content);
		mSlidingMenu = (LViewSlidingMenu)container.findViewById(R.id.view_sliding_menu);
		
		mSlidingMenu.setContentLayout(mContentView);
		mSlidingMenu.setItems(items);
		
		mContentView.setSlidingMenu(mSlidingMenu);
		mMenuSwitchBtn   = container.findViewById(R.id.view_menu_switch);
		
		registerListeners();
	}
	
	private void registerListeners()
	{
		mMenuSwitchBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {

				onSwitchMenu();
				
			}
			
		});
		
	}
	
	private void onSwitchMenu()
	{
		if(mSlidingMenu.isShow())
		{
			mSlidingMenu.hide();
		}
		else
		{
			mSlidingMenu.show();
		}
	}

	public void modifyUserIcon(Bitmap bm)
	{
		mSlidingMenu.modifyUserIcon(bm);
	}
}
