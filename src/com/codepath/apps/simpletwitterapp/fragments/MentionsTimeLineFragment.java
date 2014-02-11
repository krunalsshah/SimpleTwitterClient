package com.codepath.apps.simpletwitterapp.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletwitterapp.SimpleTwitterApp;
import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimeLineFragment extends TwitterTimeLineFragment {

	public static final String TAG = MentionsTimeLineFragment.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		showProgressBar();
		SimpleTwitterApp.getRestClient().getMentions(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						tweets = Tweet.fromJson(jsonTweets);
						getAdapter().addAll(tweets);
						hideProgressBar();
					}

					public void onFailure(Throwable e) {
						Log.d("DEBUG", "Fetch timeline error: " + e.toString());
						hideProgressBar();
					}
				});
	}

}
