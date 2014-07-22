package com.lookstudio.anywhere.view;

import java.util.ArrayList;
import java.util.List;

import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PhotoPreview extends Gallery {

	private static final boolean USE_TEST_DATA = false;
	
	private List<BitmapDrawable> drawables;
	public PhotoPreview(Context context) {
		this(context,null);
		
	}
	
	public PhotoPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public void setPhoto(List<String> list,int width,int height)
	{
		setVisibility(View.VISIBLE);
		
		drawables = new ArrayList<BitmapDrawable>(list.size());
		for(String filePath : list)
		{
			Bitmap bm = LCommonUtil.loadBitmap(filePath, width,height);
			BitmapDrawable bmDrawable = new BitmapDrawable(getContext().getResources(),bm);
			drawables.add(bmDrawable);
		}
		
		ImageAdapter adapter = new ImageAdapter(getContext());
		setAdapter(adapter);
		
	}
	
    public class ImageAdapter extends BaseAdapter {

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
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT,Gallery.LayoutParams.WRAP_CONTENT));
            // The preferred Gallery item background
            
            imageView.setImageDrawable((Drawable)getItem(position));
            
            
            //TEST BEGIN
            imageView.setBackgroundColor(Color.GREEN);
            //TEST END
            return imageView;
        }
    }
    
}
