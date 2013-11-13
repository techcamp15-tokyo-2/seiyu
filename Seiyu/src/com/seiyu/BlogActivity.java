package com.seiyu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.seiyu.adapter.BlogItemAdapter;
import com.seiyu.modal.BlogItem;
import com.seiyu.xListView.XListView;
import com.seiyu.xListView.XListView.IXListViewListener;

public class BlogActivity extends Activity implements IXListViewListener{
	
	private XListView listView = null;
	private List<BlogItem> myListBlogItem = new ArrayList<BlogItem>();
	private BlogItemAdapter adapter = null;
	private Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blog);
		init_listview();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	
	private void init_listview(){
		listView = (XListView) findViewById(R.id.xListView);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDividerHeight(0);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		mHandler = new Handler();
		BlogItem item1 = new BlogItem();
		BlogItem item2 = new BlogItem();
		BlogItem item3 = new BlogItem();
		BlogItem item4 = new BlogItem();
		BlogItem item5 = new BlogItem();
		myListBlogItem.add(item5);
		myListBlogItem.add(item4);
		myListBlogItem.add(item3);
		myListBlogItem.add(item2);
		myListBlogItem.add(item1);
		adapter = new BlogItemAdapter(myListBlogItem, BlogActivity.this);
		listView.setAdapter(adapter);
	}

}
