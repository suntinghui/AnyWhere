package com.lookstudio.anywhere.model;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.lookstudio.anywhere.http.Constant;
import com.lookstudio.anywhere.http.LHttpCommunication;
import com.lookstudio.anywhere.http.LResponse;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LThreadFactory;
import com.lookstudio.anywhere.util.ToastUtil;

public class LCreateTrackProxy {

	public static final int ERROR_NOT_REGISTER_YET = 0x1001;
	public static final int ERROR_USERNAME         = 0x1002;
	public static final int ERROR_PASSWORD         = 0x1003;
	public static final int ERROR_NO_ERROR         = 0x1004;
	public static final boolean TEST_MODE          = false;
	
	public interface OnFinish2Listener
	{
		
		public void onFinish(boolean successful,String errorInfo);
	}
	
//	public LCreateTrackInfo getLoginInfo(Context context)
//	{
//		LSaver saver = new LSaver(context);
//		LLoginInfo old = saver.readLoginInfo();
//		return old;
//	}
	
	public void createTrack(final LCreateTrackInfo info,final Context context,final OnFinish2Listener onFinishListener)
	{
		LLog.info("createTrack" + "111111");

		if(TEST_MODE)
		{
			new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run() {
					if(null != onFinishListener)
					{
						onFinishListener.onFinish(false, "okay,we are playing joke");
					}
					
				}
				
			}, 1*1000);
			return;
		}
		
		LThreadFactory.createThread("Thread create track",new Runnable()
		{

			@Override
			public void run()
			{
				LCreateTrackRequest request = new LCreateTrackRequest(info);
				String result = LHttpCommunication.post(request);
				if(null != result)
				{
					LResponse<LCreateTrackResponse> response = request.parse(result);
					if(response.isResultOk())
					{
						LCreateTrackResponse res = response.getBean();
						Constant.shareId = res.getId();
						if(null != onFinishListener)
						{
							onFinishListener.onFinish(true, "");
						}

					}
					else
					{
						if(null != onFinishListener)
						{
							onFinishListener.onFinish(false, "");
						}
						
					}
					
				}
				else
				{
					if(null != onFinishListener)
					{
						onFinishListener.onFinish(false, "");
					}
				}
			}
			
		}).start();
		
	}
	
}
  