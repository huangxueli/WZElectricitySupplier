package com.example.wzelectricitysupplier.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

public class PromptDialog extends DialogFragmentBase {
	
	private static PromptDialog mDialog;
	private OnPositiveClickListener listener;
	private String text;
	private boolean avaliable;
	
	public static PromptDialog newInstance() {
		if(mDialog==null){
			mDialog = new PromptDialog();
		}
		return mDialog;
	}
	
	public static abstract class OnPositiveClickListener{
		public abstract void doPositiveAction();
	} 
	
	public void setOnPositiveClickListener(OnPositiveClickListener listener){
		this.listener = listener;
	}
	
	public void setMessage(String text){
		this.text = text;
	}
	public void setNegativeButtonAvaliable(boolean avaliable){
		this.avaliable = avaliable;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
		setStyle(STYLE_NORMAL, 0); // 0表示系统默认的样式
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("提示");
		builder.setMessage(text);
		if(avaliable){
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismiss();
				}
			});
		}
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(listener!=null){
					listener.doPositiveAction();
				}
				dismiss();
			}
		});
		return builder.create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		Log.d("ProgressDialog", "onCancel is called");
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		mDialog = null;
		Log.d("ProgressDialog", "onDismiss is called");
	}
}
