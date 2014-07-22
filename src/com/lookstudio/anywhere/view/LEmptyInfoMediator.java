package com.lookstudio.anywhere.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.interfaces.LMediator;

public class LEmptyInfoMediator implements LMediator{

	@Override
	public View getView(Context context) {
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		
		ImageView view = new ImageView(context);
		
		view.setLayoutParams(new AbsListView.LayoutParams(metrics.widthPixels,
				metrics.heightPixels - 240));
		view.setImageAlpha(0);
		view.setImageResource(R.drawable.img_empty_info);
		
		return view;
	}

	@Override
	public void fillViewWithData(Object obj,Object extra,BaseAdapter adapter) {
		// TODO Auto-generated method stub
		
	}

}
