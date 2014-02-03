package com.codepath.apps.simpletwitterapp;

import java.util.ArrayList;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetTimeLineActivity extends Activity {
	public static final String TAG = TweetTimeLineActivity.class.getCanonicalName();
	public static final int REQUEST_CODE = R.layout.activity_tweet_time_line;
	private static TweetsAdapter adapter;
	PullToRefreshListView lvTweets;
	static ArrayList<Tweet>  tweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_time_line);
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		SimpleTwitterApp.getRestClient().getHomeTimeLineTweets(-1, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				super.onSuccess(jsonTweets);
				tweets = Tweet.fromJson(jsonTweets);
				adapter = new TweetsAdapter(getBaseContext(), tweets);
				adapter.sort(new Comparator<Tweet>() {
					@Override
					public int compare(Tweet lhs, Tweet rhs) {
						
						return (rhs.getId() > lhs.getId())?1 : -1;
					}
        			
				});
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
                fetchTimelineAsync(-1);
                lvTweets.onRefreshComplete();
            }
        });
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
	        	fetchTimelineAsync(adapter.getItem(adapter.getCount()-1).getId());	                // or customLoadMoreDataFromApi(totalItemsCount); 
	        }
	        });
	}
	
	public void fetchTimelineAsync(final long page) {
		SimpleTwitterApp.getRestClient().getHomeTimeLineTweets(page,
				new JsonHttpResponseHandler() {
					public void onSuccess(JSONArray jsonTweets) {
						// ...the data has come back, finish populating
						// listview...
						// Now we call onRefreshComplete to signify refresh has
						// finished
						super.onSuccess(jsonTweets);
						// fromJson method call guarantee's only unique tweets
						// are returned else empty array is returned
						tweets.retainAll(Tweet.fromJson(jsonTweets));
						adapter.addAll(tweets);
						adapter.sort(new Comparator<Tweet>() {
							@Override
							public int compare(Tweet lhs, Tweet rhs) {

								return (rhs.getId() > lhs.getId()) ? 1 : -1;
							}

						});
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
