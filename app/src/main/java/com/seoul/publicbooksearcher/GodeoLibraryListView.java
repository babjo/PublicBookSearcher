package com.seoul.publicbooksearcher;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import java.util.List;

public class GodeoLibraryListView implements UserRequestListener{

    private ListView bookListView = null;
    private BookListViewAdapter bookListViewAdapter = null;

    private Context context;

    public GodeoLibraryListView(Context context, ListView listView){
        this.bookListView = listView;
        this.context = context;

        bookListViewAdapter = new BookListViewAdapter(context);
        bookListView.setAdapter(bookListViewAdapter);
    }

    @Override
    public void searchBefore() {
        hideKeyboard();
    }


    private void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(bookListView.getWindowToken(), 0);
    }


    @Override
    public void searchCompleted(List<Book> books) {
        bookListViewAdapter.clearAndAddAll(books);
    }
}
