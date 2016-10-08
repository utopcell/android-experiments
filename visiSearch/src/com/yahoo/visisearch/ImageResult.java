package com.yahoo.visisearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable{

	private static final long serialVersionUID = -1163792885787896630L;
	public String thumbURL;
	public String fullURL;
	
	public ImageResult(JSONObject json) {
		try {
			this.thumbURL = json.getString("tbUrl");
			this.fullURL = json.getString("url");
		} catch(JSONException e) {
			this.fullURL = null;
			this.thumbURL = null;
		}
	}
	
	public String getThumbURL() {
		return thumbURL;
	}
	public void setThumbURL(String thumbURL) {
		this.thumbURL = thumbURL;
	}
	public String getFullURL() {
		return fullURL;
	}
	public void setFullURL(String fullURL) {
		this.fullURL = fullURL;
	}
	
	@Override
	public String toString() {
		return "ImageResult [thumbURL=" + thumbURL + "]";
	}

	public static ArrayList<ImageResult> fromJSONArray(JSONArray data) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		
		for(int loop = 0; loop < data.length(); ++loop) {
			try {
				results.add(new ImageResult(data.getJSONObject(loop)));
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}
		
		return results;
	}
}
