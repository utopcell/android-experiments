package com.yahoo.apps.ytweetie.fragments;

import android.os.Bundle;

public class HomeTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTimeline("home");
		setScreenName("");
	}
}
