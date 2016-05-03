package com.example.wzelectricitysupplier.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.wzelectricitysupplier.R;


public class LoginDialog extends DialogFragmentBase {
	
	public final static String TAG = "LoginDialog";
	
	private static LoginDialog mDialog;
	
	public static LoginDialog newInstance(){
		if(mDialog == null){
			mDialog = new LoginDialog();
		}
		return mDialog;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
		setStyle(STYLE_NO_TITLE, 0);
	}
	
	EditText count;
	EditText pwd;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_login, null);
		
		count = (EditText) view.findViewById(R.id.count);
		pwd = (EditText) view.findViewById(R.id.pwd);
	
		ImageButton loginBtn = (ImageButton) view.findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.copyFrom(getDialog().getWindow().getAttributes());
		params.width = metric.widthPixels;
		params.height = metric.heightPixels;
	    getDialog().getWindow().setAttributes(params);
		return view;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		Log.d(TAG, "onCancel is called");    
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		mDialog = null;
		Log.d(TAG, "onDismiss is called");    
	}
	
}
