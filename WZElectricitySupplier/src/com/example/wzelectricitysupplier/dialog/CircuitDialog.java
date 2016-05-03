package com.example.wzelectricitysupplier.dialog;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.wzelectricitysupplier.MainActivity;
import com.example.wzelectricitysupplier.R;
import com.example.wzelectricitysupplier.setting.Util;

public class CircuitDialog extends FullScreenDialog {

	public final static String TAG = "CircuitDialog";
	
	private EditText mRemark;
	private EditText mNameEdit;
	private ImageButton mSaveBtn;
	
	public CircuitDialog(MainActivity mMainActivity) {
		super(mMainActivity, R.layout.dialog_circuit);
		Util.HideKeyboard(mMainActivity, mView);
		Util.ControlKeyboardLayout(mView, mSaveBtn);
	}

	@Override
	protected void initBasicElements() {
		mNameEdit = (EditText) mView.findViewById(R.id.circuitname);
		mRemark = (EditText) mView.findViewById(R.id.circuitremark);
		mSaveBtn = (ImageButton) mView.findViewById(R.id.saveBtn);
		ImageButton mCancelBtn = (ImageButton) mView.findViewById(R.id.cancelBtn);
		mCancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Dismiss();
			}
		});
	}

	public void setOnSaveButtonListener(OnClickListener onClickListener) {
		mSaveBtn.setOnClickListener(onClickListener);
	}

	public void setRemark(String remark) {
		mRemark.setText(remark);
	}

	public void setName(String name) {
		mNameEdit.setText(name);
		mNameEdit.setSelection(name.length());
	}

	public String getRemark() {
		return mRemark.getText().toString();
	}

	public String getName() {
		return mNameEdit.getText().toString();
	}

	public boolean isRemarkStandard(){
		String name  = mRemark.getText().toString();
		if(name.trim().equals(""))
			return false;
		return true;
	}
}
