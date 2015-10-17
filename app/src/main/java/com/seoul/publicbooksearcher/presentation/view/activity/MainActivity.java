package com.seoul.publicbooksearcher.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.data.RecentSearchKeywordRepository;
import com.seoul.publicbooksearcher.data.cache.book.GdBookCache;
import com.seoul.publicbooksearcher.data.cache.book.SeoulBookCache;
import com.seoul.publicbooksearcher.data.crawler.GdLibrary;
import com.seoul.publicbooksearcher.data.crawler.SeoulLibrary;
import com.seoul.publicbooksearcher.data.open_api.NaverBookOpenApi;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.async_usecase.AsyncUseCase;
import com.seoul.publicbooksearcher.domain.async_usecase.SearchBooks;
import com.seoul.publicbooksearcher.domain.async_usecase.SearchTitles;
import com.seoul.publicbooksearcher.domain.usecase.AddRecentKeyword;
import com.seoul.publicbooksearcher.domain.usecase.GetRecentKeywords;
import com.seoul.publicbooksearcher.domain.usecase.IsOnline;
import com.seoul.publicbooksearcher.domain.usecase.UseCase;
import com.seoul.publicbooksearcher.presentation.presenter.BookPresenter;
import com.seoul.publicbooksearcher.presentation.view.component.BookListView;
import com.seoul.publicbooksearcher.presentation.view.component.BookTitleAutoCompleteTextView;
import com.seoul.publicbooksearcher.presentation.view.component.ProgressBarView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = MainActivity.class.getName();
    private BookListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i(TAG, "=================================== Create UseCase ======================================");
        UseCase isOnline = new IsOnline(this);
        UseCase getRecentKeywords = new GetRecentKeywords(new RecentSearchKeywordRepository(this));
        UseCase addRecentKeyword = new AddRecentKeyword(new RecentSearchKeywordRepository(this));
        AsyncUseCase searchBooks = new SearchBooks(new GdLibrary(new GdBookCache(this)), new SeoulLibrary(new SeoulBookCache(this)));
        AsyncUseCase searchTitles = new SearchTitles(new NaverBookOpenApi());

        Log.i(TAG, "=================================== Create View ==========================================");
        BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView = new BookTitleAutoCompleteTextView(this, (AutoCompleteTextView) findViewById(R.id.auto_edit));

        RecyclerView listView = (RecyclerView) findViewById(R.id.book_list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        bookListView = new BookListView(this, listView, (TextView) findViewById(R.id.empty_txt));
        setBooksIfBooksExist(savedInstanceState);

        ProgressBarView progressBarView = new ProgressBarView((RelativeLayout)findViewById(R.id.google_progress));

        Log.i(TAG, "=================================== Create Presenter =====================================");
        BookPresenter bookPresenter = new BookPresenter(isOnline, getRecentKeywords, addRecentKeyword, searchBooks, searchTitles, bookTitleAutoCompleteTextView, bookListView, progressBarView);

        bookTitleAutoCompleteTextView.setBookPresenter(bookPresenter);
    }

    private void setBooksIfBooksExist(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey("books")){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Book>>(){}.getType();
            List<Book> books = gson.fromJson(savedInstanceState.getString("books"), type);
            bookListView.setBooks(books);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        List<Book> books = bookListView.getBooks();
        Gson gson = new Gson();
        if(books.size() != 0)
            outState.putString("books", gson.toJson(books));

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);

    public class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
