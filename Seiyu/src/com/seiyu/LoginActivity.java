package com.seiyu;

import org.json.JSONException;
import org.json.JSONObject;

import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button login_btn = null;
	private ImageButton backBtn = null;
	private EditText name,pwd = null;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init();
	}

	private void init() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		name = (EditText)findViewById(R.id.login_user_edit);
		pwd = (EditText)findViewById(R.id.login_passwd_edit);
		backBtn = (ImageButton)findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.out, R.anim.out_to_right);
			}
		});
		login_btn = (Button) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!name.getText().toString().trim().equals("") && !pwd.getText().equals("")){
					AsyncHttpClient client = new AsyncHttpClient();
					client.get(LoginActivity.this,
							LoginActivity.this.getString(R.string.URL)
									+ "/login?" + "email="
									+ name.getText().toString().trim()
									+ "&pwd="
									+ pwd.getText().toString().trim()
									+ "&uid="
									+ uid
									,
							new JsonHttpResponseHandler() {

								@Override
								public void onSuccess(
										JSONObject jsonObject) {
									// TODO Auto-generated method stub
									try {
										String status = jsonObject
												.getString("state");
										if (status.equals("success")) {
											String name = jsonObject
													.getString("name");
											String email = jsonObject
													.getString("email");
											User user = new User();
											user.setUid(uid);
											user.setName(name);
											user.setEmail(email);
											Intent intent = new Intent();
											intent.setClass(
													LoginActivity.this,
													MainActivity.class);
											intent.putExtra("username", name);
											intent.putExtra("email", email);
											LoginActivity.this
													.startActivity(intent);
											overridePendingTransition(
													R.anim.in_from_right,
													R.anim.out);
											finish();
										} else {
											Toast.makeText(
													LoginActivity.this,
													jsonObject
															.getString("message"),
													Toast.LENGTH_LONG)
													.show();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch
										// block
										e.printStackTrace();
									}
									super.onSuccess(jsonObject);
								}

							});
				}else{
					Toast.makeText(LoginActivity.this, "please fill all blank!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

}
