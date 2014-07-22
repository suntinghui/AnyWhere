package com.lookstudio.anywhere.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnTimedTextListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.Marker;
import com.lookstudio.anywhere.MainActivity;
import com.lookstudio.anywhere.R;
import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.model.LDriveComment;
import com.lookstudio.anywhere.model.LLocationManager;
import com.lookstudio.anywhere.model.LTravelProxy;
import com.lookstudio.anywhere.model.LocateProxy;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LConstant;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.util.sound.PhotoUtil;
import com.lookstudio.anywhere.util.sound.ScrollViewItem;
import com.lookstudio.anywhere.util.sound.ScrollViewItem.OnItemRemovedListener;
import com.lookstudio.anywhere.view.LMarkerManager.MarkerInfo;

public class LBottomLayoutMediator implements OnClickListener,
		MainActivity.ResultCallBack {

	private ViewGroup mStartStopLayout;
	private View mLocateView;
	private ImageView mControlView;
	private View mEditView;

	// private View mSendTextLayout;
	private View mEditTextLayout;
	private View mPrepareCommentLayout;
	private View mGoRecordVoiceView;
	private View mGoTakePhotoView;
	private EditText mMessageInputView;
	private View mDoSendView;

	private View mPhotoRecordLayout;
	private View mRecordLayout;
	private ImageView mRecordStateView;
	private View mPhotoLayout;
	private View mFromCamera;
	private View mFromGallery;

	private LocateProxy locateProxy;
	private LTravelProxy travelProxy;

	private FrameLayout editEreaLayout;
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewGroup mContainer;
	MainActivity mMainActivity;
	private PhotoUtil photoUtil;
	private View mBottomLayout;
	private Animation invisibleAnim;
	private Animation visibleAnim;
	private boolean isShowControlPanel = false;
	
	public LBottomLayoutMediator(MainActivity mainActivity, View container,
			LocateProxy locateProxy, LTravelProxy travelProxy) {
		this.locateProxy = locateProxy;
		this.travelProxy = travelProxy;
		mBottomLayout = container.findViewById(R.id.view_bottom_control);
		mContext = LApplication.appContext;
		mMainActivity = mainActivity;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContainer = (ViewGroup) container;
		initRecordViewLayout();

		photoUtil = new PhotoUtil(mainActivity);
		mMainActivity.setResultCallBack(this);

		
		mStartStopLayout = (ViewGroup)container.findViewById(R.id.group_control_panel);
		mStartStopLayout.setOnTouchListener(disableTouch);
		mLocateView = container.findViewById(R.id.map_location);
		mControlView = (ImageView) (container
				.findViewById(R.id.map_start_or_end));
		mControlView.setImageResource(R.drawable.map_start);
		mControlView.setEnabled(true);
		mControlView.setClickable(true);
		mEditView = container.findViewById(R.id.map_edit);
		mLocateView.setOnClickListener(this);
		mControlView.setOnClickListener(this);
		mEditView.setOnClickListener(this);

		
		mEditTextLayout = container.findViewById(R.id.group_edit_text);
		mEditTextLayout.setOnTouchListener(disableTouch);
		mPrepareCommentLayout = container.findViewById(R.id.group_prepare_comment);
		mGoRecordVoiceView = container.findViewById(R.id.view_go_record_voice);
		mGoTakePhotoView = container.findViewById(R.id.view_go_take_photo);
		mMessageInputView = (EditText) container
				.findViewById(R.id.view_message_input);
		mDoSendView = container.findViewById(R.id.view_do_send);

		mGoRecordVoiceView.setOnClickListener(this);
		mGoTakePhotoView.setOnClickListener(this);
		mDoSendView.setOnClickListener(this);
		editEreaLayout = (FrameLayout) container
				.findViewById(R.id.map_record_erea);
		
		invisibleAnim = AnimationUtils.loadAnimation(mContext, R.anim.slide_top_to_bottom_self);
		visibleAnim = AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_to_top_self);
		visibleAnim.setStartOffset(invisibleAnim.getDuration() + 100);
		invisibleAnim.setAnimationListener(invisibleAnimListener);
		showControlPanel();
	}

	private AnimationListener invisibleAnimListener = new AnimationListener()
	{

		@Override
		public void onAnimationStart(Animation paramAnimation) {
			
			
		}

		@Override
		public void onAnimationEnd(Animation paramAnimation) {
			LLog.warn("onAnimationEnd");
			
			if(isShowControlPanel)
			{
				
			}
			else
			{
				
				
			}
			
		}

		@Override
		public void onAnimationRepeat(Animation paramAnimation) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public void showControlPanel() {
		// mSendTextLayout.setVisibility(View.GONE);
		
		if(false == isShowControlPanel)
		{
			isShowControlPanel = true;
			
			mEditTextLayout.setVisibility(View.GONE);
			mEditTextLayout.startAnimation(invisibleAnim);
			
			hideKeyBoard();
			
			mStartStopLayout.setVisibility(View.VISIBLE);
			mStartStopLayout.startAnimation(visibleAnim);
		}
	}

	public void showSendTextPanel() {
		
		if(isShowControlPanel)
		{
			isShowControlPanel = false;
			editEreaLayout.setVisibility(View.GONE);
			mPrepareCommentLayout.setVisibility(View.VISIBLE);
			
			mStartStopLayout.setVisibility(View.GONE);
			mStartStopLayout.startAnimation(invisibleAnim);
			
			mEditTextLayout.setVisibility(View.VISIBLE);
			mEditTextLayout.startAnimation(visibleAnim);
		}
	}

	private void startOrEnd() {

		if (travelProxy.isStarted()) {
			travelProxy.stop();
			mControlView.setEnabled(false);
			mControlView.setClickable(false);
		} else {
			travelProxy.start();
			mControlView.setImageResource(R.drawable.map_end);
		}

	}

	private RelativeLayout soundViewLayout;
	private FrameLayout playSoundLayout;
	private View photoViewLayout;
	private RelativeLayout recordeLayout;

	private void initRecordViewLayout() {
		initSoundView();
		initPlaySoundLayout();
		initPhotoView();
		initRecordeView();
	}

	private void changeState(View currentView) {
		editEreaLayout.setVisibility(View.VISIBLE);
		editEreaLayout.removeAllViews();
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		if(currentView == playSoundLayout)
		{
			params.gravity = Gravity.CENTER;
		}
		

		editEreaLayout.addView(currentView,params);
	}

	ImageView cameraView;
	ImageView myPhotoView;
	HorizontalScrollView photoScrollView;

	LinearLayout scrollLinearLayout;
	private     View photoChoiceLayout;
	private void initPhotoView() {
		photoViewLayout =  mInflater.inflate(
				R.layout.map_photo_layout, null);

		photoChoiceLayout = photoViewLayout
				.findViewById(R.id.layout_photo_choice);
		cameraView = (ImageView) photoViewLayout
				.findViewById(R.id.map_photo_camera);
		cameraView.setOnClickListener(this);
		myPhotoView = (ImageView) photoViewLayout
				.findViewById(R.id.map_photo_my);
		myPhotoView.setOnClickListener(this);

		photoScrollView = (HorizontalScrollView) photoViewLayout
				.findViewById(R.id.map_photo_gallery);

		scrollLinearLayout = (LinearLayout) photoScrollView
				.findViewById(R.id.map_sroll_linear);
	}

	private Map<String,Bitmap> photos = new HashMap<String,Bitmap>();
	
	@Override
	public void doResult(Bitmap imageBitmap) {
		LLog.info("doResult,original bitmap,width,height" + imageBitmap.getWidth() + "," + imageBitmap.getHeight());
		LLog.info("doResult,parent width = " + photoViewLayout.getWidth()+ "height = " + photoViewLayout.getHeight());
		LLog.info("doResult,top child width = " + photoChoiceLayout.getWidth()+ "height = " + photoChoiceLayout.getHeight());
		
		int scaleHeight = photoChoiceLayout.getHeight() + 80;
		int scaleWidth  = LCommonUtil.getWidthInConstraint(scaleHeight);
		String pathName = photoUtil.saveBitmap(imageBitmap);
		Bitmap scaleBm = LCommonUtil.scaleBitmap(imageBitmap,scaleWidth,scaleHeight);
		imageBitmap.recycle();
		LLog.info("doResult,scale bitmap,width,height" + scaleBm.getWidth() + "," + scaleBm.getHeight());
		ScrollViewItem item = new ScrollViewItem(mMainActivity);
		item.setBackGroud(scaleBm,pathName);
		item.setOnItemRemovedListener(new OnItemRemovedListener()
		{

			@Override
			public void onItemRemoved(View view, Object extra) {
				Bitmap bm = photos.get(extra);
				photos.remove(extra);
				bm.recycle();
			}
			
		});
		
		int width = scaleBm.getWidth();
		int height = scaleBm.getHeight();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		params.leftMargin = 3;
		params.rightMargin = 3;
		scrollLinearLayout.addView(item, params);
		
		photos.put(pathName,scaleBm);
	}

	private void getPhotoFromCamera() {
		photoUtil.dispatchTakePictureIntent();
	}

	private void getPhotoFromBook() {
		photoUtil.takePhotos();
	}

	private ImageView recordView;
	private TextView soundView;
	private int[] soundViewIds = new int[] { R.drawable.map_sound_view_1,
			R.drawable.map_sound_view_2, R.drawable.map_sound_view_3,
			R.drawable.map_sound_view_4, R.drawable.map_sound_view_5,
			R.drawable.map_sound_view_6,R.drawable.map_sound_view_7};
	private int currentId = 0;
	private long divideTime;
	private int soundTime;
	private Handler mTimeHandler = new Handler();
	private ScheduledExecutorService mSchelExe = Executors
			.newScheduledThreadPool(5);

	SoundUtil soundUtil = new SoundUtil();
	private Runnable delayRunnable = new Runnable() {

		@Override
		public void run() {
			startRecord();

		}
	};

	private void startRecord() {
		soundView.setVisibility(View.VISIBLE);
		soundView.setText("" + 0);
		mSchelExe = Executors.newScheduledThreadPool(5);
		mSchelExe.scheduleAtFixedRate(new Runnable() {

			public void run() {
				mTimeHandler.post(new Runnable() {

					@Override
					public void run() {
						recordView.setImageResource(soundViewIds[(currentId++)%soundViewIds.length]);
						soundView.setText("" + soundTime++ / 5 + "''");
					}
				});

			}
		}, 0, 150, TimeUnit.MILLISECONDS);

		mSchelExe.execute(new Runnable() {

			@Override
			public void run() {
				soundUtil.startRecord();
			}
		});
	}

	private void stopRecord() {
		soundView.setVisibility(View.GONE);
		mTimeHandler.removeCallbacks(delayRunnable);
		recordView.setImageResource(R.drawable.map_sound_view_0);
		currentId = 0;
		soundTime = 0;
		mSchelExe.shutdownNow();
	}

	private void initSoundView() {
		soundViewLayout = (RelativeLayout) mInflater.inflate(
				R.layout.map_sound_view_layout, null);
		recordView = (ImageView) soundViewLayout
				.findViewById(R.id.map_sound_button);
		soundView = (TextView) soundViewLayout
				.findViewById(R.id.map_sound_time);
		recordView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE: {
					break;
				}
				case MotionEvent.ACTION_UP: {
					divideTime = System.currentTimeMillis() - divideTime;
					stopRecord();
					if (divideTime > 1000) {
						changeState(playSoundLayout);
						soundUtil.stopRecord();
					} else {
						Toast.makeText(mContext, "ʱ��̫��", Toast.LENGTH_LONG)
								.show();
					}
					break;
				}
				case MotionEvent.ACTION_DOWN: {
					divideTime = System.currentTimeMillis();
					mTimeHandler.postDelayed(delayRunnable, 1000);
					break;

				}
				}
				return true;
			}

		});
	}

	private boolean isPlay;
	ImageView playView;

	private void initPlaySoundLayout() {
		playSoundLayout = (FrameLayout) mInflater.inflate(
				R.layout.map_play_sound_layout, null);

		final ImageView replayView = (ImageView) playSoundLayout
				.findViewById(R.id.map_replay);
		replayView.setOnClickListener(this);
		playView = (ImageView) playSoundLayout
				.findViewById(R.id.map_play_or_pause_button);
		playView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPlay) {
					playView.setImageResource(R.drawable.selector_play);
					soundUtil.pause();
					isPlay = false;
				} else {
					playView.setImageResource(R.drawable.selector_stop);
					soundUtil.play();
					isPlay = true;
				}
			}
		});
	}

	private void initRecordeView() {
		recordeLayout = (RelativeLayout) mInflater.inflate(
				R.layout.map_record_dialog_layout, null);
		recordeLayout.setOnTouchListener(disableTouch);
		final Button record = (Button) recordeLayout
				.findViewById(R.id.map_record_btn);
		record.setOnClickListener(this);
		final Button cancel = (Button) recordeLayout
				.findViewById(R.id.map_cancel_record);
		cancel.setOnClickListener(this);

		mContainer.addView(recordeLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		recordeLayout.setVisibility(View.GONE);
	}

	private void showRecordeDialog() {
		recordeLayout.setVisibility(View.VISIBLE);
	}

	private void hideRecordeDialog() {
		recordeLayout.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {

		if (view == mLocateView) {
			locateProxy.locate();
		} else if (view == mControlView) {
			startOrEnd();
		} else if (view == mEditView) {

			if (travelProxy.isStarted()) {
				showSendTextPanel();
			} else {
				ToastUtil.show(mContext, "请开始驾驶");
			}

		} else if (view == mGoRecordVoiceView) {
			recordView.setImageResource(R.drawable.map_sound_view_0);
			changeState(soundViewLayout);
			hideKeyBoard();

		} else if (view == mGoTakePhotoView) {
			changeState(photoViewLayout);
			hideKeyBoard();
		} else if (view == mDoSendView) {
			sendComment();
			
		} else if (view == mFromCamera) {
			
		} else if (view == mFromGallery) {
			
		}

		switch (view.getId()) {
		case R.id.map_photo_camera:
			getPhotoFromCamera();
			break;
		case R.id.map_photo_my:
			getPhotoFromBook();
			break;

		case R.id.map_replay:
			showRecordeDialog();
			break;
			
		case R.id.map_record_btn:
			hideRecordeDialog();
			changeState(soundViewLayout);
			if(null != soundUtil)
			{
				soundUtil.path_name = null;
			}
			recordView.setImageResource(R.drawable.map_sound_view_0);
			break;
			
		case R.id.map_cancel_record:
			hideRecordeDialog();
			break;
		default:
			break;
		}
	}
 
/*	private void showTexts(List<String>texts)
	{
		StringBuilder builder = new StringBuilder();
		for(String text : texts)
		{
			builder.append(text + "\n");
		}
		
		View view = LayoutInflater.from(mContext).inflate(R.layout.map_show_texts, null);
		TextView showTextsView = (TextView)view.findViewById(R.id.view_show_texts);
		showTextsView.setText(builder.toString());
		
		view.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mContainer.removeView(v);
				return true;
			}
			
		});
		
		mContainer.addView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}*/
	
	/*private void showPhotos(List<String> photos)
	{
		final View view = LayoutInflater.from(mContext).inflate(R.layout.map_show_photos, null);
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//mContainer.removeView(v);
				return true;
			}
		});
		
		//view.setOnTouchListener(disableTouch);
		
		View closeView = view.findViewById(R.id.view_close_photos);
		closeView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				mContainer.removeView(view);
			}
			
		});
		PhotoPreview preview = (PhotoPreview)view.findViewById(R.id.view_show_photos);
		preview.setPhoto(photos);
		preview.setOnTouchListener(disableTouch);
		mContainer.addView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}*/
	
	private void showScanChoice(final LDriveComment comment) {
		
		
		if(comment.hasText() && !comment.hasPhoto() && !comment.hasRadio())
		{
			
			//showTexts(comment.getTexts());
		}
		else if(!comment.hasText() && comment.hasPhoto() && !comment.hasRadio())
		{
			
			//showPhotos(comment.getPhotos());
		}
		else if(!comment.hasText() && !comment.hasPhoto() && comment.hasRadio())
		{
			
			//LCommonUtil.play(comment.getRadio());
		}
		else
		{
			final View scanLayout = (RelativeLayout) mInflater.inflate(
					R.layout.map_skan_dialog, null);
			
			Button scanSound = (Button)scanLayout.findViewById(R.id.map_scan_sound);
			scanSound.setVisibility(comment.hasRadio()?View.VISIBLE:View.GONE);
			scanSound.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					/*LCommonUtil.play(comment.getRadio());
					mContainer.removeView(scanLayout);*/
					
				}
				
			});
			Button scanPhoto = (Button)scanLayout.findViewById(R.id.map_scan_photo);
			scanPhoto.setVisibility(comment.hasPhoto()?View.VISIBLE:View.GONE);
			scanPhoto.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					//showPhotos(comment.getPhotos());
					mContainer.removeView(scanLayout);
					
				}
				
			});
			Button scanText  = (Button)scanLayout.findViewById(R.id.map_scan_text);
			scanText.setVisibility(comment.hasText()?View.VISIBLE:View.GONE);
			scanText.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					//showTexts(comment.getTexts());
					mContainer.removeView(scanLayout);
					
				}
				
			});
			Button scanCancel = (Button)scanLayout.findViewById(R.id.map_scan_cancel);
			
			scanCancel.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View view) {
					mContainer.removeView(scanLayout);
					
				}
				
			});
			scanLayout.setOnTouchListener(disableTouch);

			mContainer.addView(scanLayout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		
		
	}

/*	public void showError() {
		final View scanLayout = (RelativeLayout) mInflater.inflate(
				R.layout.map_error_dialog, null);
		View confirmBtn = scanLayout.findViewById(R.id.map_error_confirm);
		confirmBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view) {
				mContainer.removeView(scanLayout);
				
			}
			
		});
		scanLayout.setOnTouchListener(disableTouch);

		mContainer.addView(scanLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}*/

	private void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mMessageInputView.getWindowToken(), 0);
	}


	private void sendComment() {
		
		String text = mMessageInputView.getText().toString();
		mMessageInputView.setText("");
		showControlPanel();
		
		if((true == TextUtils.isEmpty(text)) 
			&&(true == TextUtils.isEmpty(soundUtil.path_name))
			&&(true == photos.isEmpty()))
		{
			
			return;
		}
		
		LDriveComment comment = new LDriveComment();

		if(false == TextUtils.isEmpty(text)){
			comment.setText(text);
		}
		
		
		scrollLinearLayout.removeAllViews();
		for(Entry<String,Bitmap> entry : photos.entrySet())
		{
			comment.addPhoto(entry.getKey());
			entry.getValue().recycle();
		}
		photos.clear();
		
		if(null != soundUtil)
		{
			comment.setRadio(soundUtil.path_name);
			soundUtil.path_name = "";
		}
		
		AMapLocation loc = LLocationManager.getManager().getLastKnownLocation();
		if((null == loc) || (false == isValid(loc)))
		{
			LLog.warn("发表时无法获取位置信息");
			ToastUtil.show(mContext,"无法获取位置信息");
			return;
		}
		comment.setLatitude(loc.getLatitude());
		comment.setLongitude(loc.getLongitude());
		
		travelProxy.comment(comment);
	}

	private boolean isValid(Location location)
	{
		return (0.0 != location.getLatitude() ) && (0.0 != location.getLongitude());
	}
	
	public void hide() {
		mBottomLayout.setVisibility(View.GONE);
	}

	

	public class SoundUtil {

		private MediaRecorder recorder;
		private String PATH_ROOT;
		private String path_name;

		private String lastPlayStr;

		private MediaPlayer player;

		public SoundUtil() {
			recorder = new MediaRecorder();

			PATH_ROOT = LCommonUtil.getStorageDirectory().toString();
			player = new MediaPlayer();
		}

		public void startRecord() {
			long time = System.currentTimeMillis();
			path_name = new File(PATH_ROOT,time + ".3gp").toString();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(path_name);
			try {
				recorder.prepare();
			} catch (Exception e) {
				Log.i("SoundUtil", "startRecord");
			}
			recorder.start(); // Recording is now started
		}

		public void stopRecord() {
			try {
				recorder.stop();
			} catch (RuntimeException stopException) {
				// handle cleanup here
			}
			recorder.reset();
			playView.setImageResource(R.drawable.selector_play);
			soundUtil.pause();
			isPlay = false;
		}

		private void prepareAndSetSource() {
			try {
				player = new MediaPlayer();
				player.setDataSource(path_name);
			} catch (Exception e) {
			}
			try {
				player.prepare();
			} catch (Exception e) {
				Log.i("SoundUtil", "player.prepare()");
			}

		}

		public void pause() {
			player.pause();
		}

		public void play() {
			if (!path_name.equals(lastPlayStr)) {
				player.stop();
				player.reset();
				player.release();
				prepareAndSetSource();
			}
			player.setOnCompletionListener(new OnCompletionListener()
			{

				@Override
				public void onCompletion(MediaPlayer player) {
					isPlay = false;
					playView.setImageResource(R.drawable.selector_play);
				}
				
			});
			
			player.start();
			lastPlayStr = path_name;
		}
		
		public String getPathName() {
			return path_name;
		}
	}

	private OnTouchListener disableTouch = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return true;
		}
		
	};
}
