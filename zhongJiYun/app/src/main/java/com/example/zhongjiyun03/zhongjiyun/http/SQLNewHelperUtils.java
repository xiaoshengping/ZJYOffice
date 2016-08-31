package com.example.zhongjiyun03.zhongjiyun.http;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by ZHONGJIYUN03 on 2016/6/29.
 */
public class SQLNewHelperUtils {


    /**
     * 功能：查询评价红点提示
     * @param context
     * @return
     */
    public  static String  queryEvaluate(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.evaluateTableName, null, null, null, null, null, null);
        String evaluate=null;  //用户id
        while (cursor.moveToNext()) {
            evaluate=cursor.getString(1);
        }
        return evaluate;
    }

    /**
     * 功能：查询评价红点提示id
     * @param context
     * @return
     */
    public  static String  queryEvaluateId(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.evaluateTableName, null, null, null, null, null, null);
        String evaluateId=null;  //用户id
        while (cursor.moveToNext()) {
            evaluateId=cursor.getString(0);
        }
        return evaluateId;
    }

    /**
     * 功能：查询消息红点提示
     * @param context
     * @return
     */
    public  static String  queryMessage(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.messageTableName, null, null, null, null, null, null);
        String message=null;  //手机号码
        while (cursor.moveToNext()) {
            message= cursor.getString(1);
        }
        return message;
    }

    /**
     * 功能：查询消息红点提示id
     * @param context
     * @return
     */
    public  static String  queryMessageId(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.messageTableName, null, null, null, null, null, null);
        String messageId=null;  //手机号码
        while (cursor.moveToNext()) {
            messageId= cursor.getString(0);
        }
        return messageId;
    }
    /**
     * 功能：查询红包红点提示
     * @param context
     * @return
     */
    public  static String  queryGiftBag(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.giftBagTableName, null, null, null, null, null, null);
        String giftBag=null;  //手机号码
        while (cursor.moveToNext()) {
            giftBag= cursor.getString(1);
        }
        return giftBag;
    }

    /**
     * 功能：查询红包红点提示id
     * @param context
     * @return
     */
    public  static String  queryGiftBagId(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.giftBagTableName, null, null, null, null, null, null);
        String giftBagId=null;  //手机号码
        while (cursor.moveToNext()) {
            giftBagId= cursor.getString(0);
        }
        return giftBagId;
    }

    /**
     * 功能：查询竞标红点提示
     * @param context
     * @return
     */
    public  static String  queryProjectReply(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.projectReplyTableName, null, null, null, null, null, null);
        String queryProject=null;  //手机号码
        while (cursor.moveToNext()) {
            queryProject= cursor.getString(1);
        }
        return queryProject;
    }

    /**
     * 功能：查询竞标红点提示id
     * @param context
     * @return
     */
    public  static String  queryProjectId(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.projectReplyTableName, null, null, null, null, null, null);
        String queryProjectId=null;  //手机号码
        while (cursor.moveToNext()) {
            queryProjectId= cursor.getString(0);
        }
        return queryProjectId;
    }

    /**
     * 功能：查询是否刷新项目详情
     * @param context
     * @return
     */
    public  static String  queryProjectComment(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.projectCommentTableName, null, null, null, null, null, null);
        String projectComment=null;  //手机号码
        while (cursor.moveToNext()) {
            projectComment= cursor.getString(1);
        }
        return projectComment;
    }

    /**
     * 功能：查询是否刷新项目详情id
     * @param context
     * @return
     */
    public  static String  queryProjectCommentId(Context context){
        SQLNewHelper SQLNewHelper=new SQLNewHelper(context);
        SQLiteDatabase db= SQLNewHelper.getWritableDatabase();
        Cursor cursor=db.query(SQLNewHelper.projectCommentTableName, null, null, null, null, null, null);
        String projectCommentId=null;  //手机号码
        while (cursor.moveToNext()) {
            projectCommentId= cursor.getString(0);
        }
        return projectCommentId;
    }
    /**
     * 功能：修改是否刷新项目详情
     * @param context
     * @param id
     * @param evaluate
     */
    public static void updateProjectComment(Context context,String id,String evaluate){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db = SQLNewHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLNewHelper.PROJECTCOMMENT, evaluate);
        db.update(SQLNewHelper.projectCommentTableName, contentValues,
                "projectCommentNewID=?", new String[]{id});
    }
    /**
     * 功能：修改竞标红点提示时间
     * @param context
     * @param id
     * @param evaluate
     */
    public static void updateProjectReply(Context context,String id,String evaluate){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db = SQLNewHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLNewHelper.PROJECTREPLY, evaluate);
        db.update(SQLNewHelper.projectReplyTableName, contentValues,
                "projectReplyNewID=?", new String[]{id});
    }
    /**
     * 功能：修改评价红点提示时间
     * @param context
     * @param id
     * @param evaluate
     */
    public static void updateEvaluate(Context context,String id,String evaluate){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db = SQLNewHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLNewHelper.EVALUATE, evaluate);
        db.update(SQLNewHelper.evaluateTableName, contentValues,
                "evaluateNewID=?", new String[]{id});
    }

    /**
     * 功能：更新消息
     * @param context
     * @param id
     * @param message
     */
    public static  void updateMessage(Context context,String id,String message){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db = SQLNewHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLNewHelper.MESSAGE, message);
        db.update(SQLNewHelper.messageTableName, contentValues,
                "messageNewID=?", new String[]{id});
    }

    /**
     * 功能：更新红包
     * @param context
     * @param id
     * @param giftBag
     */
    public static  void updateGiftBag(Context context,String id,String giftBag){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db = SQLNewHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLNewHelper.GIFTBAG, giftBag);
        db.update(SQLNewHelper.giftBagTableName, contentValues,
                "giftBagNewID=?", new String[]{id});
    }

    /**
     * 插入评价数据
     * @param context
     * @param evaluateNewID
     * @param evaluate
     */
    public static void insertEvaluate(Context context,String evaluateNewID,String evaluate){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db=SQLNewHelper.getWritableDatabase();
        // db.execSQL("insert into user(uid,userName,userIcon,state) values('战士',3,5,7)");
        ContentValues values=new ContentValues();
        values.put(SQLNewHelper.EVALUATENEWID,evaluateNewID);
        values.put(SQLNewHelper.EVALUATE,evaluate);
        db.insert(SQLNewHelper.evaluateTableName, SQLNewHelper.EVALUATENEWID, values);
        db.close();
    }
    /**
     * 插入消息数据
     * @param context
     * @param messageNewID
     * @param message
     */
    public static void insertMessage(Context context,String messageNewID,String message){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db=SQLNewHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SQLNewHelper.MESSAGENEWID,messageNewID);
        values.put(SQLNewHelper.MESSAGE,message);
        db.insert(SQLNewHelper.messageTableName, SQLNewHelper.MESSAGENEWID, values);
        db.close();
    }

    /**
     * 插入红包数据
     * @param context
     * @param giftBagNewID
     * @param giftBag
     */
    public static void insertGiftBag(Context context,String giftBagNewID,String giftBag){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db=SQLNewHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SQLNewHelper.GIFTBAGNEWID,giftBagNewID);
        values.put(SQLNewHelper.GIFTBAG,giftBag);
        db.insert(SQLNewHelper.giftBagTableName, SQLNewHelper.GIFTBAGNEWID, values);
        db.close();
    }

    /**
     * 插入竞标数据
     * @param context
     * @param projectReplyNewID
     * @param projectReply
     */
    public static void insertprojectReply(Context context,String projectReplyNewID,String projectReply){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db=SQLNewHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SQLNewHelper.PROJECTREPLYID,projectReplyNewID);
        values.put(SQLNewHelper.PROJECTREPLY,projectReply);
        db.insert(SQLNewHelper.projectReplyTableName, SQLNewHelper.PROJECTREPLYID, values);
        db.close();
    }

    /**
     * 插入是否刷新项目详情
     * @param context
     * @param projectReplyNewID
     * @param projectReply
     */
    public static void insertProjectComment(Context context,String projectReplyNewID,String projectReply){
        SQLNewHelper SQLNewHelper= new SQLNewHelper(context);
        SQLiteDatabase db=SQLNewHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SQLNewHelper.PROJECTCOMMENTID,projectReplyNewID);
        values.put(SQLNewHelper.PROJECTCOMMENT,projectReply);
        db.insert(SQLNewHelper.projectCommentTableName, SQLNewHelper.PROJECTCOMMENTID, values);
        db.close();
    }
    /**
     * 删除评价表
     * @param context
     */

    public static  void deleteEvaluateId(Context context){
        SQLNewHelper SQLNewHelper = new SQLNewHelper(context);
        SQLiteDatabase db = SQLNewHelper.getWritableDatabase();
        Cursor cursor = db.query(SQLNewHelper.evaluateTableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String uid = cursor.getString(0);
            if (!TextUtils.isEmpty(uid)) {
                db.delete(SQLNewHelper.evaluateTableName, null, null);
            }
        }



    }
    /**
     * 删除消息表
     * @param context
     */

    public static  void deleteMessageId(Context context){
        SQLNewHelper SQLNewHelper = new SQLNewHelper(context);
        SQLiteDatabase db = SQLNewHelper.getWritableDatabase();
        Cursor cursor = db.query(SQLNewHelper.messageTableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String uid = cursor.getString(0);
            if (!TextUtils.isEmpty(uid)) {
                db.delete(SQLNewHelper.messageTableName, null, null);
            }
        }



    }
    /**
     * 删除红包表
     * @param context
     */

    public static  void deleteGiftBagId(Context context){
        SQLNewHelper SQLNewHelper = new SQLNewHelper(context);
        SQLiteDatabase db = SQLNewHelper.getWritableDatabase();
        Cursor cursor = db.query(SQLNewHelper.giftBagTableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String uid = cursor.getString(0);
            if (!TextUtils.isEmpty(uid)) {
                db.delete(SQLNewHelper.giftBagTableName, null, null);
            }
        }



    }





}
