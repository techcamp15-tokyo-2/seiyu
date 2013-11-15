package com.seiyu.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seiyu.R;
import com.seiyu.modal.BlogItem;

public class BlogItemAdapter extends BaseAdapter{
	private List<BlogItem> info = null;
	private Map<Integer, View> rowViews = new HashMap<Integer, View>();
	private Context context = null;
	private BlogItem item;

	public BlogItemAdapter(List<BlogItem> info, Context context) {
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
		View rowView = rowViews.get(position);
		if (rowView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			rowView = layoutInflater.inflate(R.layout.blogitem, null);
			rowViews.put(position, rowView);
			BlogItem item = (BlogItem) getItem(position);
			TextView date = (TextView)rowView.findViewById(R.id.date);
			TextView title = (TextView)rowView.findViewById(R.id.content);
			date.setText(item.getTimeSmap());
			title.setText(item.getBlogName());
		}
		return rowView;
		
	}

}
