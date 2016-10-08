package com.yahoo.visisearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class OptionsActivity extends Activity {
	Spinner spSize;
	Spinner spColor;
	Spinner spType;
	EditText etSite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		ImageOptions options = (ImageOptions) getIntent().getSerializableExtra("options");
		
		spSize = (Spinner) findViewById(R.id.spSize);
		spColor = (Spinner) findViewById(R.id.spColor);
		spType = (Spinner) findViewById(R.id.spType);
		etSite = (EditText) findViewById(R.id.etSite);

		setOptions(options);
	}
	
	private void setSpinner(Spinner sp, String v) {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) sp.getAdapter();
		int pos = adapter.getPosition(v);
		sp.setSelection(pos);
	}

	private void setOptions(ImageOptions load) {
		setSpinner(spSize, load.size);
		setSpinner(spColor, load.color);
		setSpinner(spType, load.type);
		etSite.setText(load.site);
	}

	private ImageOptions getOptions() {
		ImageOptions options = new ImageOptions();

		options.size = spSize.getSelectedItem().toString();
		options.color = spColor.getSelectedItem().toString();
		options.type = spType.getSelectedItem().toString();
		options.site = etSite.getText().toString();
	
		return options;
	}
	
	public void onSave(View v) {
		ImageOptions options = getOptions();
		Intent i = new Intent();
		i.putExtra("options", options);
		setResult(52, i);
		finish();
	}
}
