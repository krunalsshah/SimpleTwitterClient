package com.codepath.apps.simpletwitterapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClearableEditText extends RelativeLayout {
	public static final int MAX_TWEET_CHARS = 140;
	LayoutInflater inflater;
	EditText edit_text;
	Button btn_clear;
	TextView tvCounter;

	public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
	}

	public ClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();

	}

	public ClearableEditText(Context context) {
		super(context);
		initViews();
	}

	void initViews() {
		inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.clearable_edit_text, this, true);
		edit_text = (EditText) findViewById(R.id.clearable_edit);
		btn_clear = (Button) findViewById(R.id.clearable_button_clear);
		btn_clear.setVisibility(RelativeLayout.INVISIBLE);
		tvCounter = (TextView)findViewById(R.id.tvCounter);
		tvCounter.setText(String.valueOf(MAX_TWEET_CHARS));
		tvCounter.setTextColor(Color.BLACK);
		clearText();
		showHideClearButton();
	}

	void clearText() {
		btn_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edit_text.setText("");
			}
		});
	}

	void showHideClearButton() {
		edit_text.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0)
					btn_clear.setVisibility(RelativeLayout.VISIBLE);
				else
					btn_clear.setVisibility(RelativeLayout.INVISIBLE);
				if(MAX_TWEET_CHARS - s.length() == -1){
					Toast.makeText(getContext(), "Message Size of Tweet exceeded 140 characters.", Toast.LENGTH_SHORT).show();
				}
				tvCounter.setText(String.valueOf(MAX_TWEET_CHARS - s.length()));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > MAX_TWEET_CHARS) {
					tvCounter.setTextColor(Color.RED);
					tvCounter.setTypeface(null,Typeface.BOLD);
				} else {
					tvCounter.setTextColor(Color.BLACK);
					tvCounter.setTypeface(null,Typeface.NORMAL);
				}

			}
		});
	}

	public Editable getText() {
		Editable text = edit_text.getText();
		return text;
	}
}
