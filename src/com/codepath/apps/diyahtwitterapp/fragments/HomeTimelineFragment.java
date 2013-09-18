package com.codepath.apps.diyahtwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;



import com.codepath.apps.diyahtwitterapp.MyTwitterApp;
import com.codepath.apps.diyahtwitterapp.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;

public class HomeTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitter = MyTwitterApp.getRestClient();
		twitter.getHomeTimeline(new JsonHttpResponseHandler () {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				edit = pref.edit();
				edit.putLong("max_id", tweets.get(0).getId()-1);
				edit.commit();
				getAdapter().addAll(tweets);
			}
		});
	}
}
