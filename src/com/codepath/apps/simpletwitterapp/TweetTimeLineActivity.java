package com.codepath.apps.simpletwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.codepath.apps.simpletwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TweetTimeLineActivity extends Activity {
	public static final String TAG = TweetTimeLineActivity.class.getCanonicalName();
	public static final int REQUEST_CODE = R.layout.activity_tweet_time_line;
	private TweetsAdapter adapter;
	PullToRefreshListView lvTweets;
	ArrayList<Tweet>  tweets;
	User authUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_time_line);
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		SimpleTwitterApp.getRestClient().getHomeTimeLineTweets(-1, -1, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				tweets = Tweet.fromJson(jsonTweets);
				adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
			}
		});
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
                fetchTimelineAsync(-1, adapter.getItem(0).getId() + 1);
                lvTweets.onRefreshComplete();
            }
        });
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
	        	fetchTimelineAsync(adapter.getItem(adapter.getCount()-1).getId() - 1, -1);
	        }
	        });
		loadMyProfileInfo();
	}
	
	public void loadMyProfileInfo() {
		SimpleTwitterApp.getRestClient().getUserAccountSetting(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject json) {
						authUser = User.fromJson(json);
						getActionBar().setTitle("@"+authUser.getScreenName());
						SimpleTwitterApp.getRestClient().getUserProfile(authUser.getScreenName(), new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject json) {
								authUser = User.fromJson(json);
							}
						});
					}
					
				});
	}
	
	public void fetchTimelineAsync(final long maxId, final long sinceId) {
		SimpleTwitterApp.getRestClient().getHomeTimeLineTweets(maxId,sinceId,
				new JsonHttpResponseHandler() {
					public void onSuccess(JSONArray jsonTweets) {
						// ...the data has come back, finish populating
						// listview...
						// Now we call onRefreshComplete to signify refresh has
						// finished
						tweets = Tweet.fromJson(jsonTweets);
						if(maxId > 0){
							adapter.addAll(tweets);
						} 
						if(sinceId > 0){
							for(int i=0 ; i < tweets.size(); i++){
								adapter.insert(tweets.get(i), i);
							}
						}
						adapter.notifyDataSetChanged();

					}

					public void onFailure(Throwable e) {
						Log.d("DEBUG", "Fetch timeline error: " + e.toString());
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_time_line, menu);
		return true;
	}
	
	public void onClickCompose(MenuItem mi){
		 Intent i = new Intent(this, ComposeTweetActivity.class);
		 i.putExtra("screenName", authUser.getScreenName());
		 i.putExtra("userProfileImageUrl", authUser.getProfileImageUrl());
    	 startActivityForResult(i, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		if (resultCode == RESULT_OK) {
			JSONObject jsonTweet;
			try {
				jsonTweet = new JSONObject(i.getExtras().getString("jsonTweet"));
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}

			adapter.insert(Tweet.fromJson(jsonTweet), 0);
		}
	}
}
