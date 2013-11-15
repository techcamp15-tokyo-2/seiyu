package com.seiyu.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seiyu.modal.FeedItem;

public class JsonUtils {

	public List<FeedItem> parseItemFromJson(String jsonData) {
		List<FeedItem> list = new ArrayList<FeedItem>();
		Type listType = new TypeToken<LinkedList<FeedItem>>() {
		}.getType();
		Gson gson = new Gson();
		LinkedList<FeedItem> items = gson.fromJson(jsonData, listType);
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			FeedItem item = (FeedItem) iterator.next();
			list.add(item);
		}
		return list;
	}

	
}
