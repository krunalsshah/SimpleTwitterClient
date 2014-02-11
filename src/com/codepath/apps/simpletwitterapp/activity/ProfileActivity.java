package com.codepath.apps.simpletwitterapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitterapp.R;
import com.codepath.apps.simpletwitterapp.fragments.UserTimeLineFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	UserTimeLineFragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		Bundle extras = getIntent().getExtras();
		populateUserProfile(extras);
		populateActionBar(extras.getString("screenName"));
	}

	@SuppressLint("NewApi")
	private void populateActionBar(String screenName) {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("@" + screenName);
		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.ab_solid_example));
		getActionBar()
				.setStackedBackgroundDrawable(
						getResources().getDrawable(
								R.drawable.ab_stacked_solid_example));

		addFragmentToView(screenName);
	}

	private void populateUserProfile(Bundle bundle) {
		ImageView ivProfileImage = (ImageView) findViewById(R.id.iv_profile_image);
		TextView tvProfileName = (TextView) findViewById(R.id.tv_profile_name);
		TextView tvProfileTag = (TextView) findViewById(R.id.tv_profile_tag);
		TextView tvFollower = (TextView) findViewById(R.id.tv_profile_follower);
		TextView tvFollowing = (TextView) findViewById(R.id.tv_profile_following);
		ImageLoader.getInstance().displayImage(
				bundle.getString("userProfileImageUrl"), ivProfileImage);
		tvProfileName.setText(bundle.getString("name"));
		tvFollower.setText(String.valueOf(bundle.getInt("followers")
				+ " Followers"));
		tvFollowing.setText(String.valueOf(bundle.getInt("following")
				+ " Following"));
		tvProfileTag.setText(bundle.getString("tag"));
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

	public void addFragmentToView(String screenName) {
		FragmentManager fm = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = fm.beginTransaction();
		if(mFragment == null){
			fts.add(R.id.fragment_container,
					UserTimeLineFragment.newInstance(screenName));

		}else{
			fts.attach(mFragment);
		}
		fts.commit();
	}

}
