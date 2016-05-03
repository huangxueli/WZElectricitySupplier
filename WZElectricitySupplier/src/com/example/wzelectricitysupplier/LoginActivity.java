package com.example.wzelectricitysupplier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ab.activity.AbActivity;
import com.example.wzelectricitysupplier.bean.LoginInfo;
import com.example.wzelectricitysupplier.function.Cache;
import com.example.wzelectricitysupplier.http.HttpCommunication;
import com.example.wzelectricitysupplier.setting.Constants;
import com.example.wzelectricitysupplier.setting.Util;

public class LoginActivity extends AbActivity implements Callback{
	
	public static final String TAG = "LoginActivity";
	public static final int REQUESTCODE = 1;
	
	private Handler mHandler;
	private Cache mCache;
	
	private EditText countEd;
	private EditText passwordEd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Util.FullScreen(this);
		
		setAbContentView(R.layout.activity_login);
		MyApplication.Context = getApplicationContext();
		MyApplication.Inflater = mInflater;
		MyApplication.Resources = getResources();
		
		mHandler = new Handler(this);
		SharedPreferences sp = this.getSharedPreferences(Cache.SharedPreferencesFile, 0);
		mCache = new Cache(sp);
		
		String[] logininfo = mCache.readAll();
		countEd = (EditText)findViewById(R.id.count);
		countEd.setText(logininfo[0]);
		countEd.setSelection(logininfo[0].length());
		passwordEd = (EditText)findViewById(R.id.pwd);
		passwordEd.setText(logininfo[1]);
		passwordEd.setSelection(logininfo[1].length());
		
		ImageButton loginBtn = (ImageButton)findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String count = countEd.getText().toString();
				String password = passwordEd.getText().toString();
				Constants.Account = count;
				Constants.Password = password;
				new HttpCommunication(LoginActivity.this, mHandler).doLogin(count);;
			}
		});
		
		mAbTitleBar.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch(msg.what){
		case Constants.MESSAGE_WHAT_LOGIN_S:
			LoginInfo info = (LoginInfo)msg.obj;
			int testplugin = Integer.valueOf(info.TestPlugin).intValue();
			String binaryplugin = Integer.toBinaryString(testplugin);
			// һ����λ��,ת�������л��ǰ���0ʡ�� ���һλ��û��, ǰ��λ�ֱ����ͬ��Ȩ��
			while(binaryplugin.length()<6){
				binaryplugin = "0" + binaryplugin;
			}
			// �ж�ǰ��λ�� 0��ASCII����48 1��ASCII��49
			if(binaryplugin.charAt(0)==49)
				Constants.CompleteProjectRight = true;
			if(binaryplugin.charAt(1)==49)
				Constants.NewProjectRight = true;
			if(binaryplugin.charAt(2)==49)
				Constants.NewUntiRight = true;
			
			mCache.write(Constants.Account, Constants.Password);
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;
		case Constants.MESSAGE_WHAT_LOGIN_F:
			Util.Toast("�˺Ż��������");
			break;
		}
		return false;
	}
	@Override
	protected void onDestroy() {
		Log.e(TAG, "onDestroy");
		super.onDestroy();
	}
	
}
