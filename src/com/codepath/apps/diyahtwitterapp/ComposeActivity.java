package com.codepath.apps.diyahtwitterapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.codepath.apps.diyahtwitterapp.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	UserModel currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		SharedPreferences pref = getSharedPreferences("myPrefs", MODE_PRIVATE);
		String id = pref.getString("current_user", null);
		currentUser = new Select()
		                  .from(UserModel.class)
		                  .where("Id = ?", id )
		                  .executeSingle();
		setupView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public void onTweetCancel(View v) {
		Intent i = getIntent();
		setResult(RESULT_CANCELED, i);
    	finish();
	}
	
	public void setupView() {
		setTitle("Compose Tweet");
		ImageView imageView = (ImageView) findViewById(R.id.ivCProfile);
		ImageLoader.getInstance().displayImage(currentUser.getProfileImageUrl(), imageView);
		
		TextView textView = (TextView) findViewById(R.id.tvCName);
		textView.setText(Html.fromHtml("@" + currentUser.getScreenName()));
	}
	
	public void onTweetSubmit(View v) {
		String tweetText = ((EditText) findViewById(R.id.etComposeTweet)).getText().toString();
		String timeStamp = new SimpleDateFormat("EEE MMM d HH:m:s Z yyyy").format(Calendar.getInstance().getTime());
		JSONObject newTweet = new JSONObject();
		try {
			newTweet.put("text", tweetText);
			newTweet.put("created_at", timeStamp);
			newTweet.put("user", currentUser.toJSONObject());
		} catch (JSONException e1) {
			Log.d("DEBUG", "tweet put failure " + e1.getMessage());
			e1.printStackTrace();
		}
		
		
		MyTwitterApp.getRestClient().postStatusUpdate(new JsonHttpResponseHandler () {
			@Override
			public void onSuccess(JSONObject jsonTweet) {
              Log.d("DEBUG", "tweet success" + jsonTweet.toString());
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject response) {
				Log.d("DEBUG", "error" + response.toString());
			}
		}, tweetText);
		
		
    	Intent data = getIntent();
    	data.putExtra("com.codepath.apps.diyahtwitterapp.MyTweet", newTweet.toString());
    	setResult(RESULT_OK, data);
    	super.finish();
		
	}
	
	

}
