package com.codepath.apps.simpletwitterapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletwitterapp.R;
import com.codepath.apps.simpletwitterapp.TweetsAdapter;
import com.codepath.apps.simpletwitterapp.models.Tweet;

import eu.erikw.PullToRefreshListView;


public class TwitterTimeLineFragment extends Fragment {
	static TweetsAdapter adapter;
	PullToRefreshListView lvTweets;
	ArrayList<Tweet>  tweets;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tweet_list, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);		
		tweets = new ArrayList<Tweet>();
		adapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(adapter);
		adapter.clear();
	}
	
	public static TweetsAdapter getAdapter() {
		return adapter;
	}
}
