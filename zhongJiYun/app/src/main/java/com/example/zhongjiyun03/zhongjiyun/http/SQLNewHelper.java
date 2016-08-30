package com.example.zhongjiyun03.zhongjiyun.http;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLNewHelper extends SQLiteOpenHelper{
	private static final String DBNAME="zhongjiyunnew.db";
	public static final String evaluateTableName="evaluateNew";
	public static final String EVALUATENEWID="evaluateNewID";
	public static final String EVALUATE="evaluate";
	public static final String messageTableName="messageNew";
	public static final String MESSAGENEWID="messageNewID";
	public static final String MESSAGE="message";
	public static final String giftBagTableName="giftBagNew";
	public static final String GIFTBAGNEWID="giftBagNewID";
	public static final String GIFTBAG="giftBag";
	public static final String projectReplyTableName="projectReplyNew";
	public static final String PROJECTREPLYID="projectReplyNewID";
	public static final String PROJECTREPLY="projectReply";
	public static final String projectCommentTableName="projectCommentNew";
	public static final String PROJECTCOMMENTID="projectCommentID";
	public static final String PROJECTCOMMENT="projectComment";

	private static final int VERSION=1;

	public SQLNewHelper(Context context
	) {
		super(context, DBNAME, null, VERSION);
	}

	public void onCreate(SQLiteDatabase db){
//		db.execSQL("create table if not exists "+tableName+"("+ID+" integer primary key,"+NAME+" varchar,"
//				+ATT+" integer,"+AGI+" integer,"+INT+" integer)");		
		db.execSQL("create table if not exists evaluateNew(evaluateNewID varchar pr imary key,evaluate varchar)");
		db.execSQL("create table if not exists messageNew(messageNewID varchar pr imary key,message varchar)");
		db.execSQL("create table if not exists giftBagNew(giftBagNewID varchar pr imary key,giftBag varchar)");
		db.execSQL("create table if not exists projectReplyNew(projectReplyNewID varchar pr imary key,projectReply varchar)");
		db.execSQL("create table if not exists projectCommentNew(projectCommentID varchar pr imary key,projectComment varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists"+evaluateTableName);
		db.execSQL("drop table if exists"+messageTableName);
		db.execSQL("drop table if exists"+giftBagTableName);
		db.execSQL("drop table if exists"+projectReplyTableName);
		db.execSQL("drop table if exists"+projectCommentTableName);
		onCreate(db);
	}

}
