package com.seoul.publicbooksearcher.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.infrastructure.sqlite.SqliteHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@EBean
public class BookCache implements BookRepository {

    private final static String TABLE_NAME = "books";
    private static final String ID_FIELD = "_ID";
    private static final String BOOKS_JSON_FIELD = "BOOKS_JSON";

    @Bean(SqliteHelper.class)
    SqliteHelper sqliteHelper;

    @AfterInject
    public void init(){
        if(!sqliteHelper.isTableExists(TABLE_NAME)) {
            getDB().execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID_FIELD + " text primary key not null, " + BOOKS_JSON_FIELD + " text not null)");
        }
    }

    @Override
    public List<Book> selectByKeywordAndLibraryId(String keyword, Long libraryId) {
        Cursor c = getDB().query(TABLE_NAME,
                new String[]{ID_FIELD, BOOKS_JSON_FIELD}, //colum 명세
                ID_FIELD + " = ?",
                new String[]{makeId(keyword, libraryId)}, //where 절에 전달할 데이터
                null, //group by
                null, //having
                null
        );

        List<Book> books = null;
        if(c.moveToFirst()) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Book>>() {}.getType();
            books = gson.fromJson(c.getString(c.getColumnIndex(BOOKS_JSON_FIELD)), type);
            if(books.size() == 0) return null;
        }

        return books;
    }

    @Override
    public void insertOrUpdateBooks(String keyword, Long libraryId, List<Book> books) {
        ContentValues args = new ContentValues();
        args.put(ID_FIELD, makeId(keyword,libraryId));
        Gson gson = new Gson();
        args.put(BOOKS_JSON_FIELD, gson.toJson(books));

        if(getDB().update(TABLE_NAME, args, ID_FIELD + "=?", new String[]{makeId(keyword, libraryId)}) == 0)
            getDB().insert(TABLE_NAME, null, args);
    }

    private String makeId(String keyword, Long library){
        return keyword + "_" + library.toString();
    }

    private SQLiteDatabase getDB(){
        return sqliteHelper.getDBInstance();
    }
}
