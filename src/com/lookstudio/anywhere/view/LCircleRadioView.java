package com.lookstudio.anywhere.view;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.util.LLog;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class LCircleRadioView extends LinearLayout {

	private int totalTime;
	private Timer timer;
	private final int INTERVAL = 100;
	private int currentProgress;
	private long startTimeInMillis = 0;
	private String filePath;
	private CircleProgress circleProgress;
	
	public LCircleRadioView(Context context,String filePath) {
		super(context);
		this.filePath = filePath;
		View view = LayoutInflater.from(context).inflate(R.layout.lyt_circle_progress, this);
		circleProgress = (CircleProgress)view.findViewById(R.id.view_circle_progress);
		circleProgress.setSubProgress(0);
		circleProgress.setMainProgress(0);
		circleProgress.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.img_play_normal));
		circleProgress.requestLayout();
		circleProgress.invalidate();
		
		circleProgress.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				start();
				
			}
			
		});
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			updateProgress();
		}
	};
	
	private void start()
	{
		circleProgress.setEnabled(false);
		circleProgress.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.img_stop_normal));
		circleProgress.requestLayout();
		circleProgress.invalidate();
		
		
		new Thread()
		{
			@Override
			public void run()
			{
				play();
			}
		}.start();
	}
	
	private void play()
	{
		MediaPlayer player = new MediaPlayer();
		
		if (player.isPlaying())
		{
			player.stop();
		}
		player.setOnCompletionListener(new OnCompletionListener()
		{

			@Override
			public void onCompletion(MediaPlayer m) {
				circleProgress.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.img_play_normal));
				circleProgress.setMainProgress(0);
				circleProgress.requestLayout();
				circleProgress.invalidate();
				circleProgress.setEnabled(true);
			}
			
		});
		try {
			player.reset();
			player.setDataSource(filePath);
			player.prepare();
			
			totalTime = player.getDuration();
			timer = new Timer("play_updater");
			timer.schedule(counter, 0, INTERVAL);
			startTimeInMillis = System.currentTimeMillis();
			player.start();
			timer.cancel();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			LLog.error("error" ,e);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			LLog.error("error" ,e);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			LLog.error("error" ,e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LLog.error("error" ,e);
		}
	
	}
	private void updateProgress()
	{
		if(null != circleProgress)
		{
			circleProgress.setMainProgress(currentProgress);
		}
		
	}
	
	private long elipseTime()
	{
		return System.currentTimeMillis() - startTimeInMillis;
	}
	
	private TimerTask counter = new TimerTask()
	{

		@Override
		public void run() {
			currentProgress = (int)(elipseTime()/ totalTime * 100);
			LLog.info("current progress:" + currentProgress + " total time:" + totalTime);
			if(!( elipseTime() >= totalTime))
			{
				handler.sendEmptyMessage(0);
			}
			
		}
		
	};
}
