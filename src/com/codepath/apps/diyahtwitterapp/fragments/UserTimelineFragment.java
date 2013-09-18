package com.codepath.apps.diyahtwitterapp.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.diyahtwitterapp.MyTwitterApp;
import com.codepath.apps.diyahtwitterapp.Tweet;
import com.codepath.apps.diyahtwitterapp.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;

public class UserTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getActivity().getIntent().getStringExtra("user") != null) {
			try {
				User user = User.fromJson(new JSONObject(getActivity().getIntent().getStringExtra("user")));
				
				MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						getAdapter().addAll(Tweet.fromJson(jsonTweets));
					}
				}, user.getScreenName());	
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray jsonTweets) {
					getAdapter().addAll(Tweet.fromJson(jsonTweets));
				}
			});
		}
	}

}
