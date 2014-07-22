package com.lookstudio.anywhere.util;

public enum SpeedLevel {

	LEVEL_ZERO(0,1,0,"无风"),
	LEVEL_ONE(1,5,1,"软风"),
	LEVEL_TWO(6,11,2,"轻风"),
	LEVEL_THREE(12,19,3,"微风"),
	LEVEL_FOUR(20,28,4,"和风"),
	LEVEL_FIVE(29,38,5,"清风"),
	LEVEL_SIX(39,49,6,"强风"),
	LEVEL_SEVEN(50,61,7,"劲风"),
	LEVEL_EIGHT(62,74,8,"大风"),
	LEVEL_NINE(75,88,9,"烈风"),
	LEVEL_TEN(89,102,10,"狂风"),
	LEVEL_ELEVEN(103,117,11,"暴风"),
	LEVEL_TWELVE(117,10000,12,"台风");
	
	
	int low;
	int high;
	int levelNum;
	String desc;
	
	SpeedLevel(int low,int high,int levelNum,String desc)
	{
		this.low = low;
		this.high = high;
		this.levelNum = levelNum;
		this.desc     = desc;
	}
	
	public boolean isLevel(int speed)
	{
		return (speed >= low) && (speed <= high);
	}
	

	
	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}

	public int getLevelNum() {
		return levelNum;
	}

	public String getDesc() {
		return desc;
	}

	public static SpeedLevel getLevel(int speed)
	{
		SpeedLevel result = LEVEL_ZERO;
		
		for(SpeedLevel level : values())
		{
			if(level.isLevel(speed))
			{
				result = level;
				break;
			}
		}
		
		return result;
	}
}
