package com.seiyu;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity {

	private SlidingMenu menu = null;
	private ImageView menuButton = null;
	private TextView title_text, photo = null;

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
	}

}
