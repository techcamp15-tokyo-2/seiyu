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
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.seiyu.adapter.FeedAdapter;
import com.seiyu.modal.FeedItem;

public class MainActivity extends Activity {

	private SlidingMenu menu = null;
	private ImageView menuButton = null;
	private TextView title_text, photo = null;
	private GridView feedGridView = null;
	private FeedAdapter adapter = null;
	private List<FeedItem> items = new ArrayList<FeedItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		configureMenu();
		init_ui();
		set_listner();
	}

	/**
	 * configure the SlidingMenu
	 * */
	private void configureMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidth(50);
		menu.setBehindOffset(110);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.behind_view);
	}

	private void init_ui() {
		menuButton = (ImageView) findViewById(R.id.newsfeed_flip);
		title_text = (TextView) findViewById(R.id.title_text);
		photo = (TextView) findViewById(R.id.photo);
		title_text.setText(photo.getText().toString().trim());
		feedGridView = (GridView) findViewById(R.id.gridView);
		adapter = new FeedAdapter(items, MainActivity.this);
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
		menuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.toggle();
			}
		});
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				title_text.setText(photo.getText().toString().trim());
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

			}
		});
		feedGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				if(isNetworkAvailable(MainActivity.this)==false && isWiFiActive(MainActivity.this)==false){
//					Toast.makeText(MainActivity.this, "ネットワーク接続できませんでした。\n電波の良いところで再度お試しください。", Toast.LENGTH_SHORT).show();
//				}else{
//					RelateItem relateItem = (RelateItem) relateGridView
//							.getItemAtPosition(position);
//					Intent intent = new Intent();
//					intent.setClass(MainActivity.this, KouzaActivity.class);
//					intent.putExtra("url", relateItem.getUrl());
//					MainActivity.this.startActivity(intent);
//					overridePendingTransition(R.anim.in_from_right, R.anim.out);
//				}
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DetailActivity.class);
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
				finish();
			}
		});
	}

}
