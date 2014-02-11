package com.codepath.apps.simpletwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletwitterapp.SimpleTwitterApp;
import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

@SuppressLint("ValidFragment")
public class UserTimeLineFragment extends TwitterTimeLineFragment {

	public static final String TAG = UserTimeLineFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		showProgressBar();
		SimpleTwitterApp.getRestClient().getUserTimeLine(
				getArguments().getString("screen_name"),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jo) {
						ArrayList<Tweet> tweets = Tweet.fromJson(jo);
						getAdapter().addAll(tweets);
						hideProgressBar();

					}

					public void onFailure(Throwable e) {
						Log.d("DEBUG", "Fetch timeline error: " + e.toString());
						hideProgressBar();
					}
				});

	}

	public static UserTimeLineFragment newInstance(String screenName) {
		UserTimeLineFragment userTimeLineFragment = new UserTimeLineFragment();
		Bundle args = new Bundle();
		args.putString("screen_name", screenName);
		userTimeLineFragment.setArguments(args);
		return userTimeLineFragment;
	}

}
