package com.codepath.apps.simpletwitterapp.models;

import org.json.JSONObject;

public class User extends BaseModel{
	
	public String getName(){
		return getString("name");
	}
	
	public long getId(){
		return getLong("id");
	}
	
	public String getScreenName(){
		return getString("screen_name");
	}
	
	public String getProfileImageUrl(){
		return getString("profile_image_url");
	}
	
	public String getProfileBackGroundImageUrl(){
		return getString("profile_background_image_url");
	}
	
	public int getNumTweets(){
		return getInt("statuses_count");
	}
	
	public int getFollowersCount(){
		return getInt("followers_count");
	}
	
	public int getFriendsCount(){
		return getInt("friends_count");
	}
	
	public static User fromJson(JSONObject jo) {
		User user = new User();
		try {
			user.jsonObject = jo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return user;

	}
	
}
