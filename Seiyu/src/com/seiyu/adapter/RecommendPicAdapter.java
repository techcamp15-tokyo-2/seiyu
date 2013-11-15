package com.seiyu.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.seiyu.R;
import com.seiyu.modal.RecommendPicItem;
import com.seiyu.utils.MyImageLoader;

public class RecommendPicAdapter extends BaseAdapter{
	private List<RecommendPicItem> info = null;
	private Map<Integer, View> rowViews = new HashMap<Integer, View>();
	private Context context = null;

	public RecommendPicAdapter(List<RecommendPicItem> info, Context context) {
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
			RecommendPicItem item = (RecommendPicItem) getItem(position);
			rowView = layoutInflater.inflate(R.layout.feeditem, null);
			TextView contentText = (TextView)rowView.findViewById(R.id.contentText);
			TextView contentText1 = (TextView)rowView.findViewById(R.id.contentText1);
			ImageView image = (ImageView)rowView.findViewById(R.id.content_img);
			contentText.setText(item.getSeiyuName());
			MyImageLoader.imageLoader(image, item.getImageUrl(), context);
			contentText1.setText(item.getTime());
			rowViews.put(position, rowView);
		}
		return rowView;
	}
}
