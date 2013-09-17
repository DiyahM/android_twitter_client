package com.codepath.apps.diyahtwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.codepath.apps.diyahtwitterapp.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	private static final int REQUEST_CODE = 0;
	ArrayList<Tweet> tweets;
	ListView lvTweets;
	TweetsAdapter adapter;
	UserModel currentUser;
	TwitterClient twitter;
	SharedPreferences pref;
	Editor edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
		currentUser = new Select()
		                  .from(UserModel.class)
		                  .where("Id = ?", pref.getString("current_user", null))
		                  .executeSingle();
		setTitle("@" + currentUser.getScreenName());
		twitter = MyTwitterApp.getRestClient();
		twitter.getHomeTimeline(new JsonHttpResponseHandler () {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				tweets = Tweet.fromJson(jsonTweets);
				edit = pref.edit();
				edit.putLong("max_id", tweets.get(0).getId()-1);
				edit.commit();
				lvTweets = (ListView)findViewById(R.id.lvTweets);
				adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
				
				lvTweets.setOnScrollListener(new EndlessScrollListener() {
				    @Override
				    public void loadMore(int page, int totalItemsCount) {
			          Log.d("DEBUG", "load more " + String.valueOf(page) + " " + String.valueOf(totalItemsCount));
			          getMoreTweets();
				    }
			    });
			}
		});
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	
	public void onCompose(MenuItem mi) {
		Intent i = new Intent(this, ComposeActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
		  JSONObject j;
		try {
			j = new JSONObject(data.getExtras().getString("com.codepath.apps.diyahtwitterapp.MyTweet"));
			Tweet tweet = Tweet.fromJson(j);
			adapter.insert(tweet,0);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	  }
	} 
	

}
