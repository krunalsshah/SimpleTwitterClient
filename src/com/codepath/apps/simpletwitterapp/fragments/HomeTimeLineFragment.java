package com.codepath.apps.simpletwitterapp.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletwitterapp.EndlessScrollListener;
import com.codepath.apps.simpletwitterapp.SimpleTwitterApp;
import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimeLineFragment extends TwitterTimeLineFragment {
	public static final String TAG = HomeTimeLineFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SimpleTwitterApp.getRestClient().getHomeTimeLineTweets(-1, -1,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						tweets = Tweet.fromJson(jsonTweets);
						getAdapter().addAll(tweets);
					}
					public void onFailure(Throwable e) {
						Log.d("DEBUG", "Fetch timeline error: " + e.toString());
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list contents
				// Make sure you call listView.onRefreshComplete()
				// once the loading is done. This can be done from here or any
				// place such as when the network request has completed
				// successfully.
				fetchTimelineAsync(-1, adapter.getItem(0).getId() + 1);
				lvTweets.onRefreshComplete();
			}
		});

		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				if (adapter.getCount() > 0) {
					fetchTimelineAsync(adapter.getItem(adapter.getCount() - 1)
							.getId() - 1, -1);
				}
			}
		});
	}

	public void fetchTimelineAsync(final long maxId, final long sinceId) {
		SimpleTwitterApp.getRestClient().getHomeTimeLineTweets(maxId, sinceId,
				new JsonHttpResponseHandler() {
					public void onSuccess(JSONArray jsonTweets) {
						// ...the data has come back, finish populating
						// listview...
						// Now we call onRefreshComplete to signify refresh has
						// finished
						tweets = Tweet.fromJson(jsonTweets);
						if (maxId > 0) {
							adapter.addAll(tweets);
						}
						if (sinceId > 0) {
							for (int i = 0; i < tweets.size(); i++) {
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

}
