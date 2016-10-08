package com.yahoo.visisearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	private static final int page_cnt = 8;
	private static final String base_url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz="+page_cnt+"&";
	private String query;
	boolean loading;
	ImageOptions options = new ImageOptions();

	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	
	ArrayList<ImageResult> imageResults;
	ImageResultArrayAdapter imageAdapter;
	
	AsyncHttpClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		client = new AsyncHttpClient();
		loading = false;

		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		
		imageResults = new ArrayList<ImageResult>();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("image", imageResult);
				startActivity(i);
			}
		});
		
		gvResults.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Log.d("scroll", "onScroll: "+firstVisibleItem+", "+visibleItemCount+", "+totalItemCount);
				if(firstVisibleItem + page_cnt > totalItemCount) {
					getImageResults(totalItemCount);					
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {				
			}
		});
		
		clearResults();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.options:
				Intent i = new Intent(getApplicationContext(), OptionsActivity.class);
				i.putExtra("options", options);
				startActivityForResult(i, 42);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	private String googleImageURL(Integer start, String query) {

		return base_url + "start=" + start + "&q=" + Uri.encode(query) + options.toString();
	}

	public void onImageSearch(View v) {
		query = etQuery.getText().toString();
		clearResults();
		getImageResults(0);
		Toast.makeText(this, "Showing Images for: '"+query+"'", Toast.LENGTH_LONG).show();		
	}

	public void clearResults() {
		imageResults.clear();
		loading = false;
		imageAdapter.notifyDataSetChanged();
	}

	public void getImageResults(int start) {
		if(loading || query == null || query.length() == 0) {
			return;
		}
		Log.d("net", "asking for "+page_cnt+" results starting from "+start+" for query '"+query+"'");
		loading = true;
		client.get(googleImageURL(start, query), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					imageResults.addAll(ImageResult.fromJSONArray(imageJsonResults));
					imageAdapter.notifyDataSetChanged();
					loading = false;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
				Toast.makeText(SearchActivity.this, "could not retrieve results", Toast.LENGTH_LONG).show();		
				loading = false;
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 52 && requestCode == 42) {
			options = (ImageOptions) data.getSerializableExtra("options");
			onImageSearch(null);
		}
	}
}
