package com.yahoo.apps.ytweetie.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.apps.ytweetie.R;
import com.yahoo.apps.ytweetie.TweetArrayAdapter;
import com.yahoo.apps.ytweetie.TwitterClient;
import com.yahoo.apps.ytweetie.TwitterClientApp;
import com.yahoo.apps.ytweetie.models.Tweet;

public class TweetsListFragment extends Fragment {
	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private ListView lvTweets;
	
	public String timeline;
	public String screenName;

	private TwitterClient client;
	
	private boolean loading;
	private long lastId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// non-view initialization
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
		timeline = "";
		client = TwitterClientApp.getRestClient();
		loading = false;
		lastId = 1;
	}
	
	public void setTimeline(String val) {
		timeline = val;
	}
	
	public void setScreenName(String val) {
		screenName = val;
		populateTimeline();
	}

	public void populateTimeline() {
		if(loading || (screenName == null)) {
			return;
		}
		loading = true;
		client.getTimeline(lastId, timeline, screenName, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				addAll(Tweet.fromJsonArray(json));
				int pos = tweetCount();
				Tweet tweet = getTweet(pos-1);
				lastId = tweet.getUid();
				loading = false;
				Log.d("info", "got: " + lastId);
			}

			@Override
			public void onFailure(Throwable e, String s) {
				loading = false;
				Log.d("debug", "got: " + e.toString() + " " + s);
			}
		});		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		// assign view references
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);

		lvTweets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d("info", "onClick: "+position);
			}
		});
		
		lvTweets.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem+visibleItemCount >= totalItemCount-1) {
					populateTimeline();
				}
				Log.d("info", "onScroll: "+firstVisibleItem+", "+visibleItemCount+", "+totalItemCount);
			}
		});
		
		return v;
	}

	public void refreshTweets() {
		aTweets.clear();
		loading = false;
		lastId = 1;
		populateTimeline();
	}

	public int tweetCount() {
		return aTweets.getCount();
	}

	public Tweet getTweet(int pos) {
		return aTweets.getItem(pos);
	}

	// delegate adding to internal adapter
	public void addAll(ArrayList<Tweet> tweets) {
		aTweets.addAll(tweets);
	}
}
