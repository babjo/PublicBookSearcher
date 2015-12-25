package com.seoul.publicbooksearcher.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.seoul.publicbooksearcher.infrastructure.sqlite.SqliteHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class RecentSearchKeywordRepository implements KeywordRepository{

    private final static String TABLE_NAME = "keyword_table";

    @Bean(SqliteHelper.class)
    SqliteHelper sqliteHelper;

    @Override
    public void insertKeyword(String keyword) {
        ContentValues args = new ContentValues();
        args.put("KEYWORD", keyword);
        getDB().insert(TABLE_NAME, null, args);
    }

    @AfterInject
    public void init(){
        //db.execSQL("DROP TABLE if EXISTS " + getTableName());
        if(!sqliteHelper.isTableExists(TABLE_NAME)) {
            getDB().execSQL("CREATE TABLE " + TABLE_NAME + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, KEYWORD text not null unique)");
        }
    }

    @Override
    public List<String> selectAll() {
        Cursor c = getDB().query(TABLE_NAME,
                new String[]{"_ID", "KEYWORD"}, //colum 명세
                null,
                null, //where 절에 전달할 데이터
                null, //group by
                null, //having
                "_ID DESC"
        );

        List<String> keywords = new ArrayList();
        while(c.moveToNext())
            keywords.add(c.getString(c.getColumnIndex("KEYWORD")));

        return keywords;
    }


    private SQLiteDatabase getDB(){
        return sqliteHelper.getDBInstance();
    }
}
