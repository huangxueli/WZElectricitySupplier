package com.example.wzelectricitysupplier.fragment;

import android.widget.CompoundButton.OnCheckedChangeListener;

public class MenuItem {
	
	public String mText;
	public int mImgResID;
	public ButtonType mButtonType;
	public Boolean mIsChecked;
	public OnCheckedChangeListener mCheckedChangeListener = null;
	
	public enum ButtonType {
		NormalButton, SliderButton
	}

	public MenuItem(String text, int imgResID) {
		mText = text;
		mImgResID = imgResID;
		mButtonType = ButtonType.NormalButton;
	}
	
	public MenuItem(String text, int imgResID, ButtonType type, boolean isChecked, OnCheckedChangeListener checkListerner) {
		mText = text;
		mImgResID = imgResID;
		mButtonType = type;
		mIsChecked = isChecked;
		mCheckedChangeListener = checkListerner;
	}

}
