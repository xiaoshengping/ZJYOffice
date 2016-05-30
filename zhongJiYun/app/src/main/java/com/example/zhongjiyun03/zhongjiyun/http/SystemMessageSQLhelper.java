package com.example.zhongjiyun03.zhongjiyun.http;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SystemMessageSQLhelper extends SQLiteOpenHelper{
	private static final String DBNAME="systemmessage.db";
	public static final String tableName="MessageRemind";
	public static final String MESSAGEREMINDID="messageRemindId";
	public static final String GIFTBAG="GiftBag";





	private static final int VERSION=1;

	public SystemMessageSQLhelper(Context context
	) {
		super(context, DBNAME, null, VERSION);
	}

	public void onCreate(SQLiteDatabase db){
//		db.execSQL("create table if not exists "+tableName+"("+ID+" integer primary key,"+NAME+" varchar,"
//				+ATT+" integer,"+AGI+" integer,"+INT+" integer)");		
		db.execSQL("create table if not exists MessageRemind(messageRemindId integer primary key autoincrement ,GiftBag varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists"+tableName);
		onCreate(db);
	}

}
