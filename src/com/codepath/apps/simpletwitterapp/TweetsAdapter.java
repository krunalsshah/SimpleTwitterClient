package com.codepath.apps.simpletwitterapp;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, R.layout.tweet_item, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.tweet_item, null, false);
		}
		Tweet tweet = getItem(position);

		ImageView profile = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(
				tweet.getUser().getProfileImageUrl(), profile);

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
