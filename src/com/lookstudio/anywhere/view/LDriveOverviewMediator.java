package com.lookstudio.anywhere.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.model.LDriveOverviewInfo;
import com.lookstudio.anywhere.util.LCommonUtil;

public class LDriveOverviewMediator implements LMediator{

	private TextView mTotalDistanceView;
	private TextView mTotalTimeView;
	private View view;
	public LDriveOverviewMediator()
	{
		
	}

	@Override
	public View getView(Context context) {
		if(null != view)
		{
			return view;
		}
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.lyt_drive_overview, null);
		
		mTotalDistanceView = (TextView)view.findViewById(R.id.view_total_drive_distance);
		mTotalTimeView = (TextView)view.findViewById(R.id.view_total_drive_time);
		
		return view;
	}

	@Override
	public void fillViewWithData(Object obj,Object extra,BaseAdapter adapter) {
		
		if(!(obj instanceof LDriveOverviewInfo))
		{
			return;
		}
		LDriveOverviewInfo info = (LDriveOverviewInfo)obj;
		
		mTotalDistanceView.setText(LCommonUtil.fromMtoKM(info.totalDistance) + "km");
		mTotalTimeView.setText(LCommonUtil.formatTimeNormal(info.totalTime));
	}
}
