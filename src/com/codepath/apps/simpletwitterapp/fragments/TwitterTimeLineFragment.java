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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		adapter = new TweetsAdapter(getActivity(), tweets);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tweet_list, container,false);
		lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);		
		lvTweets.setAdapter(adapter);
		adapter.clear();
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public static TweetsAdapter getAdapter() {
		return adapter;
	}
	
	// Should be called manually when an async task has started
    public void showProgressBar() {
        getActivity().setProgressBarIndeterminateVisibility(true); 
    }

    // Should be called when an async task has finished
    public void hideProgressBar() {
        getActivity().setProgressBarIndeterminateVisibility(false); 
    }
}
