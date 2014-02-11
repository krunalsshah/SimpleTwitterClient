package com.codepath.apps.simpletwitterapp.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.codepath.apps.simpletwitterapp.R;
import com.codepath.apps.simpletwitterapp.SimpleTwitterApp;
import com.codepath.apps.simpletwitterapp.TweetsAdapter;
import com.codepath.apps.simpletwitterapp.fragments.FragmentTabListener;
import com.codepath.apps.simpletwitterapp.fragments.HomeTimeLineFragment;
import com.codepath.apps.simpletwitterapp.fragments.MentionsTimeLineFragment;
import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.codepath.apps.simpletwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TwitterMainActivity extends FragmentActivity {
	public static final String TAG = TwitterMainActivity.class
			.getCanonicalName();
	public static final int REQUEST_CODE = 9000;
	TweetsAdapter adapter;
	static User authUser;
	Tab homeTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		// Request the feature before setting content view
		setContentView(R.layout.activity_tweet_time_line);
		setUpActionBar();
		loadMyProfileInfo();
	}

	public void loadMyProfileInfo() {
		setProgressBarIndeterminateVisibility(true);
		SimpleTwitterApp.getRestClient().getUserProfile(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jo) {
						authUser = User.fromJson(jo);
						getActionBar().setTitle("@" + authUser.getScreenName());
						setProgressBarIndeterminateVisibility(false);
					}
				});
	}

	@SuppressLint("NewApi")
	private void setUpActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		homeTab = actionBar
				.newTab()
				.setText(R.string.tab_home)
				.setTag(HomeTimeLineFragment.TAG)
				.setIcon(R.drawable.ic_action_1391922698_65)
				.setTabListener(
						new FragmentTabListener<HomeTimeLineFragment>(
								R.id.frame_container, this,
								HomeTimeLineFragment.TAG,
								HomeTimeLineFragment.class));
		Tab mentionTab = actionBar.newTab().setText(R.string.tab_mention)
				.setTag(MentionsTimeLineFragment.TAG)
				.setIcon(R.drawable.ic_action_1391922654_icon_at)
				.setTabListener(
						new FragmentTabListener<MentionsTimeLineFragment>(
								R.id.frame_container, this,
								MentionsTimeLineFragment.TAG,
								MentionsTimeLineFragment.class));
		actionBar.addTab(homeTab, true);
		actionBar.addTab(mentionTab);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.ab_solid_example));
		getActionBar()
				.setStackedBackgroundDrawable(
						getResources().getDrawable(
								R.drawable.ab_stacked_solid_example));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_time_line, menu);
		return true;
	}

	public void onClickCompose(MenuItem mi) {
		Intent i = new Intent(this, ComposeTweetActivity.class);
		i.putExtra("screenName", authUser.getScreenName());
		i.putExtra("userProfileImageUrl", authUser.getProfileImageUrl());
		startActivityForResult(i, REQUEST_CODE);
	}

	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("screenName", authUser.getScreenName());
		bundle.putString("userProfileImageUrl", authUser.getProfileImageUrl());
		bundle.putString("name", authUser.getName());
		bundle.putString("tag", authUser.getDescription());
		bundle.putInt("followers", authUser.getFollowersCount());
		bundle.putInt("following", authUser.getFriendsCount());
		i.putExtras(bundle);
		startActivity(i);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
		if (resultCode == RESULT_OK) {
			JSONObject jsonTweet;
			try {
				jsonTweet = new JSONObject(i.getExtras().getString("jsonTweet"));
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}
			HomeTimeLineFragment.getAdapter().insert(Tweet.fromJson(jsonTweet),
					0);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().selectTab(homeTab);
	}

	// Should be called manually when an async task has started
	public void showProgressBar() {
		setProgressBarIndeterminateVisibility(true);
	}

	// Should be called when an async task has finished
	public void hideProgressBar() {
		setProgressBarIndeterminateVisibility(false);
	}

}
