package com.yahoo.apps.ytweetie;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.apps.ytweetie.fragments.UserTimelineFragment;
import com.yahoo.apps.ytweetie.models.User;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		String screenName = getIntent().getStringExtra("screenName");
		loadProfileInfo(screenName);
        ((UserTimelineFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentUserTimeline)).setScreenName(screenName);
	}
	
	private void loadProfileInfo(String screenName) {
		TwitterClientApp.getRestClient().getUserInfo(screenName, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject info) {
				User u = User.fromJson(info);
				getActionBar().setTitle("@"+u.getScreenName());
				populateProfileHeader(u);
			}
		});
	}
	
	private void populateProfileHeader(User u) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvTagline.setText(u.getTagline());
		tvFollowers.setText(u.getFollowersCount() + " followers");
		tvFollowing.setText(u.getFriendsCount() + " following");
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(),  ivProfileImage);
	}
}
