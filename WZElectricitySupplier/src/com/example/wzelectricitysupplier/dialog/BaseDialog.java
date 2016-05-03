package com.example.wzelectricitysupplier.dialog;

import android.view.View;

import com.ab.global.AbConstant;
import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.MyApplication;

public class BaseDialog {
	
	public static int TYPE_BOTTOM = AbConstant.DIALOGBOTTOM;
	public static int TYPE_CENTER = AbConstant.DIALOGCENTER;
	
	protected MainActivity mMainActivity;
	protected View mView;
	protected int mDialogType = TYPE_BOTTOM; // д╛хо╬соботй╬
	
	public BaseDialog(MainActivity activity, int resource) {
		mMainActivity = activity;
		mView = MyApplication.Inflater.inflate(resource, null);
		initBasicElements();
	}
	
	protected void initBasicElements(){}
	
	public void ShowDialog(int type) {
		mDialogType = type;
		mMainActivity.showDialog(type, mView);
	}

	public void Dismiss(int type) {
		mDialogType = type;
		mMainActivity.removeDialogInThread(type);
	}
	
	public void ShowDialog() {
		ShowDialog(mDialogType);
	}
	
	public void Dismiss() {
		Dismiss(mDialogType);
	}
	
	public int getDialogType() {
		return mDialogType;
	}

	public void setDialogType(int mDialogType) {
		this.mDialogType = mDialogType;
	}
	
}
