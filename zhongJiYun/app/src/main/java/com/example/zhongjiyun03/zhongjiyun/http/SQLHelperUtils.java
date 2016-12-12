package com.example.zhongjiyun03.zhongjiyun.http;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by ZHONGJIYUN03 on 2016/6/29.
 * 数据库Utils
 */
public class SQLHelperUtils {


    /**
     * 查询id
     * @param context
     * @return
     */
    public  static String  queryId(Context context){
        SQLhelper sqLhelper=new SQLhelper(context);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String uid=null;  //用户id
        while (cursor.moveToNext()) {
            uid=cursor.getString(0);
        }
        return uid;
    }

    /**
     * 功能：查询电话
     * @param context
     * @return
     */
    public  static String  queryPhone(Context context){
        SQLhelper sqLhelper=new SQLhelper(context);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String phone=null;  //手机号码
        while (cursor.moveToNext()) {
            phone= cursor.getString(1);
        }
        return phone;
    }
    /**
     * 功能：查询tekon
     * @param context
     * @return
     */
    public  static String  queryTekon(Context context){
        SQLhelper sqLhelper=new SQLhelper(context);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String tekon=null;  //手机号码
        while (cursor.moveToNext()) {
            tekon= cursor.getString(6);
        }
        return tekon;
    }
    /**
     * 功能：查询星级
     * @param context
     * @return
     */

    public  static String  queryStarRate(Context context){
        SQLhelper sqLhelper=new SQLhelper(context);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String tekon=null;  //手机号码
        while (cursor.moveToNext()) {
            tekon= cursor.getString(3);
        }
        return tekon;
    }

    /**
     * 功能：查询头像
     * @param context
     * @return
     */

    public  static String queryHeadtHumb(Context context){
        SQLhelper sqLhelper=new SQLhelper(context);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String headtHumb=null;  //手机号码
        while (cursor.moveToNext()) {
            headtHumb= cursor.getString(4);
        }
        return headtHumb;
    }
    /**
     * 功能：查询头像
     * @param context
     * @return
     */

    public  static String queryName(Context context){
        SQLhelper sqLhelper=new SQLhelper(context);
        SQLiteDatabase db= sqLhelper.getWritableDatabase();
        Cursor cursor=db.query(SQLhelper.tableName, null, null, null, null, null, null);
        String name=null;  //手机号码
        while (cursor.moveToNext()) {
            name= cursor.getString(2);
        }
        return name;
    }

    /**
     * 功能：修改手机号
     * @param context
     * @param uid
     * @param phoneNumber
     */
    public static void update(Context context,String uid,String phoneNumber){
        SQLhelper sqLhelper= new SQLhelper(context);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLhelper.PHONENUMBER, phoneNumber);
        db.update(SQLhelper.tableName, contentValues,
                "uid=?", new String[]{uid});
    }

    /**
     * 功能：更新头像
     * @param context
     * @param uid
     * @param userIcon
     */
    public static  void updateUserImage(Context context,String uid,String userIcon){
        SQLhelper sqLhelper= new SQLhelper(context);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLhelper.HEADTHUMB, userIcon);
        db.update(SQLhelper.tableName, contentValues,
                "uid=?", new String[]{uid});
    }

    /**
     * 功能：更新星级
     * @param context
     * @param uid
     * @param StarRate
     */
    public static  void updateStarRate(Context context,String uid,String StarRate){
        SQLhelper sqLhelper= new SQLhelper(context);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLhelper.STARRATE, StarRate);
        db.update(SQLhelper.tableName, contentValues,
                "uid=?", new String[]{uid});
    }

    /**
     * 删除uid
     * @param context
     */

    public static  void deleteUid(Context context){
        SQLhelper sqLhelper = new SQLhelper(context);
        SQLiteDatabase db = sqLhelper.getWritableDatabase();
        Cursor cursor = db.query(SQLhelper.tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String uid = cursor.getString(0);
            if (!TextUtils.isEmpty(uid)) {
                db.delete(SQLhelper.tableName, null, null);
            }
        }




    }




}
