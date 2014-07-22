package com.lookstudio.anywhere.view;

import com.lookstudio.anywhere.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class LLoginMediator implements OnClickListener{

	private TextView registerView;
	private EditText userNameView;
	private EditText passwordView;
	private TextView loginView;
	private TextView forgetPasswordView;
	private boolean  login_action = true;
	
	public LLoginMediator(View v)
	{
		
	}
	
	private void registerListeners()
	{
		registerView.setOnClickListener(this);
		loginView.setOnClickListener(this);
		forgetPasswordView.setOnClickListener(this);
	}

	private void login()
	{
		
	}
	
    private void register()
    {
    	
    }
    
	@Override
	public void onClick(View v) {
		if(registerView == v)
		{
			login_action = false;
			userNameView.setText("");
			passwordView.setText("");
			registerView.setVisibility(View.GONE);
			forgetPasswordView.setVisibility(View.GONE);
		}
		else if(loginView == v)
		{
			if(login_action)
			{
				login();
			}
			else
			{
				register();
			}
		}
		else if(forgetPasswordView == v)
		{
			
		}
		else
		{
			
		}
		
	}
}
