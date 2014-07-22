package com.lookstudio.anywhere.util;

public class LThreadFactory {

	private LThreadFactory(){}
	private static int count = 1;
	public static Thread createThread(Runnable runnable)
	{
		return createThread("",runnable);
	}
	
	public static Thread createThread(String name,Runnable runnable)
	{
		if((null == name) || ("".equals(name)))
		{
			name = "thread[" + count + "]";
		}
		
		Thread thread = new Thread(runnable);
		thread.setName(name);

		return thread;
	}
}
