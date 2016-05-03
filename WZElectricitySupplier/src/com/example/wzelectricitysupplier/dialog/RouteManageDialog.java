package com.example.wzelectricitysupplier.dialog;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;

public class RouteManageDialog extends FullScreenDialog {
	
	private ImageButton mDeleteBtn;
	private ImageButton mLoadBtn;

	public RouteManageDialog(MainActivity activity) {
		super(activity, R.layout.dialog_route_manage);
	}
	
	@Override
	protected void initBasicElements() {
		mDeleteBtn = (ImageButton) mView.findViewById(R.id.deleteBtn);
		mLoadBtn = (ImageButton) mView.findViewById(R.id.loadBtn);
		ImageButton mCancelBtn = (ImageButton) mView.findViewById(R.id.cancelBtn);
		mCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Dismiss();
			}
		});
	}
	
	public void setOnDeleteButtonListener(OnClickListener onClickListener) {
		mDeleteBtn.setOnClickListener(onClickListener);
	}

	public void setOnLoadButtonListener(OnClickListener onClickListener) {
		mLoadBtn.setOnClickListener(onClickListener);
	}

}
