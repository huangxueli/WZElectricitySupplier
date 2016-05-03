package com.example.wzelectricitysupplier.customwidget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class EditableSpinner extends LinearLayout {

	private AutoCompleteTextView _text;
	private ImageButton _button;

	public EditableSpinner(Context context) {
		super(context);
		this.createChildControls(context);
	}

	public EditableSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.createChildControls(context);
	}

	private void createChildControls(Context context) {
		this.setOrientation(HORIZONTAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		_text = new AutoCompleteTextView(context);
		_text.setSingleLine();
		_text.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_NORMAL
				| InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
				| InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
				| InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		_text.setRawInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		this.addView(_text, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 1));

		_button = new ImageButton(context);
		_button.setImageResource(android.R.drawable.arrow_down_float);
		_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_text.showDropDown();
			}
		});
		this.addView(_button, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}

	/**
	 * Sets the source for DDLB suggestions. Cursor MUST be managed by
	 * supplier!!
	 * 
	 * @param source
	 *            Source of suggestions.
	 * @param column
	 *            Which column from source to show.
	 */
	public void setSuggestionSource(String[] column) {
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
				this.getContext(), android.R.layout.simple_dropdown_item_1line, column);
		_text.setAdapter(mAdapter);
	}

	/**
	 * Gets the text in the combo box.
	 * 
	 * @return Text.
	 */
	public String getText() {
		return _text.getText().toString();
	}

	/**
	 * Sets the text in combo box.
	 */
	public void setText(String text) {
		_text.setText(text);
	}
}
