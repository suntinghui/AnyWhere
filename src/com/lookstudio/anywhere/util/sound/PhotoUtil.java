package com.lookstudio.anywhere.util.sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.provider.MediaStore;

import com.lookstudio.anywhere.util.LCommonUtil;

public class PhotoUtil {
	private String path_name;

	public static final int REQUEST_TAKE_PHOTO = 1;
	public static final int REQUEST_IMAGE_CAPTURE = 2;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private Activity mActivity;

	public PhotoUtil(Activity activity) {
		mActivity = activity;
	}

	public String saveBitmap(Bitmap imageBitmap) {
		long time = System.currentTimeMillis();
		String path_name = time + ".png";

		File dir = LCommonUtil.getStorageDirectory();
		File file = new File(dir, path_name);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		imageBitmap.compress(CompressFormat.PNG, 100, fos);
		try {
			fos.flush();
		} catch (IOException e) {
		}
		try {
			fos.close();
		} catch (IOException e) {
		}

		return file.getAbsolutePath();
	}

	public void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mActivity.startActivityForResult(takePictureIntent,
				REQUEST_IMAGE_CAPTURE);
	}

	public void takePhotos() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		mActivity.startActivityForResult(intent, REQUEST_TAKE_PHOTO);
	}
	
	
}
