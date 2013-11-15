package com.seiyu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;  
    
    public DBHelper(Context context, String name, CursorFactory factory,  
            int version) {  
        super(context, name, factory, version);  
        // TODO Auto-generated constructor stub  
    }  
    public DBHelper(Context context,String name){  
        this(context,name,VERSION);  
    }  
    public DBHelper(Context context,String name,int version){  
        this(context, name,null,version);  
    } 

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub 
//		db.execSQL("create table information(phone_no varchar, phone_key varchar, ko_no varchar, device_type varchar, token varchar, push_flag varchar, pass_code varchar)");
//		db.execSQL("create table notice(send_date varchar, title varchar, news_type varchar, message varchar, news_id varchar)");
		db.execSQL("create table user(uid varchar, name varchar, email varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
//		db.execSQL("alter table person add column other string");  
	} 
}
