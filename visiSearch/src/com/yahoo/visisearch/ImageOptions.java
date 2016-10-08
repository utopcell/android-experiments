package com.yahoo.visisearch;

import java.io.Serializable;

import android.net.Uri;

public class ImageOptions implements Serializable{
	private static final long serialVersionUID = 4973613552418377876L;
	public String size;
	public String color;
	public String type;
	public String site;
	
	public ImageOptions() {
		size = color = type = "default";
		site = "";
	}

	@Override
	public String toString() {
		String str = "";
		
		if(size != "default") {
			str += "&imgsz=" + size;
		}
		if(color != "default") {
			str += "&imgcolor=" + color;
		}
		if(type != "default") {
			str += "&imgtype=" + type;
		}
		if(site.length() > 0) {
			str += "&as_sitesearch=" + Uri.encode(site);
		}

		return str;
	}
}
