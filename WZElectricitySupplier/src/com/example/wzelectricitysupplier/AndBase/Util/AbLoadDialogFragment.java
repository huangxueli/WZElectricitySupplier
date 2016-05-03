package com.example.wzelectricitysupplier.AndBase.Util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * © 2012 amsoft.cn
 * 名称：AbLoadDialogFragment.java 
 * 描述：弹出加载框
 *
 * @author 还如�?���?
 * @version v1.0
 * @date�?014-07-30 下午16:00:52
 */
public class AbLoadDialogFragment extends AbDialogFragment {

	private int mTheme;
	private int mStyle;
	private int mIndeterminateDrawable;
	private int mTextSize = 15;
	private int mTextColor = Color.WHITE;
	private View mContentView;
	private TextView mTextView = null;
	private ImageView mImageView = null;
	private int mBackgroundColor = Color.parseColor("#88838B8B");

	/**
	 * Create a new instance of AbDialogFragment, providing "style" as an
	 * argument.
	 */
	public static AbLoadDialogFragment newInstance(int style, int theme) {
		AbLoadDialogFragment f = new AbLoadDialogFragment();
		// Supply style input as an argument.
		Bundle args = new Bundle();
		args.putInt("style", style);
		args.putInt("theme", theme);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStyle = getArguments().getInt("style");
		mTheme = getArguments().getInt("theme");
		setStyle(mStyle, mTheme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout parent = new LinearLayout(this.getActivity());
		parent.setBackgroundColor(mBackgroundColor);
		parent.setGravity(Gravity.CENTER);
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setPadding(20, 20, 20, 20);
		parent.setMinimumWidth(scale(this.getActivity(), 400));
		
		mImageView = new ImageView(this.getActivity());
		mImageView.setImageResource(mIndeterminateDrawable);
		mImageView.setScaleType(ScaleType.MATRIX);

		mTextView = new TextView(this.getActivity());
		mTextView.setText(mMessage);
		mTextView.setTextColor(mTextColor);
		mTextView.setTextSize(mTextSize);
		mTextView.setPadding(5, 5, 5, 5);

		parent.addView(mImageView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		parent.addView(mTextView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					// 执行刷新
					load(v);
			}

		});
		
		// 执行加载
		load(mImageView);
		mContentView = parent;

		return mContentView;
	}

	public View getContentView() {
		return mContentView;
	}

	public int getTextSize() {
		return mTextSize;
	}

	public void setTextSize(int textSize) {
		this.mTextSize = textSize;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int textColor) {
		this.mTextColor = textColor;
	}
	
	@Override
	public void setMessage(String message) {
		this.mMessage = message;
		if(mTextView!=null){
			mTextView.setText(mMessage);
		}
	}

	public int getIndeterminateDrawable() {
		return mIndeterminateDrawable;
	}

	public void setIndeterminateDrawable(int indeterminateDrawable) {
		this.mIndeterminateDrawable = indeterminateDrawable;
	}

	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.mBackgroundColor = backgroundColor;
	}
	
	
	private int scale(Context context, float value) {
		Resources mResources;
		if (context == null){
			mResources = Resources.getSystem();
		}else{
			mResources = context.getResources();
		}
		DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
		return scale(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, value);
	}
	
	private int scale(int displayWidth, int displayHeight, float pxValue) {
		if(pxValue == 0 ){
			return 0;
		}
		float scale = 1;
		try {
			float scaleWidth = (float) displayWidth / 720;
			float scaleHeight = (float) displayHeight / 1080;
			scale = Math.min(scaleWidth, scaleHeight);
		} catch (Exception e) {
		}
		return Math.round(pxValue * scale + 0.5f);
	}
	
}