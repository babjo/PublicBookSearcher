package com.seoul.publicbooksearcher.presentation.view.component;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.presentation.AsyncUseCaseListener;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookListViewAdapter;

import java.util.List;

public class BookListView2 implements AsyncUseCaseListener<Void, List<Book>> {

    private ListView bookListView = null;
    private RelativeLayout progressBar = null;

    private BookListViewAdapter bookListViewAdapter = null;
    private Context context;

    public BookListView2(Context context, ListView listView, RelativeLayout progressBar){
        this.context = context;
        this.bookListView = listView;
        this.progressBar = progressBar;

        bookListViewAdapter = new BookListViewAdapter(context);
        bookListView.setAdapter(bookListViewAdapter);
    }

    public List<Book> getBooks(){
        return bookListViewAdapter.getItems();
    }
    public void setBooks(List<Book> books){
        bookListViewAdapter.setItems(books);
    }

    private void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(bookListView.getWindowToken(), 0);
    }

    @Override
    public void onBefore(Void Void) {
        hideKeyboard();
        progressBar.setVisibility(RelativeLayout.VISIBLE);
        bookListViewAdapter.clear();
    }

    @Override
    public void onAfter(List<Book> afterArg) {
        progressBar.setVisibility(RelativeLayout.GONE);
        bookListViewAdapter.AddAll(afterArg);
    }

    @Override
    public void onError(Exception e) {

    }
}
