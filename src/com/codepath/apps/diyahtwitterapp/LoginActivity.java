package com.codepath.apps.diyahtwitterapp;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.activeandroid.query.Select;
import com.codepath.apps.diyahtwitterapp.models.UserModel;
import com.codepath.oauth.OAuthLoginActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
    	MyTwitterApp.getRestClient().getUserCredentials(new JsonHttpResponseHandler () {
			@Override
			public void onSuccess(JSONObject jsonUser) {
				User user = User.fromJson(jsonUser);
				UserModel currentUser = new Select()
				                            .from(UserModel.class)
				                            .where("screen_name = ?", user.getScreenName())
				                            .executeSingle();
				if (currentUser == null) {
					currentUser = new UserModel(user.getScreenName(), user.getName(), user.getProfileImageUrl());
					currentUser.save();
				}
				
				SharedPreferences pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
				Editor edit = pref.edit();
				edit.putString("current_user", currentUser.getId().toString());
				edit.commit();
				Intent i = new Intent(getBaseContext(), TimelineActivity.class);
		    	startActivity(i);
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject response) {
				Log.d("DEBUG", "error" + response.toString());
			}
		});
    	
    }
    
    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
    
    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
        getClient().connect();
    }

}
