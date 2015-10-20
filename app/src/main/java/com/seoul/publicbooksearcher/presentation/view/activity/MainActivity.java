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

import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.data.BookRepository;
import com.seoul.publicbooksearcher.data.RecentSearchKeywordRepository;
import com.seoul.publicbooksearcher.data.cache.book.BookCache;
import com.seoul.publicbooksearcher.data.crawler.BaseLibrary;
import com.seoul.publicbooksearcher.data.crawler.SeoulLibrary;
import com.seoul.publicbooksearcher.data.open_api.NaverBookOpenApi;
import com.seoul.publicbooksearcher.domain.async_usecase.AsyncUseCase;
import com.seoul.publicbooksearcher.domain.async_usecase.SearchBooks;
import com.seoul.publicbooksearcher.domain.async_usecase.SearchTitles;
import com.seoul.publicbooksearcher.domain.usecase.AddRecentKeyword;
import com.seoul.publicbooksearcher.domain.usecase.GetRecentKeywords;
import com.seoul.publicbooksearcher.domain.usecase.IsOnline;
import com.seoul.publicbooksearcher.domain.usecase.UseCase;
import com.seoul.publicbooksearcher.presentation.presenter.BookPresenter;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookListViewAdapter;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookListViewItem;
import com.seoul.publicbooksearcher.presentation.view.component.BookListView;
import com.seoul.publicbooksearcher.presentation.view.component.BookTitleAutoCompleteTextView;
import com.seoul.publicbooksearcher.presentation.view.component.ProgressBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private final static String TAG = MainActivity.class.getName();
    private RecyclerView listView;

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
        Map<String, BookRepository> libraries = createLibraries();
        List<BookListViewItem> bookListViewItems = new ArrayList();
        for(String libraryName : libraries.keySet()) {
            Log.i(TAG, libraryName);
            bookListViewItems.add(new BookListViewItem(libraryName));
        }
        AsyncUseCase searchBooks = new SearchBooks(libraries);
        AsyncUseCase searchTitles = new SearchTitles(new NaverBookOpenApi());

        Log.i(TAG, "=================================== Create View ==========================================");
        BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView = new BookTitleAutoCompleteTextView(this, (AutoCompleteTextView) findViewById(R.id.auto_edit));

        listView = (RecyclerView) findViewById(R.id.book_list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        BookListViewAdapter bookListViewAdapter = new BookListViewAdapter(this, bookListViewItems);
        if(savedInstanceState != null)
            bookListViewAdapter.onRestoreInstanceState(savedInstanceState);
        listView.setAdapter(bookListViewAdapter);

        BookListView bookListView = new BookListView(this, listView, (TextView) findViewById(R.id.empty_txt));

        Log.i(TAG, "=================================== Create Presenter =====================================");
        BookPresenter bookPresenter = new BookPresenter(isOnline, getRecentKeywords, addRecentKeyword, searchBooks, searchTitles, bookTitleAutoCompleteTextView, bookListView);

        bookTitleAutoCompleteTextView.setBookPresenter(bookPresenter);
    }

    private Map createLibraries(){
        Map<String, BookRepository> bookRepositoryMap = new HashMap<>();
        bookRepositoryMap.put("서울도서관", new SeoulLibrary(new BookCache(this)));

        bookRepositoryMap.put("강남도서관", new BaseLibrary(new BookCache(this), "111003"));
        bookRepositoryMap.put("강동도서관", new BaseLibrary(new BookCache(this), "111004"));
        bookRepositoryMap.put("강서도서관", new BaseLibrary(new BookCache(this), "111005"));
        bookRepositoryMap.put("고덕평생학습관", new BaseLibrary(new BookCache(this), "111007"));


        bookRepositoryMap.put("고척도서관", new BaseLibrary(new BookCache(this), "111008"));
        bookRepositoryMap.put("구로도서관", new BaseLibrary(new BookCache(this), "111009"));
        bookRepositoryMap.put("개포도서관", new BaseLibrary(new BookCache(this), "111006"));
        bookRepositoryMap.put("남산도서관", new BaseLibrary(new BookCache(this), "111010"));

        bookRepositoryMap.put("노원평생학습관", new BaseLibrary(new BookCache(this), "111022"));
        bookRepositoryMap.put("동대문도서관", new BaseLibrary(new BookCache(this), "111012"));
        bookRepositoryMap.put("도봉도서관", new BaseLibrary(new BookCache(this), "111011"));
        bookRepositoryMap.put("동작도서관", new BaseLibrary(new BookCache(this), "111013"));

        bookRepositoryMap.put("마포평생학습관", new BaseLibrary(new BookCache(this), "111014"));
        bookRepositoryMap.put("마포평생아현분관", new BaseLibrary(new BookCache(this), "111031"));
        bookRepositoryMap.put("서대문도서관", new BaseLibrary(new BookCache(this), "111016"));
        bookRepositoryMap.put("송파도서관", new BaseLibrary(new BookCache(this), "111030"));

        bookRepositoryMap.put("양천도서관", new BaseLibrary(new BookCache(this), "111015"));
        bookRepositoryMap.put("어린이도서관", new BaseLibrary(new BookCache(this), "111017"));
        bookRepositoryMap.put("영등포평생학습관", new BaseLibrary(new BookCache(this), "111018"));
        bookRepositoryMap.put("용산도서관", new BaseLibrary(new BookCache(this), "111019"));

        bookRepositoryMap.put("정독도서관", new BaseLibrary(new BookCache(this), "111020"));
        bookRepositoryMap.put("종로도서관", new BaseLibrary(new BookCache(this), "111021"));

        return bookRepositoryMap;
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
        super.onSaveInstanceState(outState);
        ((BookListViewAdapter) listView.getAdapter()).onSaveInstanceState(outState);
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
