package com.seoul.publicbooksearcher;

import android.content.Context;
import android.widget.ListView;

import java.util.List;

public class GodeoLibraryListView implements UserRequestListener{

    private ListView bookListView = null;
    private BookListViewAdapter bookListViewAdapter = null;

    public GodeoLibraryListView(Context context, ListView listView){
        this.bookListView = listView;

        bookListViewAdapter = new BookListViewAdapter(context);
        bookListView.setAdapter(bookListViewAdapter);
    }

    @Override
    public void searchCompleted(List<Book> books) {
        bookListViewAdapter.clearAndAddAll(books);
    }
}
