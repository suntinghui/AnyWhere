package com.lookstudio.anywhere;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.SEARCH_MODE;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;
//import com.lookstudio.anywhere.view.PullToRefreshListView;
//import com.lookstudio.anywhere.view.PullToRefreshListView.OnRefreshListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.fortysevendeg.android.swipelistview.BaseSwipeListViewListener;
import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.bitmap.BitmapLoader;
import com.lookstudio.anywhere.bitmap.caches.LMyDiskCache;
import com.lookstudio.anywhere.blureffect.Blur;
import com.lookstudio.anywhere.blureffect.ImageUtils;
import com.lookstudio.anywhere.model.LCreateTrackInfo;
import com.lookstudio.anywhere.model.LCreateTrackProxy;
import com.lookstudio.anywhere.model.LCreateTrackProxy.OnFinish2Listener;
import com.lookstudio.anywhere.model.LDriveRecord;
import com.lookstudio.anywhere.model.LDriveRecordProxy;
import com.lookstudio.anywhere.model.LLocation;
import com.lookstudio.anywhere.model.LLocationManager;
import com.lookstudio.anywhere.model.LSaver;
import com.lookstudio.anywhere.model.TaskHandler;
import com.lookstudio.anywhere.model.TaskHandler.Task;
import com.lookstudio.anywhere.util.LCommonUtil;
import com.lookstudio.anywhere.util.LConstant;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.LScreenUtil;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.util.sound.PhotoUtil;
import com.lookstudio.anywhere.view.HomeAdapter;
import com.lookstudio.anywhere.view.LMyLocationMediator;
import com.lookstudio.anywhere.view.LSlidingMenuMediator;
import com.lookstudio.anywhere.view.MyPullToRefreshListView;
import com.lookstudio.anywhere.view.MyPullToRefreshListView.OnRefreshListener;
import com.lookstudio.anywhere.whether.LWeatherInfo;
import com.lookstudio.anywhere.whether.LWeatherUpdater;
import com.lookstudio.anywhere.whether.LWeatherUpdater.OnGotWeatherInfoListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class HomeActivity extends CommonActivity{

	private LSlidingMenuMediator mSlidingMenuMediator;
	private LWeatherUpdater      mWeatherUpdater;
	private MyPullToRefreshListView listView;
	private HomeAdapter adapter;
	private BitmapLoader mBitmapLoader;
	private long timeInMillis = 0;
	//Blur:BEGIN
	private static final String BLURRED_IMG_PATH = "blurred_image.png";
	private static final int TOP_HEIGHT = 700;
	private ImageView mBlurredImage;
	private View headerView;
	private ImageView mNormalImage;
	private LMyLocationMediator myLocationMediator;
	private LDriveRecordProxy   driveRecordProxy;
	private Handler             delayTaskHandler = new Handler();
	private Handler             uiHandler = new Handler();
	private Runnable            delayTask;
	private final       boolean TEST_PULL_TO_REFRESH = false;
	private float alpha;
	private ProgressDialog progressDialog;
	private LCreateTrackProxy createTrackProxy;
	
	//Blue:END
	
	public static final String ACTION_MODIFY_USER_ICON = "action_maoddiy_user_icon";
	public static final String ACTION_LOGOUT = "action_logout";
	private static final boolean JUMP_HOME = false;
	
	private IWXAPI wxApi;  
	private String WX_APP_ID = "wx02fdaec6049ca437";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		final View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_home, null);
		setContentView(view);
		
		//实例化  
		wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID);  
		wxApi.registerApp(WX_APP_ID);  
		
		wechatShare(1);
		mBitmapLoader  = new BitmapLoader(this,((LApplication)getApplication()).getBitmapCache(),new LMyDiskCache(getApplicationContext()));
		init(view);
		
		
		LSaver saver = new LSaver(getApplicationContext());
		
		//adapter.loadData(saver.readWeatherInfo(LWeatherInfo.getDefault()), driveRecordProxy.driveOverviewInfo(saver.readRecords()),saver.readRecords());
		LWeatherInfo defaultInfo = LWeatherInfo.getDefault();
		LWeatherInfo weatherInfo = saver.readWeatherInfo(defaultInfo);
		adapter.loadData(weatherInfo, driveRecordProxy.driveOverviewInfo(saver.readRecords()),saver.readRecords());
		if(weatherInfo.equals(defaultInfo))
		{
			if(LCommonUtil.isNetworkActivate(getApplicationContext()))
			{
				updateWeather();
				Toast.makeText(getApplicationContext(), R.string.str_getting_weather, Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), R.string.str_no_network, Toast.LENGTH_LONG).show();
			}
			
		}
		
		if(JUMP_HOME)
		{
			LScreenUtil.forward(getApplicationContext(), MainActivity.class);
		}
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_NEW_RECORD_COME);
		filter.addAction(ACTION_MODIFY_USER_ICON);
		filter.addAction(ACTION_LOGOUT);
		registerReceiver(receiver, filter);
	}
	
	private void wechatShare(int flag){  
	    WXWebpageObject webpage = new WXWebpageObject();  
	    webpage.webpageUrl = "http";  
	    WXMediaMessage msg = new WXMediaMessage(webpage);  
	    msg.title = "摩托帮";  
	    msg.description = "我的骑行轨迹";  
	    //这里替换一张自己工程里的图片资源  
	    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
	    msg.setThumbImage(thumb);  
	      
	    SendMessageToWX.Req req = new SendMessageToWX.Req();  
	    req.transaction = String.valueOf(System.currentTimeMillis());  
	    req.message = msg;  
	    req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;  
	    wxApi.sendReq(req);  
	} 
	@Override
	public void onResume()
	{
		super.onResume();
		showSystemUI();
		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mBitmapLoader.shutdownNow();
		unregisterReceiver(receiver);
	}
	
	private void refreshRecords()
	{
		LSaver saver = new LSaver(getApplicationContext());
		adapter.update(LCommonUtil.driveOverviewInfo(saver.readRecords()), saver.readRecords());
	}
	
	private void init(View view)
	{
		driveRecordProxy = new LDriveRecordProxy();
		myLocationMediator = new LMyLocationMediator(view);
		mWeatherUpdater = new LWeatherUpdater(new Handler(),getApplicationContext());
		mWeatherUpdater.setOnGotWeatherInfoListener(onGotWeatherInfoListener);
		
		TaskHandler.getHandler().exeTask(new Task()
		{
			private static final int NUM = 10;
			@Override
			public int delayInMillis() {
				
				return NUM * Task.MINUTE;
			}

			@Override
			public boolean onRun() {
				LLog.info("begin to update weather for the period of " + NUM + " minutes");
				updateWeather();
				return false;
			}
			
		});
		//weatherProxy = new LWeatherProxy(mWeatherUpdater);
		mSlidingMenuMediator = new LSlidingMenuMediator(view);
		
		initBluer();
		
		
		//LWeatherUtil.updateWeather(mWeatherUpdater, listView, getApplicationContext());
	}
	
	private OnGotWeatherInfoListener onGotWeatherInfoListener = new OnGotWeatherInfoListener()
	{

		@Override
		public void onGot(LWeatherInfo info) {
			
			LLog.info("得到天气信息:" + info);
			if(info.isValid)
			{
				LSaver saver = new LSaver(getApplicationContext());
				saver.writeWeatherInfo(info);
				
				if(null != adapter)
				{
					adapter.updateWeatherInfo(info);
				}
			}
			//listView.onRefreshComplete();
			timeInMillis = System.currentTimeMillis();
		}
		
	};
	
	private int[] HOME_BG = {R.drawable.bg_home_1,R.drawable.bg_home_2,
			R.drawable.bg_home_3,R.drawable.bg_home_4,
			R.drawable.bg_home_5,R.drawable.bg_home_6,
			R.drawable.bg_home_7,R.drawable.bg_home_8,
			R.drawable.bg_home_9,R.drawable.bg_home_10};
	private int CURRENT_BLUR_IMAGE = R.drawable.bg_home_style_1;
	private void initBluer()
	{
		// Get the screen width
			final int screenWidth = ImageUtils.getScreenWidth(this);

			// Find the view
			mBlurredImage = (ImageView) findViewById(R.id.blurred_image);
			mNormalImage = (ImageView) findViewById(R.id.normal_image);
			listView =  (MyPullToRefreshListView)findViewById(R.id.pull_to_refresh_listview);
			listView.setSwipeCloseAllItemsWhenMoveList(true);
			mBlurredImage.setAlpha(alpha);
			//mBlurredImage.setAlpha(0f);


			Random r = new Random();
			CURRENT_BLUR_IMAGE = HOME_BG[r.nextInt(HOME_BG.length)];
			// Try to find the blurred image
			final File blurredImage = new File(getFilesDir() + BLURRED_IMG_PATH);
			
			if(true){
				// launch the progressbar in ActionBar
				setProgressBarIndeterminateVisibility(true);
				mSlidingMenuMediator.setBackgroundResource(CURRENT_BLUR_IMAGE);
				new Thread(new Runnable() {

					@Override
					public void run() {

						// No image found => let's generate it!
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						
						Bitmap image = BitmapFactory.decodeResource(getResources(), CURRENT_BLUR_IMAGE, options);
						Bitmap newImg = Blur.fastblur(HomeActivity.this, image, 12);
						ImageUtils.storeImage(newImg, blurredImage);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								updateView(screenWidth);

								// And finally stop the progressbar
								setProgressBarIndeterminateVisibility(false);
							}
						});

					}
				}).start();

			} else {

				// The image has been found. Let's update the view
				updateView(screenWidth);

			}

			String[] strings = getResources().getStringArray(R.array.list_content);

			// Prepare the header view for our list
			headerView = new View(this);
			headerView.setBackgroundResource(0);
			headerView.setClickable(false);
			headerView.setEnabled(false);
			headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TOP_HEIGHT));
			headerView.setBackgroundResource(0);
			listView.addHeaderView(headerView);
			//listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, strings));
			listView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {

				}

				/**
				 * Listen to the list scroll. This is where magic happens ;)
				 */
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

					// Calculate the ratio between the scroll amount and the list
					// header weight to determinate the top picture alpha
				
					alpha = (float) -headerView.getTop() / (float) TOP_HEIGHT;
					// Apply a ceil
					if (alpha > 1) {
						alpha = 1;
					}

					// Apply on the ImageView if needed
					
					mBlurredImage.setAlpha(alpha);
					

					// Parallax effect : we apply half the scroll amount to our
					// three views
					mBlurredImage.setTop(headerView.getTop() / 2);
					mNormalImage.setTop(headerView.getTop() / 2);
					

				}
			});
			
			// OPTIONAL: Disable scrolling when list is refreshing
			// listView.setLockScrollWhileRefreshing(false);

			// OPTIONAL: Uncomment this if you want the Pull to Refresh header to show the 'last updated' time
			// listView.setShowLastUpdatedText(true);

			// OPTIONAL: Uncomment this if you want to override the date/time format of the 'last updated' field
			// listView.setLastUpdatedDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));

			// OPTIONAL: Uncomment this if you want to override the default strings
			// listView.setTextPullToRefresh("Pull to Refresh");
			// listView.setTextReleaseToRefresh("Release to Refresh");
			// listView.setTextRefreshing("Refreshing");

			// MANDATORY: Set the onRefreshListener on the list. You could also use
			// listView.setOnRefreshListener(this); and let this Activity
			// implement OnRefreshListener.
			/*listView.setOnRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					LLog.info("pull to refresh");
					// Your code to refresh the list contents goes here

					// for example:
					// If this is a webservice call, it might be asynchronous so
					// you would have to call listView.onRefreshComplete(); when
					// the webservice returns the data
					
					LWeatherUtil.updateWeather(mWeatherUpdater, listView, getApplicationContext());
					// Make sure you call listView.onRefreshComplete()
					// when the loading is done. This can be done from here or any
					// other place, like on a broadcast receive from your loading
					// service or the onPostExecute of your AsyncTask.

					// For the sake of this sample, the code will pause here to
					// force a delay when invoking the refresh
					listView.postDelayed(new Runnable() {

						
						@Override
						public void run() {
							listView.onRefreshComplete();
						}
					}, 2000);
				}
			});
*/
			adapter = new HomeAdapter(this,mBitmapLoader,listView);
			listView.setAdapter(adapter);
			
			listView.setOnRefreshListener(new OnRefreshListener()
			{

				@Override
				public void onRefresh() {
					if(TEST_PULL_TO_REFRESH)
					{
						delayTaskHandler.removeCallbacks(delayTask);
						delayTaskHandler.postDelayed(delayTask, 2000);
						return;
					}
					
					
					/*if(0 == timeInMillis)
					{
						listView.setLastUpdated(getString(R.string.str_first_refresh));
					}
					else
					{
						String text = "";
						String elapseTime = LCommonUtil.getElapseTime((int)((System.currentTimeMillis() - timeInMillis)));
						if("".equals(elapseTime))
						{
							elapseTime = "1分钟";
						}
						listView.setLastUpdated(getString(R.string.str_refresh_before).replace("%TIME", elapseTime));
					}*/
					updateWeather();
				}
				
			});
			// click listener
			/*listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int postion,
						long id) {

						Object obj = view.getTag();
						if(obj instanceof LDriveRecord)
						{
							onDriveRecordSelected((LDriveRecord)obj);
						}
				}
			});*/
			
	        listView.setSwipeListViewListener(new BaseSwipeListViewListener() {
	            @Override
	            public void onChoiceChanged(int position, boolean selected) {
	               LLog.info("SwipeListViewListener onChoiceChanged position = " + position + " selected = " + selected);
	            }

	            @Override
	            public void onChoiceEnded() {
	            	LLog.info("SwipeListViewListener onChoiceEnded");
	            }

	            @Override
	            public void onChoiceStarted() {
	            	LLog.info("SwipeListViewListener onChoiceStarted");
	            }

	            @Override
	            public void onClickBackView(int position) {
	            	LLog.info("SwipeListViewListener onClickBackView position = " + position);
	            }

	            @Override
	            public void onClickFrontView(View view,int position) {
	                
	            	LLog.info("onClickFrontView,tag = " + view.getTag() + " position = " + position);
					if(view.getTag()  instanceof LDriveRecord)
					{
						onDriveRecordSelected((LDriveRecord)view.getTag());
					}
	            }

	            @Override
	            public void onClosed(int position, boolean fromRight) {
	            	LLog.info("SwipeListViewListener onClosed position = " + position + " fromRight = " + fromRight);
	            }

	            @Override
	            public void onDismiss(int[] arg0) {
	            	LLog.info("SwipeListViewListener onDismiss arg0 = " + arg0);
	            }

	            @Override
	            public void onFirstListItem() {
	            	LLog.info("SwipeListViewListener onFirstListItem");
	            }

	            @Override
	            public void onLastListItem() {
	            	LLog.info("SwipeListViewListener onLastListItem");
	            }

	            @Override
	            public void onListChanged() {
	            	LLog.info("SwipeListViewListener onListChanged");
	            }

	            @Override
	            public void onMove(int position, float x) {
	            	LLog.info("SwipeListViewListener onMove position = " + position + " x = " + x);
	            }

	            @Override
	            public void onOpened(int position, boolean toRight) {
	            	LLog.info("SwipeListViewListener onOpened position = " + position + " toRight=" + toRight);
	            }

	            @Override
	            public void onStartClose(int position, boolean right) {
	            	LLog.info("SwipeListViewListener ");
	            }

	            @Override
	            public void onStartOpen(int position, int action, boolean right) {
	            	LLog.info("SwipeListViewListener onStartOpen position = " + position + " action = " + action + " right = " + right);
	            }
	        });

	}
	
	private void updateView(final int screenWidth) {
		Bitmap bmpBlurred = BitmapFactory.decodeFile(getFilesDir() + BLURRED_IMG_PATH);
		/*bmpBlurred = Bitmap.createScaledBitmap(bmpBlurred, screenWidth, (int) (bmpBlurred.getHeight()
				* ((float) screenWidth) / (float) bmpBlurred.getWidth()), false);*/

		bmpBlurred = Bitmap.createScaledBitmap(bmpBlurred, screenWidth, bmpBlurred.getHeight() + 1024, false);
		
		mBlurredImage.setImageBitmap(bmpBlurred);

		
	}
	
	private void onDriveRecordSelected(LDriveRecord record)
	{
		Bundle b = new Bundle();
		b.putInt(LConstant.EXTRA_MAP_SCOPE, LConstant.MAP_SCOPE_SCAN);
		b.putSerializable(LConstant.EXTRA_DRIVE_RECORD,record);
		LScreenUtil.forward(getApplicationContext(), MainActivity.class,b);
	} 
	
	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000, true);
	private boolean isUpdating = false;
	private void updateWeather()
	{
		if(isUpdating)
		{
			LLog.info("already in updating weather");
			return;
		}
		
		if(!LCommonUtil.isNetworkActivate(getApplicationContext()))
		{
			LLog.warn("no active network for the moment");
			return;
		}
		AMapLocation loc = LLocationManager.getManager().getLastKnownLocation();
		if(null == loc)
		{
			return;
		}
		isUpdating = true;
		mYahooWeather.setNeedDownloadIcons(false);
		mYahooWeather.setSearchMode(SEARCH_MODE.GPS);
		mYahooWeather.queryYahooWeatherByLatLon(getApplicationContext(),loc.getLatitude() + "", loc.getLongitude() + "", new YahooWeatherInfoListener()
		{

			@Override
			public void gotWeatherInfo(final WeatherInfo weatherInfo) {
				uiHandler.post(new Runnable()
				{

					@Override
					public void run() {
						if(null != weatherInfo)
						{
							LWeatherInfo weather = new LWeatherInfo(weatherInfo);
							onGotWeatherInfoListener.onGot(weather);
						}
						isUpdating = false;
						listView.onRefreshComplete();
					}
				});
			
			}
			
		});
	}
	
	public static final String ACTION_NEW_RECORD_COME = "action_new_record_come";
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(null == intent)
			{
				return;
			}
			LLog.info("receive action " + intent.getAction() + " @Home");
			if(ACTION_NEW_RECORD_COME.equals(intent.getAction()))
			{
				refreshRecords();
			}
			else if(ACTION_MODIFY_USER_ICON.equals(intent.getAction()))
			{
				modifyUserIcon();
			}
			else if(ACTION_LOGOUT.equals(intent.getAction()))
			{
				finish();
			}
			else
			{
				
			}
		}
		
	};
	
	
	private View photoChoice;
	private boolean isChoiceViewVisible()
	{
		return View.VISIBLE == photoChoice.getVisibility();
	}
	
	private void showChoiceView()
	{
		Animation visibleAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_bottom_to_top_self);
		photoChoice.startAnimation(visibleAnim);
		photoChoice.setVisibility(View.VISIBLE);
	}
	private void hideChoiceView()
	{
		Animation invisibleAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_top_to_bottom_self);
		photoChoice.startAnimation(invisibleAnim);
		photoChoice.setVisibility(View.GONE);
	}
	
	private void modifyUserIcon()
	{
		photoChoice = findViewById(R.id.lyt_choice_photo_source);
		photoChoice.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(isChoiceViewVisible())
				{
					hideChoiceView();
				}
				return true;
			}
			
		});
		View takePhotoView = photoChoice.findViewById(R.id.view_choice_take_photo);
		takePhotoView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				new PhotoUtil(HomeActivity.this).dispatchTakePictureIntent();
				photoChoice.setVisibility(View.GONE);
			}
			
		});
		View fromGalleryView = photoChoice.findViewById(R.id.view_choice_from_gallery);
		fromGalleryView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				new PhotoUtil(HomeActivity.this).takePhotos();
				photoChoice.setVisibility(View.GONE);
			}
			
		});
		
		View cancelView = photoChoice.findViewById(R.id.view_choice_cancel);
		cancelView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				hideChoiceView();
				
			}
			
		});
		showChoiceView();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PhotoUtil.REQUEST_IMAGE_CAPTURE
				&& resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			mSlidingMenuMediator.modifyUserIcon(imageBitmap);
			// mImageView.setImageBitmap(imageBitmap);
		} else {
			if (requestCode == PhotoUtil.REQUEST_TAKE_PHOTO
					&& resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 3;

				Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath,
						options);

				mSlidingMenuMediator.modifyUserIcon(imageBitmap);

			}
		}
	}
	
	public void shareDriveRecord(LDriveRecord record)
	{
		String ori = record.start;
		String dst = record.end;
		String len = record.distanceInMeter +"";
		String dur = record.timeInSeconds +"";
		ArrayList<String> coordsList = new ArrayList<String>();
		for(int i=0; i<record.locations.size();i++){
			LLocation location = record.locations.get(i);
			coordsList.add(location.longitude+"");
			coordsList.add(location.latitude+"");
		}
		String coords = coordsList.toString();
		progressDialog = ProgressDialog.show(this, "", this.getString(R.string.str_registering), true,false);
		Long tsLong = System.currentTimeMillis()/1000;
		String ts = tsLong.toString();
		LCreateTrackInfo info = new LCreateTrackInfo(ori,dst,len,dur,coords, ts);
		createTrackProxy.createTrack(info, getApplicationContext(), new OnFinish2Listener()
		{

			public void onFinish(final boolean successful, final String errorInfo) {
				
				runOnUiThread(new Runnable()
				{

					@Override
					public void run() {
						if(null != progressDialog)
						{
							progressDialog.dismiss();
							progressDialog = null;
						}
						
						if(successful)
						{
//							onRegisterSuccess();
							Log.i("create track tag", "success");
						}
						else
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
							builder.setTitle(R.string.str_fail_to_register);
							if(!"".equals(errorInfo))
							{
								builder.setMessage(errorInfo);
							}
							builder.setPositiveButton(R.string.str_button_close,null);
							builder.show();
						}
						
					}
					
				}); 
		
				
			}
			
		});
	    	
	}
}
