package com.lookstudio.anywhere.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.amap.api.maps.AMap.OnMapScreenShotListener;
import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.model.LMapController;

public class LScreenshot implements OnMapScreenShotListener{

	private OnScreenshotFinishListener listener;
	
	public interface OnScreenshotFinishListener
	{
		//path��Ϊ�ձ�ʾ�ɹ��������ʾʧ��
		public void onScreenshotFinish(String path);
	}
	
	public void screenshort(LMapController controller)
	{
		controller.get().getMapScreenShot(this);
	}
	
	public void setOnScreenshotFinishListener(OnScreenshotFinishListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void onMapScreenShot(Bitmap bitmap) {
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = sdf.format(new Date()) + ".png";
			
			File dir = LCommonUtil.getStorageDirectory();
			File file = new File(dir,fileName);
			FileOutputStream fos = new FileOutputStream(file);
			boolean b = bitmap.compress(CompressFormat.PNG, 100, fos);
			try {
				fos.flush();
			} catch (IOException e) {
				LLog.error("screen shot error",e);
				if(null != listener)
				{
					listener.onScreenshotFinish("");
				}
			}
			try {
				fos.close();
			} catch (IOException e) {
				LLog.error("screen shot error",e);
				if(null != listener)
				{
					listener.onScreenshotFinish("");
				}
			}
			if (!b)
			{
				
				if(null != listener)
				{
					listener.onScreenshotFinish("");
				}
			}
			else
			{
				if(null != listener)
				{
					listener.onScreenshotFinish(file.getAbsolutePath());
				}
			}
			bitmap.recycle();	
		} catch (FileNotFoundException e) {
			LLog.error("����ʧ��",e);
			if(null != listener)
			{
				listener.onScreenshotFinish("");
			}
		}
	
	}

}
