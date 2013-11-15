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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.seiyu.adapter.FeedAdapter;
import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.FeedItem;
import com.seiyu.utils.JsonUtils;

public class MainActivity extends Activity {

	private SlidingMenu menu = null;
	private ImageView menuButton,refresh = null;
	private TextView title_text, photo, recommend,username,search,like = null;
	private GridView feedGridView = null;
	private FeedAdapter adapter = null;
	private String uid;
	private int start = 0;
	private String uname,umail;
	private List<FeedItem> items = new ArrayList<FeedItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		Intent intent = getIntent();
		uname = intent.getStringExtra("username");
		umail = intent.getStringExtra("email");
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		configureMenu();
		init_ui();
		getNews("/latestFeed?",0);
		set_listner();
	}

	public void getNews(String tag,int pageNo) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MainActivity.this,
				MainActivity.this.getString(R.string.URL) + tag + "&uid=" + uid + "&page=" + pageNo,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObject) {
						// TODO Auto-generated method stub
                        start = 0;
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
									item.setGender(json.getString("gender").toString());
									items.add(item);
								}
								adapter = new FeedAdapter(items,
										MainActivity.this);
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
		like = (TextView)findViewById(R.id.like);
		search = (TextView)findViewById(R.id.search);
		username = (TextView)findViewById(R.id.username);
		username.setText(uname + " 様");
		recommend = (TextView)findViewById(R.id.recommend);
		menuButton = (ImageView) findViewById(R.id.newsfeed_flip);
		title_text = (TextView) findViewById(R.id.title_text);
		photo = (TextView) findViewById(R.id.photo);
		title_text.setText(photo.getText().toString().trim());
		feedGridView = (GridView) findViewById(R.id.gridView);
		refresh = (ImageView)findViewById(R.id.refresh);
		
	}

	private void set_listner() {
		
		like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.toggle();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, FavorActivity.class);
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
			}
		});
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					getNews("/latestFeed?",0);
				
				
			}
		});
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.toggle();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SearchActivity.class);
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
			}
		});
		username.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.toggle();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, UserActivity.class);
				intent.putExtra("username", uname);
				intent.putExtra("email", umail);
				intent.putExtra("oid", uid);
				intent.putExtra("from", "main");
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
			}
		});
		recommend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.toggle();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, RecommendActivity.class);
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
			}
		});
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
				menu.toggle();
				title_text.setText(photo.getText().toString().trim());
				
			}
		});
		
		feedGridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						
							++start;
							loadMore("/latestFeed?", start);
						
						
						
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
				if(isNetworkAvailable(MainActivity.this)==false && isWiFiActive(MainActivity.this)==false){
					Toast.makeText(MainActivity.this, "ネットワーク接続できませんでした。\n電波の良いところで再度お試しください。", Toast.LENGTH_SHORT).show();
				}else{
					FeedItem item = (FeedItem) feedGridView
							.getItemAtPosition(position);
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, DetailActivity.class);
					intent.putExtra("item", item);
					MainActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.in_from_right, R.anim.out);
				}
			}
		});
	}
	private void loadMore(String tag,int page){
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MainActivity.this,
				MainActivity.this.getString(R.string.URL) + tag + "&uid=" + uid + "&page=" + page,
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
								System.out.println("gan");
								for(int i=0;i<array.length();i++){
									JSONObject json = array.getJSONObject(i);
									FeedItem item = new FeedItem();
									item.setImageUrl(json.getString("imageUrl").toString());
									item.setSeiyuId(json.getString("seiyuId").toString());
									item.setSeiyuName(json.getString("seiyuName").toString());
									item.setTimeSmap(json.getString("timeSmap").toString());
									item.setBlogUrl(json.getString("blogUrl").toString());
									item.setGender(json.getString("gender").toString());
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
	/**
	 * is 3g connected
	 * 
	 * */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			System.out.println("**** newwork is off");
			return false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				System.out.println("**** newwork is off");
				return false;
			} else {
				if (info.isAvailable()) {
					System.out.println("**** newwork is on");
					return true;
				}

			}
		}
		System.out.println("**** newwork is off");
		return false;
	}

	/**
	 * is wifi connected
	 * 
	 * */
	public static boolean isWiFiActive(Context inContext) {
		WifiManager mWifiManager = (WifiManager) inContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
		if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
			System.out.println("**** WIFI is on");
			return true;
		} else {
			System.out.println("**** WIFI is off");
			return false;
		}
	}
}
