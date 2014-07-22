package com.lookstudio.anywhere.model;

import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.interfaces.LMediatorable;
import com.lookstudio.anywhere.view.LDriveOverviewMediator;

public class LDriveOverviewInfo implements LMediatorable{

	
	public double totalDistance;
	
	public int    totalTime;
	private transient LDriveOverviewMediator mediator;
	public LDriveOverviewInfo(){}
	public LDriveOverviewInfo(double totalDistance,int totalTime)
	{
		this.totalDistance = totalDistance;
		this.totalTime     = totalTime;
	}
	
	@Override
	public LMediator createMediator() {
		if(null == mediator)
		{
			mediator = new LDriveOverviewMediator();
		}
		return mediator;
	}

}
