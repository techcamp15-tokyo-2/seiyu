package com.seiyu.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seiyu.DetailActivity;
import com.seiyu.R;
import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.FollowItem;

public class FollowItemAdapter extends BaseAdapter{
	private List<FollowItem> info = null;
	private Map<Integer, View> rowViews = new HashMap<Integer, View>();
	private Context context = null;
	private FollowItem item;
	private String from;

	public FollowItemAdapter(List<FollowItem> info, String from,Context context) {
		this.info = info;
		this.from = from;
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
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			rowView = layoutInflater.inflate(R.layout.followitem, null);
			rowViews.put(position, rowView);
			final FollowItem item = (FollowItem) getItem(position);
			TextView name = (TextView)rowView.findViewById(R.id.name);
			final ImageView follow = (ImageView)rowView.findViewById(R.id.follow);
			if(!from.equals("main")){
				if(item.getFollowed().equals("0")){
					follow.setImageResource(R.drawable.unfollow);
				}else{
					follow.setImageResource(R.drawable.follow);
				}
				follow.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AsyncHttpClient client = new AsyncHttpClient();
						client.get(
								context,
								context.getString(R.string.URL)
										+ "/action?" + "uid=" + tm.getDeviceId() + "&followed="
										+ item.getFollowed() + "&seiyuId="
										+ item.getSeiyuId(),
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
												if(json.equals("0")){
													follow.setImageResource(R.drawable.unfollow);
												}else{
													follow.setImageResource(R.drawable.follow);
												}
												
												
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
			}
			
			
			name.setText(item.getSeiyuName());
		}
		return rowView;
		
	}
}
