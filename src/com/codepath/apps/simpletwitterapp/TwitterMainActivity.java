package com.codepath.apps.simpletwitterapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.simpletwitterapp.fragments.HomeTimeLineFragment;
import com.codepath.apps.simpletwitterapp.fragments.MentionsTimeLineFragment;
import com.codepath.apps.simpletwitterapp.models.Tweet;
import com.codepath.apps.simpletwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TwitterMainActivity extends FragmentActivity implements TabListener{
	public static final String TAG = TwitterMainActivity.class.getCanonicalName();
	public static final int REQUEST_CODE = 9000; 
	TweetsAdapter adapter;
	User authUser;
	Tab homeTab ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_time_line);
		setUpActionBar();
		loadMyProfileInfo();
	}
	
	public void loadMyProfileInfo() {
		SimpleTwitterApp.getRestClient().getUserAccountSetting(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject json) {
						authUser = User.fromJson(json);
						getActionBar().setTitle("@"+authUser.getScreenName());
						SimpleTwitterApp.getRestClient().getUserProfile(authUser.getScreenName(), new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject json) {
								authUser = User.fromJson(json);
							}
						});
					}
					
				});
	}
	
	private void setUpActionBar(){
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		homeTab = actionBar.newTab().setText(R.string.tab_home).setTag(HomeTimeLineFragment.TAG).setIcon(R.drawable.ic_home).setTabListener(this);
		Tab mentionTab = actionBar.newTab().setText(R.string.tab_mention).setTag(MentionsTimeLineFragment.TAG).setIcon(R.drawable.ic_mention).setTabListener(this);
		actionBar.addTab(homeTab, true);
		actionBar.addTab(mentionTab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_time_line, menu);
		return true;
	}
	
	public void onClickCompose(MenuItem mi){
		 Intent i = new Intent(this, ComposeTweetActivity.class);
		 i.putExtra("screenName", authUser.getScreenName());
		 i.putExtra("userProfileImageUrl", authUser.getProfileImageUrl());
    	 startActivityForResult(i, REQUEST_CODE);
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
			HomeTimeLineFragment.getAdapter().insert(Tweet.fromJson(jsonTweet), 0);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager fm = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = fm.beginTransaction();
		if(tab.getTag() == HomeTimeLineFragment.TAG){
			fts.replace(R.id.frame_container, new HomeTimeLineFragment());
		}else{
			fts.replace(R.id.frame_container, new MentionsTimeLineFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().selectTab(homeTab);
	}
	
	
}
