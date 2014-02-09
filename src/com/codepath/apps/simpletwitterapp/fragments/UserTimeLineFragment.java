package com.codepath.apps.simpletwitterapp.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletwitterapp.SimpleTwitterApp;
import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimeLineFragment extends TwitterTimeLineFragment {

	public static final String TAG = UserTimeLineFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SimpleTwitterApp.getRestClient().getUserTimeLine(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jo) {
						tweets = Tweet.fromJson(jo);
						getAdapter().addAll(tweets);
					}

					public void onFailure(Throwable e) {
						Log.d("DEBUG", "Fetch timeline error: " + e.toString());
					}
				});
	}

}
