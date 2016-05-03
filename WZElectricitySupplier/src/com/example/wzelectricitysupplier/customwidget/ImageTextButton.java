package com.example.wzelectricitysupplier.customwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wzelectricitysupplier.R;

public class ImageTextButton extends LinearLayout {

	public interface OnPressListener {
		public abstract void onPressDown();

		public abstract void onPressUp();
	}

	private ImageView mImageView;
	private TextView mTextView;
	private ImageView mSelectImage;
	private boolean mIsPressed = false;

	private OnPressListener mOnPressListener = null;

	public ImageTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mImageView = new ImageView(context, attrs);
		mImageView.setPadding(0, 0, 0, 0);
		mTextView = new TextView(context, attrs);
		mTextView.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
		mTextView.setPadding(0, 0, 0, 0);
		mSelectImage = new ImageView(context, attrs);
		mSelectImage.setPadding(0, 0, 0, 0);
		pressUp();
		setClickable(true);
		setFocusable(true);
		addView(mImageView);
		addView(mTextView);
		addView(mSelectImage);
		mSelectImage.setVisibility(View.GONE);
		mSelectImage.setImageResource(R.drawable.image_button_selected);
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mIsPressed) {
					pressUp();
				} else {
					pressDown();
				}
			}

		});

	}

	public void setOnPressListener(OnPressListener onPressListener) {
		mOnPressListener = onPressListener;
	}

	public void pressDown() {
		mIsPressed = true;
		setBackgroundResource(R.drawable.button_selector_textimagebutton_selected);
		if (mOnPressListener != null) {
			mOnPressListener.onPressDown();
		}
	}

	public void pressUp() {
		mIsPressed = false;
		setBackgroundResource(R.drawable.button_selector_textimagebutton);
		if (mOnPressListener != null) {
			mOnPressListener.onPressUp();
		}
	}

	public boolean isPressed() {
		return mIsPressed;
	}

	public void showSelect(boolean show) {
		if (show) {
			mSelectImage.setVisibility(View.VISIBLE);
		} else {
			mSelectImage.setVisibility(View.GONE);
		}
		// this.setBackground(background);
	}

}
