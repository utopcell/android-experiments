package com.yahoo.visisearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
		Intent i = getIntent();
		ImageResult imageResult = (ImageResult) i.getSerializableExtra("image");
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(imageResult.getFullURL());
	}
}
