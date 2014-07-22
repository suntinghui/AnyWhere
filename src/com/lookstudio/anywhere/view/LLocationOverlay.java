package com.lookstudio.anywhere.view;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.AMap;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

public class LLocationOverlay{
	
	private static int count = 1;
	
	public static void showPoiAt(AMap aMap,LatLonPoint point,String title)
	{
		PoiItem item = new PoiItem("overlay " + count, point, title, "");
		count ++;
		
		List<PoiItem> items = new ArrayList<PoiItem>(1);
		items.add(item);
		
		aMap.clear();
		PoiOverlay overlay = new PoiOverlay(aMap,items);
		overlay.removeFromMap();
		overlay.addToMap();
		overlay.zoomToSpan();
	}

}
