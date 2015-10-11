package com.seoul.publicbooksearcher.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.domain.Book;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBookCache implements BookRepository {

    private final static String DB_NAME = "book_cache_db";
    private SQLiteDatabase db = null;

    private final Context context;

    public AbstractBookCache(Context context){
        this.context = context;
    }

    public abstract String getTableName();

    @Override
    public List<Book> selectByKeyword(String keyword) {
        if (db == null) {
            db = context.openOrCreateDatabase(DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if(!isTableExists(db, getTableName()))
                db.execSQL("CREATE TABLE " + getTableName() + " (_KEYWORD text primary key not null, BOOKS_JSON text not null)");
        }

        Cursor c = db.query(getTableName(),
                new String[] {"_KEYWORD","BOOKS_JSON"}, //colum 명세
                "_KEYWORD = ?",
                new String[] {keyword}, //where 절에 전달할 데이터
                null, //group by
                null, //having
                null
        );

        List<Book> books = null;
        if(c.moveToFirst()) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Book>>() {}.getType();
            books = gson.fromJson(c.getString(c.getColumnIndex("BOOKS_JSON")), type);
            if(books.size() == 0) return null;
        }

        return books;
    }

    boolean isTableExists(SQLiteDatabase db, String tableName)
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

    @Override
    public void insertOrUpdateBooks(String keyword, List<Book> books) {
        ContentValues args = new ContentValues();
        args.put("_KEYWORD", keyword);
        Gson gson = new Gson();
        args.put("BOOKS_JSON", gson.toJson(books));

        if(db.update(getTableName(), args, "_KEYWORD=?", new String[]{keyword}) == 0)
            db.insert(getTableName(), null, args);
    }
}
