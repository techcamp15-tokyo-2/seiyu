package com.seiyu;

import com.seiyu.database.DBHelper;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class StartActivity extends Activity{

	private SQLiteDatabase db = null;
	private DBHelper helper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		helper = new DBHelper(this, "inform");
		db = helper.getWritableDatabase();
	}

}
