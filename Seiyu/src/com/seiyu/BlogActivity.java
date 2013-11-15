package com.seiyu;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seiyu.adapter.BlogItemAdapter;
import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.BlogItem;
import com.seiyu.modal.FeedItem;
import com.seiyu.xListView.XListView;
import com.seiyu.xListView.XListView.IXListViewListener;

public class BlogActivity extends Activity implements IXListViewListener{
	
	private XListView listView = null;
	private List<BlogItem> myListBlogItem = new ArrayList<BlogItem>();
	private BlogItemAdapter adapter = null;
	private Handler mHandler = null;
	private ImageButton backBtn = null;
	private FeedItem item;
	private int start = 0;
	private String uid;
	private TextView username = null;
	private ProgressBar progress = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blog);
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		Intent intent = getIntent();
		item = (FeedItem) intent.getSerializableExtra("item");
		init_UI();
		init_listview();
		getNews();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				BlogItem item = (BlogItem)listView.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.putExtra("url", item.getBlogUrl());
				intent.setClass(BlogActivity.this, WebActivity.class);
				BlogActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
			}
		});
	}

	public void getNews() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(BlogActivity.this,
				BlogActivity.this.getString(R.string.URL) + "/blogDetail?" + "&uid=" + uid + "&seiyuId=" + item.getSeiyuId() + "&page=0",
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObject) {
						// TODO Auto-generated method stub
						try {
							String status = jsonObject
									.getString("state");
							if (status.equals("success")) {
								JSONArray array = jsonObject
										.getJSONArray("blogList");
								for(int i=0;i<array.length();i++){
									JSONObject json = array.getJSONObject(i);
									BlogItem item = new BlogItem();
									item.setBlogName(json.getString("blogName"));
									item.setTimeSmap(json.getString("timeSmap"));
									item.setBlogUrl(json.getString("blogUrl"));
									myListBlogItem.add(item);
								}
								adapter = new BlogItemAdapter(myListBlogItem,
										BlogActivity.this);
							}
							progress.setVisibility(View.GONE);
							listView.setAdapter(adapter);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(jsonObject);
					}

				});
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				progress.setVisibility(View.VISIBLE);
				getNews();
				listView.stopRefresh();
				listView.stopLoadMore();
				progress.setVisibility(View.GONE);
			}
		}, 3000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				progress.setVisibility(View.VISIBLE);
				++start;
				AsyncHttpClient client = new AsyncHttpClient();
				client.get(BlogActivity.this,
						BlogActivity.this.getString(R.string.URL) + "/blogDetail?" + "uid=" + uid + "&seiyuId=" + item.getSeiyuId() + "&page="
								+ start, new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(JSONObject jsonObject) {
								// TODO Auto-generated method stub
								try {
									String status = jsonObject
											.getString("state");
									if (status.equals("success")) {
										JSONArray array = jsonObject
												.getJSONArray("blogList");
										for(int i=0;i<array.length();i++){
											JSONObject json = array.getJSONObject(i);
											BlogItem item = new BlogItem();
											item.setBlogName(json.getString("blogName"));
											item.setBlogUrl(json.getString("timeSmap"));
											item.setBlogUrl(json.getString("blogUrl"));
											myListBlogItem.add(item);
										}
										progress.setVisibility(View.GONE);
										adapter.notifyDataSetChanged();
									}
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								super.onSuccess(jsonObject);
							}

						});
				listView.stopRefresh();
				listView.stopLoadMore();
			}
		}, 3000);
	}
	
	private void init_UI(){
		progress = (ProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.VISIBLE);
		username = (TextView)findViewById(R.id.username);
		username.setText(item.getSeiyuName()+"のブログ");
		backBtn = (ImageButton)findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.out, R.anim.out_to_right);
			}
		});
	}
	
	private void init_listview(){
		listView = (XListView) findViewById(R.id.xListView);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDividerHeight(0);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		mHandler = new Handler();
		
	}

}
