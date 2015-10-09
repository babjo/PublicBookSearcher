package com.seoul.publicbooksearcher.presentation.view.component;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.presentation.listener.SearchBooksListener;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookListViewAdapter;

import java.util.List;

public class BookListView implements SearchBooksListener {

    private ListView bookListView = null;
    private BookListViewAdapter bookListViewAdapter = null;

    private Context context;

    public BookListView(Context context, ListView listView){
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