package com.lookstudio.anywhere.util.sound;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lookstudio.anywhere.R;

public class ScrollViewItem extends FrameLayout {
	private ImageView backView;
	private ImageView cancelView;
	private OnItemRemovedListener onItemRemovedListener;
	private String  filePath;
	
	public interface OnItemRemovedListener
	{
		public void onItemRemoved(View view,Object extra);
	}
	
	public ScrollViewItem(Context context) {
		super(context);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final FrameLayout layout = (FrameLayout) mInflater.inflate(
				R.layout.map_sroll_view_layout, null);
		backView = (ImageView) layout.findViewById(R.id.map_scroll_back);

		cancelView = (ImageView) layout.findViewById(R.id.map_scroll_cancel);

		cancelView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if(null != onItemRemovedListener)
				{
					onItemRemovedListener.onItemRemoved(view, filePath);
				}
				//…æ≥˝ ”Õº
				((ViewGroup) getParent()).removeView(ScrollViewItem.this);

			}
		});
		addView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}

	public void setOnItemRemovedListener(OnItemRemovedListener listener)
	{
		onItemRemovedListener = listener;
	}
	
	public void setBackGroud(Bitmap imageBitmap,String filePath) {
		backView.setImageBitmap(imageBitmap);
		this.filePath = filePath;
	}
}
