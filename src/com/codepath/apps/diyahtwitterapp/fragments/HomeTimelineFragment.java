package com.codepath.apps.diyahtwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;



import com.codepath.apps.diyahtwitterapp.EndlessScrollListener;
import com.codepath.apps.diyahtwitterapp.MyTwitterApp;
import com.codepath.apps.diyahtwitterapp.R;
import com.codepath.apps.diyahtwitterapp.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class HomeTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitter = MyTwitterApp.getRestClient();
		twitter.getHomeTimeline(new JsonHttpResponseHandler () {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", "Homeline success");
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				edit = pref.edit();
				edit.putLong("home_max_id", tweets.get(tweets.size()-1).getId()-1);
				edit.commit();
				getAdapter().addAll(tweets);
				ListView lvTweets = (ListView)getActivity().findViewById(R.id.lvTweets);
				lvTweets.setOnScrollListener(new EndlessScrollListener() {
				    @Override
				    public void loadMore(int page, int totalItemsCount) {
			          getMoreTweets();
				    }
				    
				    
			    });
			}
			@Override
			public void onFailure(Throwable e, JSONObject response) {
				Log.d("DEBUG", "Homeline error" + response.toString());
			}
		});
		
		
	}
	
	public void getMoreTweets(){
		twitter.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				adapter.addAll(Tweet.fromJson(jsonTweets));
				edit.putLong("home_max_id", tweets.get(tweets.size()-1).getId()-1);
				edit.commit();	
			}
		}, String.valueOf(pref.getLong("home_max_id", -1)));
		
	}
}
