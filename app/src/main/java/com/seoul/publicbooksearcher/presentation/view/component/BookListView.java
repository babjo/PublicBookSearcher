package com.seoul.publicbooksearcher.presentation.view.component;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookListViewAdapter;

import java.util.List;

public class BookListView {

    private ListView bookListView = null;

    private BookListViewAdapter bookListViewAdapter = null;
    private Context context;

    public BookListView(Context context, ListView listView){
        this.context = context;
        this.bookListView = listView;

        bookListViewAdapter = new BookListViewAdapter(context);
        bookListView.setAdapter(bookListViewAdapter);
    }

    public List<Book> getBooks(){
        return bookListViewAdapter.getItems();
    }
    public void setBooks(List<Book> books){
        bookListViewAdapter.setItems(books);
    }

    public void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(bookListView.getWindowToken(), 0);
    }

    public void clear() {
        bookListViewAdapter.clear();
    }

    public void addAll(List<Book> books) {
        bookListViewAdapter.addAll(books);
    }
}
