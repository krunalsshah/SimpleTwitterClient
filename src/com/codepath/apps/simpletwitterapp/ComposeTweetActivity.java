package com.codepath.apps.simpletwitterapp;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeTweetActivity extends Activity {
	private ClearableEditText cetTweetMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		initializeView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}

	private void initializeView() {
		cetTweetMessage = (ClearableEditText) findViewById(R.id.cetTweetMessage);
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
		SimpleTwitterApp.getRestClient().postTweet(tweetText,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonTweet) {
						Intent i = new Intent();
						i.putExtra("jsonTweet", jsonTweet.toString());
						setResult(RESULT_OK, i);
						finish();
					}
				});
	}

	public void onClickCancelTweet(MenuItem mi) {
		setResult(RESULT_CANCELED, null);
		finish();
	}

}
