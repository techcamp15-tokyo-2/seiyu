package com.seiyu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class WebActivity extends Activity {

	private WebView webView = null;
	private ImageButton backBtn = null;
	private String url;
	private TextView name = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web);
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		init();
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
		webView = (WebView) findViewById(R.id.webView);
		webView.setScrollBarStyle(0);
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setDomStorageEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setPluginsEnabled(true);
		settings.setBuiltInZoomControls(true);
		 webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				webView.loadUrl(url);
				return true;
			}

		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (webView.canGoBack()) {
				webView.goBack();
				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			moveTaskToBack(true);
			return true;
		}

		return true;
	}

}
