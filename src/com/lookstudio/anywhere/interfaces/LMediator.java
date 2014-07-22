package com.lookstudio.anywhere.interfaces;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

public interface LMediator {

	public View getView(Context context);
	public void fillViewWithData(Object obj,Object extra,BaseAdapter adapter);
}
