package com.seoul.publicbooksearcher.presentation.view.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;

import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookListViewAdapter;

import java.util.List;

public class BookListView {

    private RecyclerView bookListView = null;

    private BookListViewAdapter bookListViewAdapter = null;
    private Context context;

    public BookListView(Context context, RecyclerView listView){
        this.context = context;
        this.bookListView = listView;

        bookListViewAdapter = new BookListViewAdapter();
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
