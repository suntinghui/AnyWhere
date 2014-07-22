package com.lookstudio.anywhere.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.bitmap.BitmapLoader;
import com.lookstudio.anywhere.bitmap.views.AsyncImageView;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.model.LDriveRecord;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LConstant;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.ToastUtil;

public class LDriveRecordMediator implements LMediator{

	private AsyncImageView screenshotView;
	private TextView  dateView;
	private TextView  startEndView;
	private TextView  distanceView;
	private TextView  timeView;
	private View      shareView;
	private View      deleteView;
	private View view;
	public LDriveRecordMediator()
	{
		
	}
	
	@Override
	public View getView(Context context) {
		if(null != view)
		{
			return view;
		}
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.lyt_drive_record,null);
		
		
		screenshotView = (AsyncImageView)view.findViewById(R.id.view_record_screenshot);
		dateView = (TextView)view.findViewById(R.id.view_record_date);
		startEndView = (TextView)view.findViewById(R.id.view_record_startend);
		distanceView = (TextView)view.findViewById(R.id.view_record_distance);
		timeView     = (TextView)view.findViewById(R.id.view_record_time);
		
		shareView = view.findViewById(R.id.view_share_drive_record);
		deleteView = view.findViewById(R.id.view_delete_drive_record);
		
		LLog.info("screen shot,width :" + LConstant.SCREENSHOT_WIDTH + " height:" + LConstant.SCREENSHOT_HEIGHT);
		return view;
	}

	@Override
	public void fillViewWithData(Object obj,Object extra,final BaseAdapter adapter) {
		
		if(!(obj instanceof LDriveRecord))
		{
			return;
		}
		final LDriveRecord record = (LDriveRecord)obj;

		screenshotView.setImageUrl(record.screenshotPath, (BitmapLoader)extra);
		
		dateView.setText(record.date);
		startEndView.setText(record.start + "到" + record.end);
		distanceView.setText(LCommonUtil.fromMtoKM(record.distanceInMeter) + "km");
		timeView.setText(LCommonUtil.formatTime(record.timeInSeconds));
		
		shareView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view) {
				((HomeAdapter)adapter).shareDriveRecord(record);
			}
			
		});
		
		deleteView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view) {
				((HomeAdapter)adapter).deleteRecord(record);
			}
			
		});
	}

}
