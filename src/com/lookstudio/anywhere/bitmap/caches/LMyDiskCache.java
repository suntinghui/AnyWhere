package com.lookstudio.anywhere.bitmap.caches;

import java.io.File;

import android.content.Context;

public class LMyDiskCache extends SimpleLruDiskCache {

	public LMyDiskCache(Context ctx) {
		super(ctx);
		
	}

	@Override
	public synchronized File getFile(String fileName)
	{
		return new File(fileName);
	}
	
}
