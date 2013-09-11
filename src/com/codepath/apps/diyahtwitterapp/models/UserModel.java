package com.codepath.apps.diyahtwitterapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class UserModel extends Model {
	@Column(name = "screen_name")
	private String screenName;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "profile_image_url")
	private String profileImageUrl;
	
	public UserModel() {
		super();
	}
	
	public UserModel(String screenName, String name, String profileImageUrl) {
		super();
		this.name = name;
		this.screenName = screenName;
		this.profileImageUrl = profileImageUrl;
	}
	
	public String getScreenName() {
		return this.screenName;
	}
	
	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}
	
	
	public JSONObject toJSONObject() {
		JSONObject jsonUser = new JSONObject();
		try {
			jsonUser.put("name", this.name);
			jsonUser.put("screen_name", this.screenName);
			jsonUser.put("profile_image_url", this.profileImageUrl);
		} catch (JSONException e) {
			e.printStackTrace();

		}
		return jsonUser;
	}


}
