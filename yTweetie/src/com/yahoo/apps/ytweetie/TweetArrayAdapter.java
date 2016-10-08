package com.yahoo.apps.ytweetie;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.apps.ytweetie.models.Tweet;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		
		Log.d("info", tweet.getUid() + ": " + tweet.getUser().getScreenName() + " " + tweet.getBody());

		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.tweet_item,  parent, false);
		}
		
		ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
		TextView tvRealName = (TextView) convertView.findViewById(R.id.tvRealName);
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		TextView tvRelTime = (TextView) convertView.findViewById(R.id.tvRelTime);
		TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
		ivProfileImage.setImageResource(android.R.color.transparent);
		ivProfileImage.setTag(tweet.getUser().getScreenName());
		ivProfileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String screenName = (String) v.getTag();
				Intent i = new Intent(getContext(), ProfileActivity.class);
				i.putExtra("screenName",  screenName);
				getContext().startActivity(i);
			}
		});
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		tvRealName.setText(tweet.getUser().getName());
		tvUserName.setText("@"+tweet.getUser().getScreenName());
		tvRelTime.setText(tweet.getRelativeTimeAgo());
		tvBody.setText(tweet.getBody());;
		
		return convertView;
	}

}
