package com.seoul.publicbooksearcher.infrastructure.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 25..
 */

@EBean(scope = EBean.Scope.Singleton)
public class SqliteHelper {

    private final static String DB_NAME = "PublicBookSearcherDB";
    private static SQLiteDatabase db = null;
    private static Context context;

    public SqliteHelper(Context context){
        db = context.openOrCreateDatabase(DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
    }


    public boolean isTableExists(String tableName)
    {
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst())
        {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void dropTable(String tableName){
        db.execSQL("DROP TABLE if EXISTS " + tableName);
    }

    public SQLiteDatabase getDBInstance(){
        return db;
    }
}
