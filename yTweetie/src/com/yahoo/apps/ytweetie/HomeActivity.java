package com.yahoo.apps.ytweetie;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.apps.ytweetie.fragments.HomeTimelineFragment;
import com.yahoo.apps.ytweetie.fragments.MentionsTimelineFragment;
import com.yahoo.apps.ytweetie.fragments.TweetsListFragment;
import com.yahoo.apps.ytweetie.listeners.FragmentTabListener;

public class HomeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setupTabs();
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab homeTab = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home",
						HomeTimelineFragment.class));

		actionBar.addTab(homeTab);
		actionBar.selectTab(homeTab);

		Tab mentionsTab = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions",
			    		MentionsTimelineFragment.class));

		actionBar.addTab(mentionsTab);
	}
	
	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void onCompose(MenuItem mi) {
		Intent i = new Intent(this, ComposeActivity.class);
		startActivityForResult(i, 42);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 52 && requestCode == 42) {
			String tweet = data.getStringExtra("tweet");
			Log.d("info", tweet);
			TwitterClientApp.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					TweetsListFragment fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentByTag("home");
					fragmentTweetsList.refreshTweets();
				}

				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("debug", e.toString() + " " + s);
				}
			});
		}
	}
	
	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("screenName",  "");
		startActivity(i);
	}
}
