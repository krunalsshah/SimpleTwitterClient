package com.codepath.apps.simpletwitterapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitterapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		populateProfileHeader();
	}

	private void populateProfileHeader() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		String screenName = getIntent().getStringExtra("screenName");
		String userProfileImageUrl = getIntent().getStringExtra(
				"userProfileImageUrl");
		int followers = getIntent().getIntExtra("followers",0);
		String name = getIntent().getStringExtra("name");
		String tag = getIntent().getStringExtra("tag");
		int following = getIntent().getIntExtra("following",0);
		getActionBar().setTitle("@" + screenName);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.iv_profile_image);
		TextView tvProfileName = (TextView) findViewById(R.id.tv_profile_name);
		TextView tvProfileTag = (TextView) findViewById(R.id.tv_profile_tag);
		TextView tvFollower = (TextView) findViewById(R.id.tv_profile_follower);
		TextView tvFollowing = (TextView) findViewById(R.id.tv_profile_following);
		ImageLoader.getInstance().displayImage(userProfileImageUrl,
				ivProfileImage);
		tvProfileName.setText(name);
		tvFollower.setText(String.valueOf(followers) + " Followers");
		tvFollowing.setText(String.valueOf(following) + " Followers");
		tvProfileTag.setText(tag);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			setResult(RESULT_CANCELED, null);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
