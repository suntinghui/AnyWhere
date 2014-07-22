package com.lookstudio.anywhere;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.lookstudio.anywhere.model.LDriveComment;
import com.lookstudio.anywhere.model.LDriveRecord;
import com.lookstudio.anywhere.model.LLocation;
import com.lookstudio.anywhere.model.LMapManager;
import com.lookstudio.anywhere.model.LTravelProxy;
import com.lookstudio.anywhere.model.LocateProxy;
import com.lookstudio.anywhere.util.LConstant;
import com.lookstudio.anywhere.util.LLog;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.util.sound.PhotoUtil;
import com.lookstudio.anywhere.view.LBackFromMapMediator;
import com.lookstudio.anywhere.view.LBottomLayoutMediator;
import com.lookstudio.anywhere.view.LCommentPreviewMediator;
import com.lookstudio.anywhere.view.LControlMapTypeMediator;
import com.lookstudio.anywhere.view.LMarkerManager;
import com.lookstudio.anywhere.view.LMarkerManager.MarkerInfo;
import com.lookstudio.anywhere.view.LMyLocationMarker;
import com.lookstudio.anywhere.view.LWholeInfoMediator;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends CommonActivity {

	private LMapManager mMapManager;
	private LMarkerManager    mMarkerManager;
	public static final int   LOCATION_MARKER = 0x1011;
	private LBackFromMapMediator mBackMediator;
	private LControlMapTypeMediator mControlMapTypeMediator;
	private LBottomLayoutMediator mBottomLayoutMediator;
	private LTravelProxy travelProxy;
	private LocateProxy locateProxy;
	private ViewGroup container;
	private LCommentPreviewMediator commentPrevieMediator;
	private LDriveRecord currentRecord;
	
	private LWholeInfoMediator wholeInfoMediator;
	private FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		container = (ViewGroup)inflater.inflate(R.layout.activity_main, null, true);
		setContentView(container);
		
		MapView mapView = (MapView)container.findViewById(R.id.mapview);
		mMapManager = new LMapManager();
		mMapManager.create(getApplicationContext(), container, mapView, savedInstanceState);
		mMapManager.init();
		
		mMapManager.getMapController().get().setOnMapClickListener(new OnMapClickListener()
		{

			@Override
			public void onMapClick(LatLng latLng) {

				if((null != commentPrevieMediator) && (false == commentPrevieMediator.isHidden()))
				{
					commentPrevieMediator.setHidden(true);
				}
				
				mBottomLayoutMediator.showControlPanel();
			}
			
		});

		mMarkerManager = new LMarkerManager(mMapManager.getMapController());
		travelProxy = new LTravelProxy(mMapManager.getMapController(),mMarkerManager);
		
		wholeInfoMediator = new LWholeInfoMediator(container);
		wholeInfoMediator.hide();
		travelProxy.setTravelListener(wholeInfoMediator);
		
		View controlMapType = findViewById(R.id.view_map_type_control);
		mControlMapTypeMediator = new LControlMapTypeMediator(controlMapType,mMapManager.getMapController());
		
		
		locateProxy = new LocateProxy(mMapManager.getMapController(),getApplicationContext());
		mBottomLayoutMediator = new LBottomLayoutMediator(this,container,locateProxy,travelProxy);
		travelProxy.setOnMarkerClickListener(onMarkerClickListener);
		
		mBackMediator = new LBackFromMapMediator(container,this,travelProxy);
		
		Bundle extras = getIntent().getExtras();
		
		if(null != extras)
		{
			if(LConstant.MAP_SCOPE_SCAN == extras.getInt(LConstant.EXTRA_MAP_SCOPE,LConstant.MAP_SCOPE_GO_DRIVE))
			{
				LDriveRecord driveRecord = (LDriveRecord)extras.getSerializable(LConstant.EXTRA_DRIVE_RECORD);
				currentRecord = driveRecord;
				onScanMode(driveRecord);
				
			}
			else
			{
				onDriveMode();
			}
		}
		
		initCommentPreview();
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		hideSystemUI();
		mMapManager.onResume();
	}


	@Override
	protected void onPause() {
		super.onPause();
		mMapManager.onPause();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapManager.onSaveInstanceState(outState);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapManager.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			mBackMediator.onBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public interface ResultCallBack {
		void doResult(Bitmap imageBitmap);
	}

	private ResultCallBack mResult;

	public void setResultCallBack(ResultCallBack callBack) {
		mResult = callBack;
	}
	private void onDriveMode()
	{
		mBackMediator.setScanMode(false);
		locateProxy.locate();
	}
	
	private void onScanMode(LDriveRecord record)
	{
		mBottomLayoutMediator.hide();
		mControlMapTypeMediator.hide();
		mBackMediator.setScanMode(true);
		
		mMarkerManager.showMarkers(record.comments,onMarkerClickListener);
		
		//定位到起点
		if((null != record.comments) && (false == record.comments.isEmpty()))
		{
			double latitude  = record.comments.get(0).getLatitude();
			double longitude = record.comments.get(0).getLongitude();
			LatLng latlng = new LatLng(latitude,longitude);
			mMapManager.getMapController().animateTo(latlng);
		}
		
		mMapManager.getMapController().drawTravel(record.locations);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PhotoUtil.REQUEST_IMAGE_CAPTURE
				&& resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			mResult.doResult(imageBitmap);
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
				// 获取屏幕分辨率

//				DisplayMetrics dm_2 = new DisplayMetrics();
//				getWindowManager().getDefaultDisplay().getMetrics(dm_2);
//
//				// 图片分辨率与屏幕分辨率
//				float scale_2 = imageBitmap.getWidth()
//						/ (float) dm_2.widthPixels;
//				 Bitmap newBitMap_2 = null;  
//				if (scale_2 > 1) {
//					newBitMap_2 = zoomBitmap(imageBitmap,
//							imageBitmap.getWidth() / scale_2,
//							imageBitmap.getHeight() / scale_2);
				//}
				mResult.doResult(imageBitmap);

			}
		}
	}
   
	
	private void initCommentPreview()
	{
		View preview = container.findViewById(R.id.view_comment_preview);
		commentPrevieMediator = new LCommentPreviewMediator(preview,container,mMapManager.getMapController());
		commentPrevieMediator.setHidden(true);
	}
	
	public OnMarkerClickListener onMarkerClickListener = new OnMarkerClickListener() {

		@Override
		public boolean onMarkerClick(Marker marker) {
			
			if(null == currentRecord)
			{
				currentRecord = travelProxy.getRecord();
			}
			
			MarkerInfo info = (MarkerInfo) marker.getObject();
			commentPrevieMediator.update(currentRecord,info.comment);
			commentPrevieMediator.setHidden(false);
			
			return true;
		}

	};
	
}
