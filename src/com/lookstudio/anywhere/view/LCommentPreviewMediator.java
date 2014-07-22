package com.lookstudio.anywhere.view;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.model.LatLng;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.model.LDriveComment;
import com.lookstudio.anywhere.model.LDriveRecord;
import com.lookstudio.anywhere.model.LMapController;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.ToastUtil;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class LCommentPreviewMediator {

	private View container;
	private GridView toolbarGrid;
	private LDriveRecord record;
	private FrameLayout   contentGroup;
	private MenuAdapter menuAdapter;
	private Context context;
	private LDriveComment currentComment;
	private int       currentIndex = 0;
	private boolean    isHidden = false;
	private ViewGroup  activityGroup;
	private LMapController mapController;
	private Handler uiHandler = new Handler();
	private FrameLayout.LayoutParams layoutParams = 
			new FrameLayout.LayoutParams(
			FrameLayout.LayoutParams.MATCH_PARENT, 
			FrameLayout.LayoutParams.MATCH_PARENT);
	
	interface OnItemSelectedListener
	{
		public void onItemSelected(MenuItem item);
	}
	
	interface OnPictureClickListener
	{
		public void onPictureClick(List<String>pictures,String selectedPicture);
	}
	
	public LCommentPreviewMediator(View container,ViewGroup activityGroup,LMapController mapController)
	{
		this.activityGroup = activityGroup;
		this.container = container;
		this.mapController = mapController;
		container.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
			
		});
		context = container.getContext();
		contentGroup = (FrameLayout)container.findViewById(R.id.group_preview_content);
	}
	
	public void update(LDriveRecord record,LDriveComment currentComment)
	{
		this.record = record;
		this.currentComment = currentComment;
		currentIndex = record.comments.indexOf(currentComment);
		isHidden = false;
		initToolbar();
		
		TextView dateView = (TextView)container.findViewById(R.id.view_preview_date);
		dateView.setText(record.date);
	}
	
	public boolean isHidden()
	{
		return isHidden;
	}
	
	public void setHidden(boolean isHidden)
	{
		this.isHidden = isHidden;
		if(isHidden)
		{
			Animation invisibleAnim = AnimationUtils.loadAnimation(context, R.anim.slide_top_to_bottom_self);
			container.setVisibility(View.GONE);
			container.startAnimation(invisibleAnim);
		}
		else
		{
			Animation visibleAnim = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_to_top_self);
			container.setVisibility(View.VISIBLE);
			container.startAnimation(visibleAnim);
			uiHandler.postDelayed(firstContent, 200);
		}
	}
	
	private void refreshItems()
	{
		if(currentComment.hasText())
		{
			onTextItemSelected(null);
		}
		else if(currentComment.hasRadio())
		{
			onRadioItemSelected(null);
		}
		else if(currentComment.hasPhoto())
		{
			onPictureItemSelected(null);
		}
		else
		{
			onTextItemSelected(null);
		}
	}
	
	private Runnable firstContent = new Runnable()
	{

		@Override
		public void run() {
			refreshItems();
				
		}
		
	};
	
	private void updateComment()
	{
		currentComment = record.comments.get(currentIndex);
		if(null != currentComment)
		{
			
			LatLng latLng = new LatLng(currentComment.getLatitude(),currentComment.getLongitude());
			mapController.animateTo(latLng);
			refreshItems();
		}
		
	}
	
	private void onPrevious()
	{
		if(currentIndex <= 0)
		{
			//Nothing in the previous
			ToastUtil.show(context,R.string.str_no_previous_item);
			return;
		}
		
		currentIndex --;
		updateComment();
	}
	
	private void onNext()
	{
		if(currentIndex >= (record.comments.size() - 1))
		{
			//Nothing in the next
			ToastUtil.show(context,R.string.str_no_next_item);
			return;
		}
		
		currentIndex ++;
		updateComment();
	}
	
	
	private void onTextItemSelected(MenuItem item)
	{
		contentGroup.removeAllViews();
		
		if(currentComment.hasText())
		{
			StringBuilder builder = new StringBuilder();
			for(String text : currentComment.getTexts())
			{
				builder.append(text + "\n");
			}
			
			TextView textView = new TextView(context);
			textView.setPadding(10, 10, 10, 10);
			textView.setTextAppearance(context, R.style.TextStyle_Small);
			textView.setTextColor(Color.BLACK);
			textView.setText(builder.toString());
			
			contentGroup.addView(textView,layoutParams);
		}
	}
	
	private void onRadioItemSelected(MenuItem item)
	{
		contentGroup.removeAllViews();
		if(currentComment.hasRadio())
		{
			final ImageView view = new ImageView(context);
			view.setImageResource(R.drawable.selector_play);
			view.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					view.setImageResource(R.drawable.selector_stop);
					view.setEnabled(false);
					LCommonUtil.play(currentComment.getRadio(),new OnCompletionListener()
					{

						@Override
						public void onCompletion(MediaPlayer m) {
							
							view.setImageResource(R.drawable.selector_play);
							view.setEnabled(true);
						}
						
					});
				}
				
			});
			
			FrameLayout.LayoutParams params = 
					new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT, 
					FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			contentGroup.addView(view,layoutParams);
			//contentGroup.addView(new LCircleRadioView(context,currentComment.getRadio()),params);
		}
		
	}
	
	private void onPictureItemSelected(MenuItem item)
	{
		
		contentGroup.removeAllViews();

		if(currentComment.hasPhoto())
		{
			int height = contentGroup.getHeight();
			int width = height;
			
			LScrollView preview = new LScrollView(context);
			preview.setPhoto(currentComment.getPhotos(),width,height,new OnPictureClickListener()
			{

				@Override
				public void onPictureClick(List<String> pictures,
						String selectedPicture) {
					
					showLargePicture(pictures,selectedPicture);
				}
				
			});

			contentGroup.addView(preview, layoutParams);
		}
		
	}
	
	
	private void showLargePicture(List<String> pictures,String selectedPicture)
	{
		LLog.info("click picture:" + selectedPicture);
		LMyPagerView myPagerView = new LMyPagerView(context);
		int width = context.getResources().getDisplayMetrics().widthPixels;
		int height = context.getResources().getDisplayMetrics().heightPixels;
		
		myPagerView.setPhoto(pictures, selectedPicture, width, height);
		
		myPagerView.open(activityGroup);
	}
	
	private void initToolbar()
	{
		List<MenuItem> items = new ArrayList<MenuItem>();
		items.add(new MenuItem(R.drawable.selector_previous_comment,0,new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(MenuItem item) {
				
				onPrevious();
			}
			
		}));
		
		items.add(new MenuItem(R.drawable.selector_text_comment,0,new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(MenuItem item) {
				onTextItemSelected(item);
			}
			
		}));
		
		items.add(new MenuItem(R.drawable.selector_radio_comment,0,new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(MenuItem item) {
				onRadioItemSelected(item);
			}
			
		}));
		
		items.add(new MenuItem(R.drawable.selector_picture_comment,0,new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(MenuItem item) {
				onPictureItemSelected(item);
			}
			
		}));
		
		items.add(new MenuItem(R.drawable.selector_next_comment,0,new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(MenuItem item) {
				
				onNext();
			}
			
		}));
		
		menuAdapter = new MenuAdapter(items,LayoutInflater.from(container.getContext()));
		
		toolbarGrid = (GridView)container.findViewById(R.id.GridView_toolbar);   
	    toolbarGrid.setBackgroundResource(0);// 设置背景   
	    toolbarGrid.setNumColumns(5);// 设置每行列数   
	    toolbarGrid.setGravity(Gravity.CENTER);// 位置居中   
	    toolbarGrid.setVerticalSpacing(10);// 垂直间隔   
	    toolbarGrid.setHorizontalSpacing(10);// 水平间隔   
	    toolbarGrid.setAdapter(menuAdapter);// 设置菜单Adapter   
	    
	    /** 监听底部菜单选项 **/  
	    toolbarGrid.setOnItemClickListener(new OnItemClickListener() {   
	        public void onItemClick(AdapterView<?> parent, View view, int arg2,   
	                long arg3) {   

	        	
	        	MenuItem item = (MenuItem)view.getTag();
	        	
	        	menuAdapter.resetExcept(item);
	        	
        		item.onItemClickListener.onItemSelected(item);
        		item.setSelected(true);
				
	        	
	        }   
	    });  

	}
	
	class MenuAdapter extends BaseAdapter
	{

		private List<MenuItem> items;
		private LayoutInflater inflater;
		
		public MenuAdapter(List<MenuItem> items,LayoutInflater inflater)
		{
			this.items = items;
			this.inflater = inflater;
		}
		
		public void resetExcept(MenuItem item)
		{
			for(MenuItem i : items)
			{
				if(!i.equals(item))
				{
					i.setSelected(false);
				}
			}
			
			notifyDataSetChanged();
		}
		
		public MenuItem getSelectedItem()
		{
			MenuItem item = null;
			for(MenuItem i : items)
			{
				if(i.isSelected)
				{
					item = i;
					break;
				}
			}
			
			return item;
		}
		
		@Override
		public int getCount() {
			
			return items.size();
		}

		@Override
		public Object getItem(int arg0) {
			
			return items.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			
			View group = inflater.inflate(R.layout.lyt_menu_item, null);
			MenuItem menuItem = (MenuItem)getItem(position);
			fillData(group,menuItem);
			group.setTag(menuItem);
			
			return group;
		}
		
	}
	
	private void fillData(View group,MenuItem item)
	{
		ImageView icon = (ImageView)group.findViewById(R.id.view_menu_item_icon);
		TextView  text = (TextView)group.findViewById(R.id.view_menu_item_text);
		
		icon.setImageResource(item.resId);
		if(0 == item.textId)
		{
			text.setText("");
		}
		else
		{
			text.setText(item.textId);
		}
	}
	
	class MenuItem
	{
		int resId;
		int textId;
		OnItemSelectedListener onItemClickListener;
		boolean isSelected = false;
		
		public MenuItem(){}
		public MenuItem(int id,int tId,OnItemSelectedListener onItemClickListener){
			resId = id;
			textId  = tId;
			this.onItemClickListener = onItemClickListener;
		}
		
		public void setSelected(boolean isSelected)
		{
			this.isSelected = isSelected;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + textId;
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
			MenuItem other = (MenuItem) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (textId != other.textId)
				return false;
			return true;
		}
		private LCommentPreviewMediator getOuterType() {
			return LCommentPreviewMediator.this;
		}
		
		
	}
}
