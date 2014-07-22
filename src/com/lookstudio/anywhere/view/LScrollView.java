package com.lookstudio.anywhere.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.view.LCommentPreviewMediator.OnPictureClickListener;
import com.lookstudio.anywhere.view.PhotoPreview.ImageAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.view.ViewGroup.OnHierarchyChangeListener;
public class LScrollView extends FrameLayout implements OnHierarchyChangeListener{

	private LinearLayout container;
	private Context      context;
	
	public LScrollView(Context context)
	{
		this(context,null);
	}
	
	public LScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		View view = LayoutInflater.from(context).inflate(R.layout.lyt_scrollview, null);
		container = (LinearLayout)view.findViewById(R.id.view_my_scroll);
		setOnHierarchyChangeListener(this);
		addView(view);
	}

	@Override
	public void removeDetachedView(View child, boolean animate)
	{
		super.removeAllViews();
		LLog.info("removeDetachedView() child" + child);
	}
	
	@Override
	public void detachViewFromParent(View child)
	{
		super.detachViewFromParent(child);
		LLog.info("detachViewFromParent child = " + child);
	}
	public void setPhoto(final List<String> list,int width,int height,final OnPictureClickListener onPictureClickListener)
	{
		setVisibility(View.VISIBLE);
		
		/*widthPx = getContext().getResources().getDisplayMetrics().widthPixels;
		heightPx = getContext().getResources().getDisplayMetrics().heightPixels;*/
		
		for(String filePath : list)
		{
			Bitmap bm = LCommonUtil.loadBitmap(filePath, width,height);
			BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(),bm);

			final String thePath = filePath;
			ImageView imageView = new ImageView(context);
			imageView.setImageDrawable(drawable);
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
			imageView.setPadding(8,0,8,0);
			imageView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View paramView) {
					
					if(null != onPictureClickListener)
					{
						onPictureClickListener.onPictureClick(list, thePath);
					}
				}
				
			});
			container.addView(imageView, width, height);
		
		}
		
		
		
	}

	@Override
	public void onChildViewAdded(View parent, View child) {
		LLog.info("onChildViewAdded parent = " + parent + " child = " + child);
		
	}

	@Override
	public void onChildViewRemoved(View parent, View child) {
		LLog.info("onChildViewRemoved parent = " + parent + " child = " + child);
		
	}
}
