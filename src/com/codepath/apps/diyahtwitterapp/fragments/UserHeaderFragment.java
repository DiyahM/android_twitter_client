package com.codepath.apps.diyahtwitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.diyahtwitterapp.EndlessScrollListener;
import com.codepath.apps.diyahtwitterapp.MyTwitterApp;
import com.codepath.apps.diyahtwitterapp.R;
import com.codepath.apps.diyahtwitterapp.Tweet;
import com.codepath.apps.diyahtwitterapp.TweetsAdapter;
import com.codepath.apps.diyahtwitterapp.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserHeaderFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_user_header, parent, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (getActivity().getIntent().getStringExtra("user") != null) {
			try {
				User user = User.fromJson(new JSONObject(getActivity().getIntent().getStringExtra("user")));
				
				populateProfileHeader(user);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			MyTwitterApp.getRestClient().getUserCredentials(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					User user = User.fromJson(json);
					
					populateProfileHeader(user);
				}
				
				

				@Override
				public void onFailure(Throwable e, JSONObject response) {
					Log.d("DEBUG", "profile view error" + response.toString());
				}
			});
		}
		
		
	}
	
	public void populateProfileHeader(User user) {
		getActivity().getActionBar().setTitle("@" + user.getScreenName());
		TextView tvName = (TextView) getActivity().findViewById(R.id.tvProfileName);
		TextView tvTagline = (TextView) getActivity().findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) getActivity().findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) getActivity().findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) getActivity().findViewById(R.id.ivProfileImage);
		tvName.setText(user.getName());
		tvTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowersCount() + " Followers");
		tvFollowing.setText(user.getFriendsCount() + " Following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
		
	}

}
