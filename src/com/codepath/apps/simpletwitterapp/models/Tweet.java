package com.codepath.apps.simpletwitterapp.models;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Tweet extends BaseModel {
	public static final TreeMap<Long, Tweet> tweetMap = new TreeMap<Long, Tweet>();

	private User user;

	public User getUser() {
		return user;
	}

	public String getBody() {
		return getString("text");
	}

	public long getId() {
		return getLong("id");
	}

	public boolean isFavorited() {
		return getBoolean("favorited");
	}

	public boolean isRetweeted() {
		return getBoolean("retweeted");
	}

	public static Tweet fromJson(JSONObject jo) {
		Tweet tweet = new Tweet();
		try {
			tweet.jsonObject = jo;
			tweet.user = User.fromJson(jo.getJSONObject("user"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public static synchronized ArrayList<Tweet> fromJson(JSONArray ja) {
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = null;
			try {
				jo = ja.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Tweet tweet = Tweet.fromJson(jo);
			
			if (tweet != null && !tweetMap.containsKey(Long.valueOf(tweet.getId()))) {
				tweetMap.put(tweet.getId(), tweet);
			}
		}
		for(Map.Entry<Long, Tweet> e : tweetMap.entrySet()){
			Log.d("TWEET ID" , String.valueOf(e.getKey()));
		}
		return new ArrayList<Tweet>(tweetMap.values());

	}
}
