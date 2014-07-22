package com.lookstudio.anywhere.model;

public class TimeHandler 
	{
		private int hour;
		private int minute;
		private int second;
		
		public TimeHandler(int timeInMillis)
		{
			divide(timeInMillis);
		}
		
		private void divide(int timeInMillis)
		{
			int totalSecond = (int)(timeInMillis/1000);
			minute = (int)(totalSecond/60);
			second = (int)(totalSecond%60);
			hour   = (int)(minute/60);

			minute %=60;
		}
		
		public int getHour()
		{
			return hour;
		}
		
		public int getMinute()
		{
			return minute;
		}
		
		public int getSecond()
		{
			return second;
		}
		
		public String getHourWithPre0()
		{
			return preWith0(hour);
		}
		
		public String getMinuteWithPre0()
		{
			return preWith0(minute);
		}
		
		public String getSecondWithPre0()
		{
			return preWith0(second);
		}
		
		private String preWith0(int num)
		{
			String result = "" + num;
			if(num < 10)
			{
				result =  "0" + num;
			}
			
			return result;
		}
		
		public String toString()
		{
			return getHourWithPre0() + " : " + getMinuteWithPre0() + " : " + getSecondWithPre0();
		}
	}
