package com.seoul.publicbooksearcher.view.component;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;

import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.data.LibraryRepository;
import com.seoul.publicbooksearcher.domain.models.Book;
import com.seoul.publicbooksearcher.domain.models.Library;
import com.seoul.publicbooksearcher.domain.models.Location;
import com.seoul.publicbooksearcher.view.adapter.BookListViewAdapter;
import com.seoul.publicbooksearcher.view.adapter.BookListViewItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EBean
public class BookListView {

    @ViewById(R.id.book_list)
    RecyclerView bookListView = null;

    @Bean(LibraryRepository.class)
    LibraryRepository libraryRepository;

    private Context context;

    private BookListViewAdapter bookListViewAdapter;

    public BookListView(Context context){
        this.context = context;
    }

    @AfterViews
    public void init(){
        bookListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookListView.setLayoutManager(layoutManager);
        bookListView.setItemAnimator(new DefaultItemAnimator());
        //listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        List<Library> libraries = libraryRepository.selectAll();
        List<BookListViewItem> bookListViewItems = new ArrayList();

        for (Library library : libraries) {
            bookListViewItems.add(new BookListViewItem(library));
        }
        BookListViewAdapter bookListViewAdapter = new BookListViewAdapter(context, bookListViewItems);
        bookListView.setAdapter(bookListViewAdapter);

        this.bookListViewAdapter = (BookListViewAdapter) bookListView.getAdapter();
    }

    public void updateLibrary(Long libraryId, List<Book> books){
        bookListViewAdapter.updateItem(libraryId, books);
    }

    public void progressVisible(Long libraryId) {
        bookListViewAdapter.progressVisible(libraryId);
    }

    public void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(bookListView.getWindowToken(), 0);
    }

    public void showError(Long libraryId, String message) {
        bookListViewAdapter.showError(libraryId, message);
    }

    public void sort(Location location) {
        bookListViewAdapter.sort(location);
    }

    public void collapseAllParents() {
        bookListViewAdapter.collapseAllParents();
    }

    public void clearLibrary(Long libraryId) {
        bookListViewAdapter.clearChildItems(libraryId);
    }
}
