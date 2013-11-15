package com.seiyu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.seiyu.database.DBHelper;

public class FirstActivity extends Activity {

	private ViewPager mViewPager;
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;
	private ImageView mPage4;
	private int currIndex = 0;
	private Button login_btn = null;
	private Button regist_btn = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.first);
		init();
	}

	private void init() {
		
		login_btn = (Button)findViewById(R.id.login_btn);
		login_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(FirstActivity.this, LoginActivity.class);
				FirstActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
				finish();
			}
		});
		regist_btn = (Button)findViewById(R.id.regist_btn);
		regist_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(FirstActivity.this, RegistAvtivity.class);
				FirstActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.in_from_right, R.anim.out);
				finish();
			}
		});
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					mPage0.setImageDrawable(getResources().getDrawable(
							R.drawable.page_now));
					mPage1.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					break;
				case 1:
					mPage1.setImageDrawable(getResources().getDrawable(
							R.drawable.page_now));
					mPage0.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					mPage2.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					break;
				case 2:
					mPage2.setImageDrawable(getResources().getDrawable(
							R.drawable.page_now));
					mPage1.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					mPage3.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					break;
				case 3:
					mPage3.setImageDrawable(getResources().getDrawable(
							R.drawable.page_now));
					mPage4.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					mPage2.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					break;
				case 4:
					mPage4.setImageDrawable(getResources().getDrawable(
							R.drawable.page_now));
					mPage3.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					break;
				}
				currIndex = arg0;

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);
		mPage3 = (ImageView) findViewById(R.id.page3);
		mPage4 = (ImageView) findViewById(R.id.page4);
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.page1, null);
		View view2 = mLi.inflate(R.layout.page2, null);
		View view3 = mLi.inflate(R.layout.page3, null);
		View view4 = mLi.inflate(R.layout.page4, null);
		View view5 = mLi.inflate(R.layout.page5, null);
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		mViewPager.setAdapter(mPagerAdapter);
	}

}
