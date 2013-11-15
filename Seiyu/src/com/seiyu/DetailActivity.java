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
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.seiyu.adapter.FeedAdapter;
import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.FeedItem;

public class DetailActivity extends Activity {

	private GridView feedGridView = null;
	private FeedAdapter adapter = null;
	private List<FeedItem> items = new ArrayList<FeedItem>();
	private TextView blog, username = null;
	private ImageButton backBtn = null;
	private String seiyuId;
	private int start = 0;
	private FeedItem feedItem;
	private String uid;
	private Button follow = null;
	private String isfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail);
		Intent intent = getIntent();
		feedItem = (FeedItem) intent.getSerializableExtra("item");
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();

		init_ui();
		getNews();
		set_listner();
	}

	public void getNews() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(DetailActivity.this,
				DetailActivity.this.getString(R.string.URL) + "/imageDetail?"
						+ "uid=" + uid + "&page=0" + "&seiyuId="
						+ feedItem.getSeiyuId().toString(),
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObject) {
						// TODO Auto-generated method stub
						System.out.println("caonimade");
						try {
							String status = jsonObject.getString("state");
							if (status.equals("success")) {
								String follow = jsonObject.getString("followed");
								isfo = follow;
								isFollowed(isfo);
								JSONArray array = jsonObject
										.getJSONArray("imageList");
								for (int i = 0; i < array.length(); i++) {
									JSONObject json = array.getJSONObject(i);
									FeedItem item = new FeedItem();
									item.setImageUrl(json.getString("imageUrl")
											.toString());
									item.setSeiyuId(json.getString("seiyuId")
											.toString());
									item.setSeiyuName(json.getString(
											"seiyuName").toString());
									item.setTimeSmap(json.getString("timeSmap")
											.toString());
									item.setBlogUrl(json.getString("blogUrl")
											.toString());
									item.setGender(feedItem.getGender());
									items.add(item);
								}
								adapter = new FeedAdapter(items,
										DetailActivity.this);
							}

							feedGridView.setAdapter(adapter);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(jsonObject);
					}

				});
	}

	private void init_ui() {
		backBtn = (ImageButton) findViewById(R.id.back);
		blog = (TextView) findViewById(R.id.blog);
		feedGridView = (GridView) findViewById(R.id.gridView);
		adapter = new FeedAdapter(items, DetailActivity.this);
		username = (TextView) findViewById(R.id.username);
		username.setText(feedItem.getSeiyuName().toString());
		follow = (Button) findViewById(R.id.follow);
		

	}

	private void isFollowed(String f) {
		if (f.equals("0")) {
			follow.setText("follow");
		} else {
			follow.setText("unfollow");
		}
	}

	private void set_listner() {
		follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AsyncHttpClient client = new AsyncHttpClient();
				client.get(
						DetailActivity.this,
						DetailActivity.this.getString(R.string.URL)
								+ "/action?" + "uid=" + uid + "&followed="
								+ isfo + "&seiyuId="
								+ feedItem.getSeiyuId(),
						new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(JSONObject jsonObject) {
								// TODO Auto-generated method stub
								try {
									String status = jsonObject
											.getString("state");
									if (status.equals("success")) {
										String json = jsonObject
												.getString("message");
                                        isfo = json;
										isFollowed(json);
										
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								super.onSuccess(jsonObject);
							}

						});
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.out, R.anim.out_to_right);
			}
		});
		blog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(DetailActivity.this, BlogActivity.class);
				intent.putExtra("item", feedItem);
				DetailActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
			}
		});
		feedGridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						++start;
						AsyncHttpClient client = new AsyncHttpClient();
						client.get(
								DetailActivity.this,
								DetailActivity.this.getString(R.string.URL)
										+ "/imageDetail?" + "uid=" + uid
										+ "&page=" + start + "&seiyuId="
										+ feedItem.getSeiyuId(),
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
												for (int i = 0; i < array
														.length(); i++) {
													JSONObject json = array
															.getJSONObject(i);
													FeedItem item = new FeedItem();
													item.setImageUrl(json
															.getString(
																	"imageUrl")
															.toString());
													item.setSeiyuId(json
															.getString(
																	"seiyuId")
															.toString());
													item.setSeiyuName(json
															.getString(
																	"seiyuName")
															.toString());
													item.setTimeSmap(json
															.getString(
																	"timeSmap")
															.toString());
													item.setBlogUrl(json
															.getString(
																	"blogUrl")
															.toString());
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
				FeedItem item = (FeedItem) feedGridView
						.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.setClass(DetailActivity.this, WebActivity.class);
				intent.putExtra("url", item.getBlogUrl());
				DetailActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
			}
		});
	}
}
