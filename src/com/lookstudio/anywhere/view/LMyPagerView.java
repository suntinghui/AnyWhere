package com.lookstudio.anywhere.view;

import java.util.ArrayList;
import java.util.List;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.view.PhotoPreview.ImageAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class LMyPagerView extends FrameLayout{

	private TextView indicatorView;
	private View     closeView;
	private PagedView pageView;
	private ViewGroup parentGroup;
	private int       TOTAL_COUNT;
	
	private List<BitmapDrawable> drawables;
	
	public LMyPagerView(Context context)
	{
		this(context,null);
	}
	
	public LMyPagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		View view = LayoutInflater.from(context).inflate(R.layout.lyt_pager_view, null);
		addView(view);
		
		indicatorView = (TextView)view.findViewById(R.id.view_page_indicator);
		closeView     = view.findViewById(R.id.view_close_page);
		pageView      = (PagedView)view.findViewById(R.id.view_page);
		
		closeView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				parentGroup.removeView(LMyPagerView.this);
				
			}
			
		});
	}
	
	public void open(ViewGroup parentGroup)
	{
		this.parentGroup = parentGroup;
		ViewGroup.LayoutParams params = 
				new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		parentGroup.addView(this,params);
	}
	
	private void setIndicator(int index)
	{
		indicatorView.setText((index + 1) + " of " + TOTAL_COUNT);
	}
	
	public void setPhoto(List<String> list,String selectedPath,int width,int height)
	{
		setVisibility(View.VISIBLE);
		
		TOTAL_COUNT = list.size();
		drawables = new ArrayList<BitmapDrawable>(list.size());
		for(String filePath : list)
		{
			Bitmap bm = LCommonUtil.loadBitmap(filePath, width,height);
			BitmapDrawable bmDrawable = new BitmapDrawable(getContext().getResources(),bm);
			drawables.add(bmDrawable);
		}
		
		ImageAdapter adapter = new ImageAdapter(getContext());
		pageView.setAdapter(adapter);
		pageView.setOnPageChangeListener(mOnPagedViewChangedListener);
		
		int initialIndex = list.indexOf(selectedPath);
		LLog.info("size:" + list.size() + " initial index:" + initialIndex);
		setIndicator(initialIndex);
		pageView.smoothScrollToPage(initialIndex);	
	}
	
    public class ImageAdapter extends PagedAdapter {

        private  Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return drawables.size();
        }

        public Object getItem(int position) {
            return drawables.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = null;
           
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT,Gallery.LayoutParams.MATCH_PARENT));
            imageView.setImageDrawable((Drawable)getItem(position));
        
            return imageView;
        }
    }

	
	 private PagedView.OnPagedViewChangeListener mOnPagedViewChangedListener = new PagedView.OnPagedViewChangeListener() {

	        @Override
	        public void onStopTracking(PagedView pagedView) {
	        }

	        @Override
	        public void onStartTracking(PagedView pagedView) {
	        }

	        @Override
	        public void onPageChanged(PagedView pagedView, int previousPage, int newPage) {
	            //
	        	LLog.info("new page index = " + newPage);
	        	setIndicator(newPage);
	        }
	    };
}
