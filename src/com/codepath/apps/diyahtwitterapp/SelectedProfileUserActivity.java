package com.codepath.apps.diyahtwitterapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class SelectedProfileUserActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
