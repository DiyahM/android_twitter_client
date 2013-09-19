package com.codepath.apps.diyahtwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.widget.ListView;

import com.codepath.apps.diyahtwitterapp.EndlessScrollListener;
import com.codepath.apps.diyahtwitterapp.MyTwitterApp;
import com.codepath.apps.diyahtwitterapp.R;
import com.codepath.apps.diyahtwitterapp.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyTwitterApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets); 
				edit = pref.edit();
				edit.putLong("mentions_max_id", tweets.get(tweets.size()-1).getId()-1);
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
		});
	}
	
	public void getMoreTweets(){
		MyTwitterApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				adapter.addAll(Tweet.fromJson(jsonTweets));
				edit.putLong("mentions_max_id", tweets.get(tweets.size()-1).getId()-1);
				edit.commit();	
			}
		}, String.valueOf(pref.getLong("mentions_max_id", -1)));
		
	}
}
