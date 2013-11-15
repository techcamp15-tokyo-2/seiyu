package com.seiyu;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.seiyu.database.DBHelper;
import com.seiyu.download.AsyncHttpClient;
import com.seiyu.download.JsonHttpResponseHandler;
import com.seiyu.modal.User;

public class RegistAvtivity extends Activity {

	private ImageButton backBtn = null;
	private EditText name, mail, pass1, pass2 = null;
	private Button sousin_btn = null;
	private String uid;
//	private SQLiteDatabase db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.regist);
		init();
		listner();
	}

	private void init() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uid = tm.getDeviceId();
		backBtn = (ImageButton) findViewById(R.id.back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.out, R.anim.out_to_right);
			}
		});
		name = (EditText) findViewById(R.id.name);
		mail = (EditText) findViewById(R.id.mail);
		pass1 = (EditText) findViewById(R.id.pass1);
		pass2 = (EditText) findViewById(R.id.pass2);
		sousin_btn = (Button) findViewById(R.id.sousin_btn);
	}

	private void listner() {
		sousin_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!name.getText().toString().trim().equals("")
						&& !mail.getText().toString().trim().equals("")
						&& !pass1.getText().toString().trim().equals("")
						&& !pass2.getText().toString().trim().equals("")) {
					String format = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
					if (!mail.getText().toString().trim().matches(format)) {
						Toast.makeText(RegistAvtivity.this, "正確なメールを入力してください！",
								Toast.LENGTH_SHORT).show();
					} else {
						if (!pass1.getText().toString().trim()
								.equals(pass2.getText().toString().trim())) {
							Toast.makeText(RegistAvtivity.this,
									"二つのパスワードが違います。。", Toast.LENGTH_LONG)
									.show();
						} else {
							AsyncHttpClient client = new AsyncHttpClient();
							client.get(RegistAvtivity.this,
									RegistAvtivity.this.getString(R.string.URL)
											+ "/register?" + "name="
											+ name.getText().toString().trim()
											+ "&email="
											+ mail.getText().toString().trim()
											+ "&pwd="
											+ pass1.getText().toString().trim()
											+ "&uid=" + uid + "&gender="
											+ "&tag=",
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
//													db = RegistAvtivity.this
//															.openOrCreateDatabase(
//																	"inform",
//																	Context.MODE_PRIVATE,
//																	null);
//													db.execSQL(
//															"INSERT INTO user VALUES (?,?,?)",
//															new Object[] { uid,
//																	name, email });
//													db.close();
													Intent intent = new Intent();
													intent.setClass(
															RegistAvtivity.this,
															MainActivity.class);
													intent.putExtra("username", name);
													intent.putExtra("email", email);
													RegistAvtivity.this
															.startActivity(intent);
													overridePendingTransition(
															R.anim.in_from_right,
															R.anim.out);
													finish();
												} else {
													Toast.makeText(
															RegistAvtivity.this,
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
						}
					}
				} else {
					Toast.makeText(RegistAvtivity.this,
							"please fill all blank!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

}
