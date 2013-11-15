package com.seiyu;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;

import com.seiyu.adapter.BlogItemAdapter;
import com.seiyu.adapter.RecommendAdapter;
import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.RecommendItem;
import com.seiyu.modal.RecommendPicItem;

public class RecommendActivity extends Activity {

	private ListView listView = null;
	private RecommendAdapter adapter = null;
	private ImageButton backBtn = null;
	private String uid;
	private List<RecommendItem> info = new ArrayList<RecommendItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		setContentView(R.layout.recommend);
		init();
		getNews();
	}

	public void getNews() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(RecommendActivity.this,
				RecommendActivity.this.getString(R.string.URL) + "/recommend?" + "&uid=" + uid,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObject) {
						// TODO Auto-generated method stub
						try {
							String status = jsonObject
									.getString("state");
							if (status.equals("success")) {
								JSONArray infoList = jsonObject
										.getJSONArray("infoList");
								for(int i=0;i<infoList.length();i++){
									List<RecommendPicItem> infoPic = new ArrayList<RecommendPicItem>();
									JSONObject json = infoList.getJSONObject(i);
									JSONArray array = json.getJSONArray("imageList");
									for(int j=0;j<array.length();j++){
										JSONObject jo = array.getJSONObject(j);
										RecommendPicItem picItem = new RecommendPicItem();
										picItem.setImageUrl(jo.getString("imageUrl"));
										picItem.setSeiyuId(jo.getString("seiyuId"));
										picItem.setSeiyuName(jo.getString("seiyuName"));
										picItem.setTime(jo.getString("timeSmap"));
										infoPic.add(picItem);
									}
									RecommendItem ritem = new RecommendItem(json.getString("userId"), json.getString("userName"),json.getString("email"), infoPic);
                                     info.add(ritem);
									
								}
								adapter = new RecommendAdapter(info,
										RecommendActivity.this);
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
		backBtn = (ImageButton)findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.out, R.anim.out_to_right);
			}
		});
		listView = (ListView) findViewById(R.id.listView);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDividerHeight(0);
		
	}

}
