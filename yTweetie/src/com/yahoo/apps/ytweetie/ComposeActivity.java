package com.yahoo.apps.ytweetie;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {
	private TwitterClient client;
	private EditText etNewTweet;
	private TextView tvLength;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		tvLength = (TextView) findViewById(R.id.tvLength);
		etNewTweet = (EditText) findViewById(R.id.etNewTweet);
		etNewTweet.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				int len = etNewTweet.length();
				tvLength.setText(Integer.toString(len));				
				return false;
			}
		});

		client = TwitterClientApp.getRestClient();
		client.getCredentials(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				try {
					String name = json.getString("name");
					String screenName = json.getString("screen_name");
					String imageUrl = json.getString("profile_image_url");
					updateInfo(name, screenName, imageUrl);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString() + " " + s);
			}
		});
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public void onTweet(MenuItem mi) {
		int len = etNewTweet.length();
		if(len > 140) {
			Toast.makeText(this,  "your tweet is too long!", Toast.LENGTH_SHORT).show();
			return;
		}
		String newTweet = etNewTweet.getText().toString();
		Intent data = new Intent();
		data.putExtra("tweet", newTweet);
		setResult(52, data);
		finish();
	}

	private void updateInfo(String name, String screenName, String imageUrl) {
		ImageView ivOwnerImage = (ImageView) findViewById(R.id.ivOwnerImage);
		TextView tvName = (TextView) findViewById(R.id.tvOwnerName);
		TextView tvScreenName = (TextView) findViewById(R.id.tvOwnerScreenName);
		
		ivOwnerImage.setImageResource(android.R.color.transparent);;
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(imageUrl, ivOwnerImage);
		tvName.setText(name);
		tvScreenName.setText("@"+screenName);
		etNewTweet.setText("");
	}
}
