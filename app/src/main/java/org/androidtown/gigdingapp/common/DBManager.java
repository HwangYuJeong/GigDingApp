package org.androidtown.gigdingapp.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DB Manager
 */

public class DBManager {

    //DB
    String TABLE_USER = "user";
    String DATABASE_NAME = "giding.db";
    int DB_VERSION = 1;

    Context mContext = null;
    private static DBManager dbManager = null;
    private SQLiteDatabase mDataBase = null;

    public static DBManager getInstance (Context context) {
        if(dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    private DBManager(Context context) {
        mContext = context;
        mDataBase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        Log.d("DBManager", "mDataBase version = " + mDataBase.getVersion());
        if(mDataBase.getVersion() < DB_VERSION) {
            String sql = "DROP TABLE IF EXISTS " + TABLE_USER;
            mDataBase.execSQL(sql);
            mDataBase.setVersion(DB_VERSION);
        }

        String DATABASE_CREATE =
                "CREATE TABLE IF NOT EXISTS user (" +
                        "_id integer primary key autoincrement" +
                        ", user_id text not null " +
                        ", pwd text not null " +
                        ", reg_date text null);";
        mDataBase.execSQL(DATABASE_CREATE);
    }


    public void deleteUserId(String userId) {
        String sql = "DELETE FROM " + TABLE_USER + " WHERE user_id = " + userId;
        mDataBase.execSQL(sql);
    }

    public void insertUserRow(String UserId, String Pwd) {
        Cursor cs = getUserId(UserId);
        if (cs == null || cs.getCount() == 0) {
            ContentValues initialValues = new ContentValues();
            initialValues.put("user_id", UserId);
            initialValues.put("pwd", Pwd);
            mDataBase.insert(TABLE_USER, null, initialValues);
        } else {
            updateUserPwd(UserId, Pwd);

        }
        cs.close();
    }

    public void updateUserPwd(String UserId, String Pwd) {
        String sql = "UPDATE " + TABLE_USER +
                " SET pwd = " + Pwd +
                " WHERE user_id = " +  UserId;
        mDataBase.execSQL(sql);
    }

    public Cursor getUserId(String UserId) {
        boolean result = false;
        String selection = "user_id = ?";
        String selectionArgs[] = new String[]{UserId};
        return mDataBase.query(TABLE_USER, new String[]{"user_id", "pwd"}, selection, selectionArgs, null, null, null, null);
    }
}
