package com.seiyu;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seiyu.adapter.FeedAdapter;
import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.FeedItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class SearchItemActivity extends Activity{
	private GridView feedGridView = null;
	private FeedAdapter adapter = null;
	private List<FeedItem> items = new ArrayList<FeedItem>();
	private int start = 0;
	private String uid;
	private ImageButton back;
	private String keyword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchitem);
		Intent intent = getIntent();
		keyword = intent.getStringExtra("input");
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		getNews("/search?", 0,keyword);
		back = (ImageButton)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.out, R.anim.out_to_right);
			}
		});
		feedGridView = (GridView) findViewById(R.id.gridView);
		feedGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
					FeedItem item = (FeedItem) feedGridView
							.getItemAtPosition(position);
					Intent intent = new Intent();
					intent.setClass(SearchItemActivity.this, DetailActivity.class);
					intent.putExtra("item", item);
					SearchItemActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.in_from_right, R.anim.out);
				
			}
		});
		feedGridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						
							++start;
							loadMore("/search?", start,keyword);
				
					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
	}
	private void loadMore(String tag,int page,String key){
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(SearchItemActivity.this,
				SearchItemActivity.this.getString(R.string.URL) + tag + "&keyword=" + key + "&uid=" + uid + "&page=" + page,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObject) {
						// TODO Auto-generated method stub
						try {
							String status = jsonObject
									.getString("state");
							if (status.equals("success")) {
								JSONArray array = jsonObject
										.getJSONArray("imageList");
								for(int i=0;i<array.length();i++){
									JSONObject json = array.getJSONObject(i);
									FeedItem item = new FeedItem();
									item.setImageUrl(json.getString("imageUrl").toString());
									item.setSeiyuId(json.getString("seiyuId").toString());
									item.setSeiyuName(json.getString("seiyuName").toString());
									item.setTimeSmap(json.getString("timeSmap").toString());
									item.setBlogUrl(json.getString("blogUrl").toString());
									items.add(item);
								}
								adapter.notifyDataSetChanged();
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(jsonObject);
					}

				});
	}
	public void getNews(String tag,int page,String key) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(SearchItemActivity.this,
				SearchItemActivity.this.getString(R.string.URL) + tag + "&uid=" + uid + "&page=" + page + "&keyword=" + key,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObject) {
						// TODO Auto-generated method stub
						try {
							System.out.println("yang-->" + jsonObject.toString());
							String status = jsonObject
									.getString("state");
							if (status.equals("success")) {
								JSONArray array = jsonObject
										.getJSONArray("imageList");
								for(int i=0;i<array.length();i++){
									JSONObject json = array.getJSONObject(i);
									FeedItem item = new FeedItem();
									item.setImageUrl(json.getString("imageUrl").toString());
									item.setSeiyuId(json.getString("seiyuId").toString());
									item.setSeiyuName(json.getString("seiyuName").toString());
									item.setTimeSmap(json.getString("timeSmap").toString());
									item.setBlogUrl(json.getString("blogUrl").toString());
									items.add(item);
								}
								adapter = new FeedAdapter(items,
										SearchItemActivity.this);
								feedGridView.setAdapter(adapter);
							}
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(jsonObject);
					}

				});
	}
	

}
