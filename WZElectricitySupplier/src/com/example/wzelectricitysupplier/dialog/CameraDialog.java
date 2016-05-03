package com.example.wzelectricitysupplier.dialog;

import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;

public class CameraDialog extends FullScreenDialog{
	
	public CameraDialog(MainActivity mMainActivity) {
		super(mMainActivity, R.layout.dialog_camara_type);
	}
	
	private ImageButton mNormalBtn;
	private ImageButton mDefectBtn;
	@Override
	protected void initBasicElements() {
		mNormalBtn = (ImageButton)mView.findViewById(R.id.cameraNormal);
		mDefectBtn = (ImageButton)mView.findViewById(R.id.cameraDefect);
	}
	
	public void setOnNormalButtonListener(OnClickListener listener) {
		mNormalBtn.setOnClickListener(listener);
	}
	public void setOnDefectButtonListener(OnClickListener listener) {
		mDefectBtn.setOnClickListener(listener);
	}

}
