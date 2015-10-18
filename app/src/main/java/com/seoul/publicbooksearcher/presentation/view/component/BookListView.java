package com.seoul.publicbooksearcher.presentation.view.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.seoul.publicbooksearcher.domain.Library;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookListViewAdapter;

import java.util.List;

public class BookListView {

    private RecyclerView bookListView = null;
    private TextView stateView = null;
    private Context context;

    public BookListView(Context context, RecyclerView listView, TextView stateView){
        this.context = context;
        this.bookListView = listView;
        this.stateView = stateView;

        bookListView.setAdapter(null);

    }

    public void setLibraries(List<Library> books){
        bookListView.setAdapter(new BookListViewAdapter(context, books));
    }

    public void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(bookListView.getWindowToken(), 0);
    }

    public void clear() {
        bookListView.setAdapter(null);
    }

    public void showStateMsg(String msg) {
        bookListView.setVisibility(View.GONE);
        stateView.setText(msg);
        stateView.setVisibility(View.VISIBLE);
    }

    public void showList() {
        stateView.setVisibility(View.GONE);
        bookListView.setVisibility(View.VISIBLE);
    }
}
