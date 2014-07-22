package com.lookstudio.anywhere.view;

import com.lookstudio.anywhere.interfaces.LMediator;
import com.lookstudio.anywhere.interfaces.LMediatorable;

public class LEmptyInfo implements LMediatorable{

	@Override
	public LMediator createMediator() {

		return new LEmptyInfoMediator();
	}

}
