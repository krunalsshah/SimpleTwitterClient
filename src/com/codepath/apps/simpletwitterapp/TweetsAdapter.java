package com.codepath.apps.simpletwitterapp;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitterapp.activity.ProfileActivity;
import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.codepath.apps.simpletwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, R.layout.tweet_item, tweets);
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.tweet_item, null, false);
		}
		final Tweet tweet = getItem(position);

		ImageView profile = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(
				tweet.getUser().getProfileImageUrl(), profile);
		profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleTwitterApp.getRestClient().getSpecifiedUserProfile(tweet.getUser().getScreenName(),
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject jo) {
								User authUser = User.fromJson(jo);
								Intent i = new Intent(getContext(), ProfileActivity.class);
								Bundle bundle = new Bundle();
								//Can send serializable object
								bundle.putString("screenName", authUser.getScreenName());
								bundle.putString("userProfileImageUrl", authUser.getProfileImageUrl());
								bundle.putString("name", authUser.getName());
								bundle.putString("tag", authUser.getDescription());
								bundle.putInt("followers", authUser.getFollowersCount());
								bundle.putInt("following", authUser.getFriendsCount());
								i.putExtras(bundle);
								getContext().startActivity(i);
							}
						});
			}
		});

		TextView name = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUser().getName() + "</b>"
				+ "<small> <font color = '#777777'>@"
				+ tweet.getUser().getScreenName() + "</font></small>";
		name.setText(Html.fromHtml(formattedName));
				
		TextView body = (TextView) view.findViewById(R.id.tvBody);
		body.setText(Html.fromHtml(tweet.getBody()));
		return view;
	}
}
