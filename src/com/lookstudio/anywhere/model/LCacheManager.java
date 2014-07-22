package com.lookstudio.anywhere.model;

import java.util.HashMap;
import java.util.Map;

public class LCacheManager {

	private static Map<String,Object> objects = new HashMap<String,Object>();
	
	private static LCacheManager instance;
	private LCacheManager(){}
	
	public static LCacheManager get()
	{
		if(null == instance)
		{
			instance = new LCacheManager();
		}
		
		return instance;
	}
	
	public void cache(String key,Object obj)
	{
		objects.put(key, obj);
	}
	
	public Object get(String key)
	{
		return objects.get(key);
	}
}
