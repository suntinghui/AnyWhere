package com.lookstudio.anywhere.view;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.lookstudio.anywhere.HomeActivity;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.model.LLoginInfo;
import com.lookstudio.anywhere.model.LSaver;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LSetting;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 宸︿晶鍒掑嚭鑿滃崟
 * 
 * @author: zzf
 */
public class LViewSlidingMenu extends LinearLayout {

	private final int mShowDuration = 200;
	private final int mHideDuration = 600;
	private ImageView userIconView;
	private TextView usernameView;
	//private MenuAdapter mMenuAdapter;
	private boolean mIsShow;

	private final long mHideDelay = 10000;
	private final Handler mHideHandler = new Handler();
	private Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			hide();

		}
	};
	
	private ViewGroup mContentLayout;
	//private ListView mMenuListView;
	
	public static class MenuItem
	{
		public int resId;
		public String title;
		public OnClickListener listener;
		
		public MenuItem(){}
		public MenuItem(int resId,String title,OnClickListener listener)
		{
			this.resId = resId;
			this.title = title;
			this.listener = listener;
		}
	}
	
	public LViewSlidingMenu(Context context,AttributeSet attrset)
	{
		super(context,attrset);
	}
	
	public LViewSlidingMenu(Context context)
	{
		this(context,null);
	}

	public void setContentLayout(ViewGroup contentLayout)
	{
		mContentLayout = contentLayout;
	}
	
	public void setItems(List<MenuItem> items)
	{
		LSaver saver = new LSaver(getContext());
		View menuLayout = ((LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.lyt_home_menu, null);
		
		usernameView = (TextView)menuLayout.findViewById(R.id.view_user_name);
		userIconView = (ImageView)menuLayout.findViewById(R.id.view_user_icon);
		
		int width = LSetting.instance().getUserIconWidth(0); 
		int height = LSetting.instance().getUserIconHeight(0);
		if((null != saver.getUserIcon()) && (width > 0) && (height > 0))
		{
			Bitmap userIconBm = LCommonUtil.scaleBitmap(saver.getUserIcon(),width,height);
			userIconView.setImageDrawable(new BitmapDrawable(getContext().getResources(),userIconBm));
		}
		userIconView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				getContext().sendBroadcast(new Intent(HomeActivity.ACTION_MODIFY_USER_ICON));
				
			}
			
		});
		
		
		LLoginInfo old = saver.readLoginInfo();
		if(null != old)
		{
			usernameView.setText("hi," + old.getUsername());
		}
		
		makeup(items.get(0),menuLayout,R.id.lyt_advice,R.id.img_advice,R.id.title_advice);
		makeup(items.get(1),menuLayout,R.id.lyt_mark,R.id.img_mark,R.id.title_mark);
		makeup(items.get(2),menuLayout,R.id.lyt_aboutus,R.id.img_aboutus,R.id.title_aboutus);
		makeup(items.get(3),menuLayout,R.id.lyt_logout,R.id.img_logout,R.id.title_logout);
		/*mMenuListView = (ListView) menuLayout
				.findViewById(R.id.menu_listView);

		mMenuAdapter = new MenuAdapter(items);
		mMenuListView.setAdapter(mMenuAdapter);
		mMenuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				
				MenuItem item = (MenuItem)view.getTag();
				if(null != item.listener)
				{
					item.listener.onClick(view);
				}
			}
		});*/
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		addView(menuLayout,params);
		
		requestLayout();
	}
	
	private void makeup(final MenuItem item,View layout,int lytId,int iconId,int titleId)
	{
		LinearLayout itemLayout = (LinearLayout)layout.findViewById(lytId);
		
		itemLayout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				item.listener.onClick(v);
				
			}
			
		});
		ImageView imgView = (ImageView)itemLayout.findViewById(iconId);
		imgView.setImageResource(item.resId);
		
		TextView  titleView = (TextView)itemLayout.findViewById(titleId);
		titleView.setText(item.title);
	}
	
	public boolean isShow() {
		return mIsShow;
	}

	public void show() {

		LLog.info("screen,width = " + getContext().getResources().getDisplayMetrics().widthPixels 
				+ " height = " + getContext().getResources().getDisplayMetrics().heightPixels);
		LLog.info("sliding menu,width = " + userIconView.getWidth() + " height = " + userIconView.getHeight());
		LLog.info("sliding menu,right = " + userIconView.getRight() + " bottom = " + userIconView.getBottom());
		
		View itemLayout = findViewById(R.id.lyt_advice);
		LLog.info("sliding menu itemLayout,width = " + itemLayout.getWidth() + " height = " + itemLayout.getHeight());
		LLog.info("sliding menu itemLayout,right = " + itemLayout.getRight() + " bottom = " + itemLayout.getBottom());
		//mMenuAdapter.notifyDataSetChanged();
		slideView(itemLayout.getRight() + 10,40,(float)0.8,(float)0.8,mShowDuration);
		mIsShow = true;
		mHideHandler.removeCallbacks(mHideRunnable);
		//mHideHandler.postDelayed(mHideRunnable, mHideDelay);
	}


	public void hide() {
		slideView(0,0,(float)1.0,(float)1.0, mHideDuration);
		mIsShow = false;
		mHideHandler.removeCallbacks(mHideRunnable);
	}

	public void hideDelay(int delayMillis)
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(new Runnable()
		{

			@Override
			public void run() {
				hide();
			}
			
		}, delayMillis);
	}
	private void slideView(float xValue,float yValue,float scaleXValue,float scaleYValue, long duration) {
		ObjectAnimator moverX = ObjectAnimator.ofFloat(mContentLayout,"x", xValue);
		ObjectAnimator moverY = ObjectAnimator.ofFloat(mContentLayout, "y", yValue);
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(mContentLayout, "scaleX", scaleXValue);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(mContentLayout, "scaleY", scaleYValue);
		
		moverX.setDuration(duration);
		moverY.setDuration(duration);
		scaleX.setDuration(duration);
		scaleY.setDuration(duration);
		
		//play them together
		AnimatorSet set = new AnimatorSet();
		
		set.play(moverX).with(moverY);
		set.play(moverX).with(scaleX);
		set.play(moverX).with(scaleY);
		
		set.start();
	}


	private class MenuAdapter extends BaseAdapter {

		private List<MenuItem> mMenuItems;
		
		public MenuAdapter(List<MenuItem> items)
		{
			mMenuItems = Collections.unmodifiableList(items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater =LayoutInflater.from(getContext());
			View view = inflater.inflate(
					R.layout.lyt_item_home_menu, null, true);
			MenuItem item = (MenuItem)getItem(position);
			fillData(item,view);
			view.setTag(item);
			
			return view;
		}

		@Override
		public int getCount() {
			
			return mMenuItems.size();
		}

		@Override
		public Object getItem(int pos) {
			
			return mMenuItems.get(pos);
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		private void fillData(MenuItem item,View group)
		{
			TextView txtTitle = (TextView) group
					.findViewById(R.id.sliding_name);

			ImageView imageView = (ImageView) group
					.findViewById(R.id.sliding_image);
			txtTitle.setText(item.title);
			imageView.setBackgroundResource(item.resId);
			
			txtTitle.setFocusable(false);
			imageView.setFocusable(false);
		}
	}

	public void modifyUserIcon(Bitmap src)
	{
		int width = userIconView.getWidth();
		int height = userIconView.getHeight();
		LSetting.instance().setUserIconDimension(width, height);
		new LSaver(getContext()).saveUserIcon(src);
		userIconView.setImageDrawable(new BitmapDrawable(getContext().getResources(),LCommonUtil.scaleBitmap(src, width, height)));
	}
}
