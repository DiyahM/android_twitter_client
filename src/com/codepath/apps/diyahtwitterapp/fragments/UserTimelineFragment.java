package com.codepath.apps.diyahtwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.codepath.apps.diyahtwitterapp.EndlessScrollListener;
import com.codepath.apps.diyahtwitterapp.MyTwitterApp;
import com.codepath.apps.diyahtwitterapp.R;
import com.codepath.apps.diyahtwitterapp.Tweet;
import com.codepath.apps.diyahtwitterapp.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getActivity().getIntent().getStringExtra("user") != null) {
			try {
				final User user = User.fromJson(new JSONObject(getActivity().getIntent().getStringExtra("user")));
				
				MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
						edit = pref.edit();
						edit.putLong("user_max_id", tweets.get(tweets.size()-1).getId()-1);
						edit.commit();
						getAdapter().addAll(tweets);
						ListView lvTweets = (ListView)getActivity().findViewById(R.id.lvTweets);
						lvTweets.setOnScrollListener(new EndlessScrollListener() {
						    @Override
						    public void loadMore(int page, int totalItemsCount) {
					          getMoreTweets(user.getScreenName());
						    }
						    
						    
					    });
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
					ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
					edit = pref.edit();
					edit.putLong("my_max_id", tweets.get(tweets.size()-1).getId()-1);
					edit.commit();
					getAdapter().addAll(Tweet.fromJson(jsonTweets));
					ListView lvTweets = (ListView)getActivity().findViewById(R.id.lvTweets);
					lvTweets.setOnScrollListener(new EndlessScrollListener() {
					    @Override
					    public void loadMore(int page, int totalItemsCount) {
				          getMoreTweets();
					    }
					    
					    
				    });
				}
			});
		}
	}
	
	public void getMoreTweets(String screenName){
		RequestParams requestParams = new RequestParams("screen_name", screenName);
    	requestParams.put("max_id", String.valueOf(pref.getLong("user_max_id", -1)));
		MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				adapter.addAll(Tweet.fromJson(jsonTweets));
				edit.putLong("user_max_id", tweets.get(tweets.size()-1).getId()-1);
				edit.commit();	
			}
		}, requestParams);
		
	}
	
	public void getMoreTweets(){
		RequestParams requestParams = new RequestParams("max_id", String.valueOf(pref.getLong("my_max_id", -1)));
		MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				adapter.addAll(Tweet.fromJson(jsonTweets));
				edit.putLong("my_max_id", tweets.get(tweets.size()-1).getId()-1);
				edit.commit();	
			}
		}, requestParams);
		
	}

}
