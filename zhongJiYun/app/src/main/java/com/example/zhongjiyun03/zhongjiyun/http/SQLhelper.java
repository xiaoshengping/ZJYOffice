package com.example.zhongjiyun03.zhongjiyun.http;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLhelper extends SQLiteOpenHelper{
	private static final String DBNAME="zhongjiyun.db";
	public static final String tableName="user";
	public static final String UID="uid";
	public static final String PHONENUMBER="PhoneNumber";
	public static final String NAME="Name";
	public static final String STARRATE="StarRate";
	public static final String HEADTHUMB="Headthumb";
	public static final String ROLE="Role";



	private static final int VERSION=1;

	public SQLhelper(Context context
	) {
		super(context, DBNAME, null, VERSION);
	}

	public void onCreate(SQLiteDatabase db){
//		db.execSQL("create table if not exists "+tableName+"("+ID+" integer primary key,"+NAME+" varchar,"
//				+ATT+" integer,"+AGI+" integer,"+INT+" integer)");		
		db.execSQL("create table if not exists user(uid varchar pr imary key,PhoneNumber varchar,Name varchar,StarRate varchar,Headthumb varchar,Role varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists"+tableName);
		onCreate(db);
	}

}
