package com.codepath.apps.simpletwitterapp.activity;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletwitterapp.ClearableEditText;
import com.codepath.apps.simpletwitterapp.R;
import com.codepath.apps.simpletwitterapp.SimpleTwitterApp;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
//TODO refactor this activity to use fragment
public class ComposeTweetActivity extends FragmentActivity {
	private ClearableEditText cetTweetMessage;
	private TextView tvName ;
	private ImageView ivProfileImage;
	private ProgressBar pbCompose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		pbCompose = (ProgressBar) findViewById(R.id.pbCompose);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_solid_example));
		getActionBar().setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.ab_stacked_solid_example));
		String screenName = getIntent().getStringExtra("screenName");
		String userProfileImageUrl = getIntent().getStringExtra("userProfileImageUrl");
		initializeView();
		tvName.setText("@" +screenName);
		tvName.setTypeface(null, Typeface.BOLD);
		ImageLoader.getInstance().displayImage(userProfileImageUrl, ivProfileImage);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	setResult(RESULT_CANCELED, null);
			finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	private void initializeView() {
		cetTweetMessage = (ClearableEditText) findViewById(R.id.cetTweetMessage);
		tvName = (TextView) findViewById(R.id.tvScreenNameCompose);
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileCompose);
	}

	public void onClickSendTweet(MenuItem mi) {
		String tweetText = cetTweetMessage.getText().toString();
		if (tweetText == null || tweetText.isEmpty()) {
			Toast.makeText(getBaseContext(), "Message cannot be empty",
					Toast.LENGTH_LONG).show();
			return;
		} else if(tweetText != null && !tweetText.isEmpty() && tweetText.length() > ClearableEditText.MAX_TWEET_CHARS){
			Toast.makeText(getBaseContext(), "Message Size of Tweet Cannot exceed 140 characters.",
					Toast.LENGTH_LONG).show();
			return;
		}
		showProgressBar();
		SimpleTwitterApp.getRestClient().postTweet(tweetText,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonTweet) {
						Intent i = new Intent();
						i.putExtra("jsonTweet", jsonTweet.toString());
						setResult(RESULT_OK, i);
						finish();
						disableProgressBar();
					}
				});
	}
	
	public void showProgressBar(){
		pbCompose.setVisibility(ProgressBar.VISIBLE);
	}
	
	public void disableProgressBar(){
		pbCompose.setVisibility(ProgressBar.INVISIBLE);
	}

}
