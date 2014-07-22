package com.lookstudio.anywhere.model;

import java.util.HashSet;
import java.util.Set;

public class LProxyFactory {

	private static HashSet<Object> proxys = new HashSet<Object>();
	
	private LProxyFactory(){}

	public static Object getProxy(Class<?> cls)
	{
		
		Object found = find(cls);
		//Not found
		if(null == found)
		{
			try {
				Object newObj  = cls.newInstance();
				proxys.add(newObj);
				found = newObj;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return found;
	}
	
	private static Object find(Class<?> cls)
	{
		Object found = null;
		
		for(Object obj : proxys)
		{
			if(obj.getClass().equals(cls))
			{
				found = obj;
				break;
			}
		}
		
		return found;
	}
}
