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
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.seiyu.adapter.FollowItemAdapter;
import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.FeedItem;
import com.seiyu.modal.FollowItem;

public class UserActivity extends Activity {

	private TextView email,username = null;
	private ImageButton backBtn = null;
	private String uid,oid,name,mail;
	private List<FollowItem> items = new ArrayList<FollowItem>();
	private FollowItemAdapter adapter;
	private ListView listView;
	private String from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user);
		Intent intent = getIntent();
		oid = intent.getStringExtra("oid");
		name = intent.getStringExtra("username");
		mail = intent.getStringExtra("email");
		from = intent.getStringExtra("from");
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		init();
		getNews();
		listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					// TODO Auto-generated method stub
					FollowItem item = (FollowItem)listView.getItemAtPosition(position);
					Intent intent = new Intent();
					intent.setClass(UserActivity.this, DetailActivity.class);
					FeedItem fi = new FeedItem();
					fi.setGender(item.getGender());
					fi.setSeiyuId(item.getSeiyuId());
					fi.setSeiyuName(item.getSeiyuName());
					fi.setFollowed(item.getFollowed());
					intent.putExtra("item", fi);
					UserActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.in_from_right, R.anim.out);
				}
			
		});
	}
	public void getNews() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(UserActivity.this,
				UserActivity.this.getString(R.string.URL) + "/user?" + "&uid=" + uid + "&ouid=" + oid,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObject) {
						// TODO Auto-generated method stub
						try {
							String status = jsonObject
									.getString("state");
							if (status.equals("success")) {
								JSONArray array = jsonObject
										.getJSONArray("seiyuList");
								for(int i=0;i<array.length();i++){
									JSONObject json = array.getJSONObject(i);
									FollowItem item = new FollowItem();
									item.setFollowed(json.getString("followed"));
									item.setSeiyuId(json.getString("seiyuId"));
									item.setSeiyuName(json.getString("seiyuName"));
									item.setGender(json.getString("gender"));
									items.add(item);
								}
								adapter = new FollowItemAdapter(items,from,
										UserActivity.this);
								listView.setAdapter(adapter);
							}
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						super.onSuccess(jsonObject);
					}

				});
	}
	private void init() {
		listView = (ListView) findViewById(R.id.listView);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDividerHeight(0);
		backBtn = (ImageButton)findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.out, R.anim.out_to_right);
			}
		});
		username = (TextView)findViewById(R.id.username);
		username.setText(name);
		email = (TextView) findViewById(R.id.email);
		email.setText(mail);
		email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMail("こんにちは！私は中国の楊旭と申します。私は今「声優みよう」を使っています。あなたと同じ趣味があるので、これから、いろいろ交流しましょう！",mail);
			}
		});
	}

	// send email
	private void sendMail(String emailBody ,String emailReciver) {
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("plain/text");
		String emailSubject = "I want to make friends with you！";

		// 设置邮件默认地址
		email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);

		email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);

		email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);

		UserActivity.this.startActivity(Intent.createChooser(email,
				"please select ..."));
	}
}
