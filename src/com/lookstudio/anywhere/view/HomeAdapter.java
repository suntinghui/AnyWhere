package com.lookstudio.anywhere.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;









import com.lookstudio.anywhere.HomeActivity;
import com.lookstudio.anywhere.LoginActivity;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.bitmap.BitmapLoader;
import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.interfaces.LMediatorable;
import com.lookstudio.anywhere.model.LCreateTrackInfo;
import com.lookstudio.anywhere.model.LCreateTrackProxy;
import com.lookstudio.anywhere.model.LDriveOverviewInfo;
import com.lookstudio.anywhere.model.LDriveRecord;
import com.lookstudio.anywhere.model.LLocation;
import com.lookstudio.anywhere.model.LLoginProxy;
import com.lookstudio.anywhere.model.LRegisterInfo;
import com.lookstudio.anywhere.model.LSaver;
import com.lookstudio.anywhere.model.LLoginProxy.OnFinishListener;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.whether.LWeatherInfo;


public class HomeAdapter extends android.widget.BaseAdapter{

	private Handler uiHandler = new Handler();
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<LMediatorable> items = new ArrayList<LMediatorable>(8);
	private BitmapLoader bitmapLoader;
	private MyPullToRefreshListView listView;
	private ProgressDialog progressDialog;
	private LCreateTrackProxy createTrackProxy;

	public HomeAdapter(Context context,BitmapLoader mBitmapLoader,MyPullToRefreshListView listView)
	{
		this.bitmapLoader = mBitmapLoader;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.listView = listView;
		
		createTrackProxy = new LCreateTrackProxy();
	}
	
	public void updateWeatherInfo(LWeatherInfo weatherInfo)
	{
		if(items.isEmpty())
		{
			items.add(weatherInfo);
		}
		else
		{
			items.set(0, weatherInfo);
		}
		
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}
	
	public void update(LDriveOverviewInfo overviewInfo,List<LDriveRecord> records)
	{
		if(items.isEmpty())
		{
			return;
		}
		LMediatorable weatherInfo = items.get(0);
		loadData(weatherInfo,overviewInfo,records);
	}
	
	public void deleteRecord(LDriveRecord record)
	{
		LLog.info("deleteRecord:" + record);
		
		listView.closeOpenedItems();
		
		LSaver s = new LSaver(context);
		s.deleteRecord(record);
		items.remove(record);

		update(LCommonUtil.driveOverviewInfo(s.readRecords()),s.readRecords());
		uiHandler.postDelayed(new Runnable()
		{

			@Override
			public void run() {
				notifyDataSetChanged();
				
			}
			
		}, 500);
	}
	
	public void shareDriveRecord(LDriveRecord record)
	{
		ToastUtil.show(context, "恭喜，点击了分享");
		LLog.info("shareDriveRecord:" + record);
		((HomeActivity) context).shareDriveRecord(record);
	}
	
	
	
	public void loadData(LMediatorable weatherInfo,LMediatorable overviewInfo,List<LDriveRecord> records)
	{
		items.clear();
		items.add(weatherInfo);
		items.add(overviewInfo);
		
		if(null != records)
		{
			items.addAll(records);
		}
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}
	
	
    @Override
    public boolean isEnabled(int position)
    {
    	if(1 >= position)
    	{
    		return false;
    	}
    	
    	return true;
    }
    
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LMediator mediator = null;
		LMediatorable data = (LMediatorable)getItem(position);
		mediator = data.createMediator();
		convertView = mediator.getView(context);
		convertView.setTag(data);
		mediator.fillViewWithData(data,bitmapLoader,this);
		return convertView;
	}
	
/*	public class Holder
	{
		public LMediator mediator;
		public LMediatorable data;
		
		public Holder(){}
		public Holder(LMediator mediator,LMediatorable data){
			this.mediator = mediator;
			this.data     = data;
		}
	}*/
}
