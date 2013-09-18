package com.codepath.apps.diyahtwitterapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.codepath.apps.diyahtwitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.diyahtwitterapp.fragments.MentionsFragment;
import com.codepath.apps.diyahtwitterapp.models.UserModel;

public class TimelineActivity extends FragmentActivity implements TabListener{

	private static final int REQUEST_CODE = 0;
	
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
		setupNavigationTabs();
	}
	
	

	private void setupNavigationTabs() {
		ActionBar actionBar= getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome= actionBar.newTab().setText("Home")
				.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home)
				.setTabListener(this);
		Tab tabMentions = actionBar.newTab().setText("Mentions")
				.setTag("MentionsFragment").setIcon(R.drawable.ic_mentions)
				.setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
		
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



	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
		
	}



	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if (tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		} else {
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		fts.commit();
		
	}



	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
		
	} 
	

}
