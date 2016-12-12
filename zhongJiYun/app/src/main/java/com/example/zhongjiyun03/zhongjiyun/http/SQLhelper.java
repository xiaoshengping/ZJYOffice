package com.example.zhongjiyun03.zhongjiyun.http;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLhelper extends SQLiteOpenHelper{
	private static final String DBNAME="zhongjiyun.db";  //数据库名字
	public static final String tableName="user";   //数据库表
	public static final String UID="uid";      //uid
	public static final String PHONENUMBER="PhoneNumber";   //手机号码
	public static final String NAME="Name";    //名字
	public static final String STARRATE="StarRate";  //星级
	public static final String HEADTHUMB="Headthumb";   //头像链接
	public static final String ROLE="Role";   //角色




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
