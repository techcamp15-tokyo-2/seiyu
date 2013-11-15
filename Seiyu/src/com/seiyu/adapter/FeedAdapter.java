package com.seiyu.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.seiyu.R;
import com.seiyu.modal.FeedItem;
import com.seiyu.utils.MyImageLoader;

public class FeedAdapter extends BaseAdapter{
	private List<FeedItem> info = null;
	private Map<Integer, View> rowViews = new HashMap<Integer, View>();
	private Context context = null;
	private FeedItem item;

	public FeedAdapter(List<FeedItem> info, Context context) {
		this.info = info;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return info.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return info.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = rowViews.get(position);
		if (rowView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			rowView = layoutInflater.inflate(R.layout.feeditem, null);
			ImageView content_img = (ImageView)rowView.findViewById(R.id.content_img);
			TextView contentText = (TextView)rowView.findViewById(R.id.contentText);
			TextView contentText1 = (TextView)rowView.findViewById(R.id.contentText1);
			FeedItem item = (FeedItem) getItem(position);
			contentText.setText(item.getSeiyuName().toString().trim());
			contentText1.setText(item.getTimeSmap().toString().trim());
			MyImageLoader.imageLoader(content_img, item.getImageUrl(),context);
			if(item.getGender().equals("1")){
				contentText.setBackgroundColor(Color.parseColor("#00bfff"));
			}else{
				contentText.setBackgroundColor(Color.parseColor("#f08080"));
			}
			rowViews.put(position, rowView);
		}
		return rowView;
	}

	
}
