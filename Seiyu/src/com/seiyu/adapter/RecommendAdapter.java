package com.seiyu.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.seiyu.DetailActivity;
import com.seiyu.R;
import com.seiyu.UserActivity;
import com.seiyu.WebActivity;
import com.seiyu.modal.FeedItem;
import com.seiyu.modal.RecommendItem;
import com.seiyu.modal.RecommendPicItem;

public class RecommendAdapter extends BaseAdapter{
	private List<RecommendItem> info = null;
	private Map<Integer, View> rowViews = new HashMap<Integer, View>();
	private Context context = null;
	private RecommendPicAdapter adapter = null;
	public RecommendAdapter(List<RecommendItem> info,Context context) {
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
			rowView = layoutInflater.inflate(R.layout.recommenditem, null);
			final RecommendItem item = (RecommendItem) getItem(position);
			final GridView gridView = (GridView)rowView.findViewById(R.id.gridView);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					RecommendPicItem item = (RecommendPicItem) gridView
							.getItemAtPosition(position);
					FeedItem feedItem = new FeedItem();
					feedItem.setImageUrl(item.getImageUrl());
					feedItem.setSeiyuId(item.getSeiyuId());
					feedItem.setSeiyuName(item.getSeiyuName());
					feedItem.setTimeSmap(item.getTime());
					Intent intent = new Intent();
					intent.setClass(context, DetailActivity.class);
					intent.putExtra("item", feedItem);
					context.startActivity(intent);
					((Activity) context).overridePendingTransition(R.anim.in_from_right,
							R.anim.out);
				}
			});
			final Button button = (Button)rowView.findViewById(R.id.user);
			button.setText(item.getUserName());
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, UserActivity.class);
					intent.putExtra("oid", item.getUserId());
					intent.putExtra("username", item.getUserName());
					intent.putExtra("email", item.getEmail());
					intent.putExtra("from", "recommend");
					context.startActivity(intent);
					((Activity) context).overridePendingTransition(R.anim.in_from_right,
							R.anim.out);
				
				}
			});
	        adapter = new RecommendPicAdapter(item.getInfos(), context);
	        gridView.setAdapter(adapter);
			rowViews.put(position, rowView);
		}
		return rowView;
	}
}
