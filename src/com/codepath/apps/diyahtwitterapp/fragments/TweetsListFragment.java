package com.codepath.apps.diyahtwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.diyahtwitterapp.EndlessScrollListener;
import com.codepath.apps.diyahtwitterapp.MyTwitterApp;
import com.codepath.apps.diyahtwitterapp.R;
import com.codepath.apps.diyahtwitterapp.Tweet;
import com.codepath.apps.diyahtwitterapp.TweetsAdapter;
import com.codepath.apps.diyahtwitterapp.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetsListFragment extends Fragment {
	TweetsAdapter adapter;
	SharedPreferences pref;
	Editor edit;
	TwitterClient twitter;
	ArrayList<Tweet> tweets;
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_tweet_list, parent, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		tweets = new ArrayList<Tweet>();
		pref = getActivity().getSharedPreferences("myPrefs", getActivity().MODE_PRIVATE);
		ListView lvTweets = (ListView)getActivity().findViewById(R.id.lvTweets);
		adapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(adapter);
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void loadMore(int page, int totalItemsCount) {
	          Log.d("DEBUG", "load more " + String.valueOf(page) + " " + String.valueOf(totalItemsCount));
	          getMoreTweets();
		    }
		    
		    
	    });
	}
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}
	
	public void getMoreTweets(){
		twitter.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				adapter.addAll(Tweet.fromJson(jsonTweets));
				edit.putLong("max_id", tweets.get(0).getId()-1);
				edit.commit();	
			}
		}, String.valueOf(pref.getLong("max_id", -1)));
		
	}
	
	public void onUserProfileView(View v) {
		Log.d("DEBUG", "Tweet Frag HAHA");
	}
}
