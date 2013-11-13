package com.seiyu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.seiyu.adapter.FeedAdapter;
import com.seiyu.modal.FeedItem;

public class DetailActivity extends Activity{

	private GridView feedGridView = null;
	private FeedAdapter adapter = null;
	private List<FeedItem> items = new ArrayList<FeedItem>();
	private TextView blog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail);
		init_ui();
		set_listner();
	}
	
	private void init_ui() {
		blog = (TextView)findViewById(R.id.blog);
		feedGridView = (GridView) findViewById(R.id.gridView);
		adapter = new FeedAdapter(items, DetailActivity.this);
		FeedItem item1 = new FeedItem();
		FeedItem item2 = new FeedItem();
		FeedItem item3 = new FeedItem();
		FeedItem item4 = new FeedItem();
		FeedItem item5 = new FeedItem();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		feedGridView.setAdapter(adapter);
	}

	private void set_listner() {
		blog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(DetailActivity.this, BlogActivity.class);
				DetailActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
			}
		});
		feedGridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						FeedItem item6 = new FeedItem();
						FeedItem item7 = new FeedItem();
						FeedItem item8 = new FeedItem();
						FeedItem item9 = new FeedItem();
						items.add(item6);
						items.add(item7);
						items.add(item8);
						items.add(item9);
						adapter.notifyDataSetChanged();

					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		feedGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(DetailActivity.this, DetailActivity.class);
//				MainActivity.this.startActivity(intent);
//				overridePendingTransition(R.anim.in_from_right, R.anim.out);
//				finish();
			}
		});
	}
}
