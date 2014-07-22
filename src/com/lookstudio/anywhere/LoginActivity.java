package com.lookstudio.anywhere;

import com.lookstudio.anywhere.app.LApplication;
import com.lookstudio.anywhere.model.LAppManager;
import com.lookstudio.anywhere.model.LLoginInfo;
import com.lookstudio.anywhere.model.LLoginProxy;
import com.lookstudio.anywhere.model.LLoginProxy.OnFinishListener;
import com.lookstudio.anywhere.model.LRegisterInfo;
import com.lookstudio.anywhere.model.LSaver;
import com.lookstudio.anywhere.util.ToastUtil;
import com.lookstudio.anywhere.view.LLoginMediator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends CommonActivity implements OnClickListener {

	private static final boolean JUMP_LOGIN = false;
	private TextView registerView;
	private EditText userNameView;
	private EditText passwordView;
	private TextView loginView;
	private TextView forgetPasswordView;
	private boolean  login_action = true;
	private LLoginProxy loginProxy;
	private ProgressDialog progressDialog;
	private static boolean alreadyLogin = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyt_login);
		
		loginProxy = new LLoginProxy();
		
		registerView = (TextView)findViewById(R.id.view_register);
		userNameView = (EditText)findViewById(R.id.view_username);
		userNameView.setFocusableInTouchMode(true);
		userNameView.setFocusable(true);
		passwordView = (EditText)findViewById(R.id.view_password);
		loginView    = (TextView)findViewById(R.id.view_login);
		forgetPasswordView = (TextView)findViewById(R.id.view_forget_password);
		
		hideKeyBoard(userNameView);
		hideKeyBoard(passwordView);
		registerListeners();
		

		
		if(JUMP_LOGIN)
		{
			LLoginInfo info = new LLoginInfo(getString(R.string.str_test_username),getString(R.string.str_test_password));
			LSaver saver = new LSaver(this);
			saver.saveLoginInfo(info);
			onLoginSuccess();
		}
		else
		{
			LSaver saver = new  LSaver(this);
			LLoginInfo info = saver.readLoginInfo();
			if(null != info)
			{
				userNameView.setText(info.getUsername());
				passwordView.setText(info.getPassword());
				login(info.getUsername(),info.getPassword());
			}
		}
	}
	
	private void login(String username,String password)
	{
		progressDialog = ProgressDialog.show(this, "", getString(R.string.str_logining), true,false);
		LLoginInfo info = new LLoginInfo(username,password);
		loginProxy.login(info,getApplicationContext(),new OnFinishListener() {
			
			@Override
			public void onFinish(final boolean successful, final String errorInfo) {

				
				runOnUiThread(new Runnable()
				{

					@Override
					public void run() {
						if(null != progressDialog)
						{
							progressDialog.dismiss();
							progressDialog = null;
						}
						
						if(successful)
						{
							onLoginSuccess();
						}
						else
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
							builder.setTitle(R.string.str_fail_to_login);
							if(!"".equals(errorInfo))
							{
								builder.setMessage(errorInfo);
							}
							builder.setPositiveButton(R.string.str_button_close,null);
							builder.show();
						}
						
					}
					
				});
			
				
			}
		});
		
	}
	
	private void onLoginSuccess()
	{
		LAppManager.onLogin(getApplicationContext());
		startActivity(new Intent(this,HomeActivity.class));
		finish();
	}
	
	private void onRegisterSuccess()
	{
		LAppManager.onLogin(getApplicationContext());
		startActivity(new Intent(this,HomeActivity.class));
		finish();
	}
	
    private void register()
    {
    	String username = userNameView.getText().toString();
		String password = passwordView.getText().toString();
		
		if(TextUtils.isEmpty(username))
		{
			ToastUtil.show(this, "请输入用户名");
			return;
		}
		
		if(TextUtils.isEmpty(password))
		{
			ToastUtil.show(this, "请输入密码");
			return;
		}
		progressDialog = ProgressDialog.show(this, "", getString(R.string.str_registering), true,false);
		LRegisterInfo info = new LRegisterInfo(username,password);
		loginProxy.register(info, getApplicationContext(),new OnFinishListener()
		{

			@Override
			public void onFinish(final boolean successful, final String errorInfo) {
				
				runOnUiThread(new Runnable()
				{

					@Override
					public void run() {
						if(null != progressDialog)
						{
							progressDialog.dismiss();
							progressDialog = null;
						}
						
						if(successful)
						{
							onRegisterSuccess();
						}
						else
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
							builder.setTitle(R.string.str_fail_to_register);
							if(!"".equals(errorInfo))
							{
								builder.setMessage(errorInfo);
							}
							builder.setPositiveButton(R.string.str_button_close,null);
							builder.show();
						}
						
					}
					
				});
				
			}
			
		});
		
		
    }
    
    private void registerListeners()
	{
		registerView.setOnClickListener(this);
		loginView.setOnClickListener(this);
		forgetPasswordView.setOnClickListener(this);
	}
    
    private void onRegisterMode()
    {
    	View v = findViewById(R.id.group_login);
    	Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_close_open_self);
    	v.startAnimation(anim);
    	
    	login_action = false;
		userNameView.setText("");
		userNameView.requestFocus();
		passwordView.setText("");
		loginView.setText(R.string.str_register);
		registerView.setText(R.string.str_login);
		forgetPasswordView.setVisibility(View.GONE);
    }
    
    private void onLoginMode()
    {
    	login_action = true;
    	
    	View v = findViewById(R.id.group_login);
    	Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_close_open_self);
    	v.startAnimation(anim);
    	
		userNameView.setText("");
		userNameView.requestFocus();
		passwordView.setText("");
		loginView.setText(R.string.str_login);
		registerView.setText(R.string.str_register);
		forgetPasswordView.setVisibility(View.VISIBLE);
    }
    
	@Override
	public void onClick(View v) {
		if(registerView == v)
		{
			if(login_action)
			{
				onRegisterMode();
			}
			else
			{
				onLoginMode();
			}
			
		}
		else if(loginView == v)
		{
			if(login_action)
			{
				String username = userNameView.getText().toString();
				String password = passwordView.getText().toString();
				
				if(TextUtils.isEmpty(username))
				{
					ToastUtil.show(this, "请输入用户名");
					return;
				}
				
				if(TextUtils.isEmpty(password))
				{
					ToastUtil.show(this, "请输入密码");
					return;
				}
				login(username,password);
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
