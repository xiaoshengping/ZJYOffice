package com.example.zhongjiyun03.zhongjiyun.http;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SystemEvaluateSQLhelper extends SQLiteOpenHelper{
	private static final String DBNAME="systemEvaluate.db";
	public static final String tableName="EvaluateRemind";
	public static final String MESSAGEEVALUATEID="messageEvaluateId";
	public static final String EVALUATE="evaluate";





	private static final int VERSION=1;

	public SystemEvaluateSQLhelper(Context context
	) {
		super(context, DBNAME, null, VERSION);
	}

	public void onCreate(SQLiteDatabase db){
//		db.execSQL("create table if not exists "+tableName+"("+ID+" integer primary key,"+NAME+" varchar,"
//				+ATT+" integer,"+AGI+" integer,"+INT+" integer)");		
		db.execSQL("create table if not exists EvaluateRemind(messageEvaluateId integer primary key autoincrement ,evaluate varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists"+tableName);
		onCreate(db);
	}

}
