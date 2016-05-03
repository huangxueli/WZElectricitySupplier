package com.example.wzelectricitysupplier.dialog;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;

public class CircurtManageDialog extends FullScreenDialog {
	
	private ImageButton mExportBtn;
	private ImageButton mDeleteBtn;
	private ImageButton mEditBtn;
	private ImageButton mLoadBtn;

	public CircurtManageDialog(MainActivity activity) {
		super(activity, R.layout.dialog_circuit_manage);
	}
	
	@Override
	protected void initBasicElements() {
		mExportBtn = (ImageButton) mView.findViewById(R.id.exportBtn);
		mDeleteBtn = (ImageButton) mView.findViewById(R.id.deleteBtn);
		mEditBtn = (ImageButton) mView.findViewById(R.id.editBtn);
		mLoadBtn = (ImageButton) mView.findViewById(R.id.loadBtn);
		ImageButton mCancelBtn = (ImageButton) mView.findViewById(R.id.cancelBtn);
		mCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Dismiss();
			}
		});
	}
	
	public void setOnExportButtonListener(OnClickListener onClickListener) {
		mExportBtn.setOnClickListener(onClickListener);
	}

	public void setOnDeleteButtonListener(OnClickListener onClickListener) {
		mDeleteBtn.setOnClickListener(onClickListener);
	}

	public void setOnEditButtonListener(OnClickListener onClickListener) {
		mEditBtn.setOnClickListener(onClickListener);
	}

	public void setOnLoadButtonListener(OnClickListener onClickListener) {
		mLoadBtn.setOnClickListener(onClickListener);
	}

}
