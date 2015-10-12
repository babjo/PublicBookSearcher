package com.seoul.publicbooksearcher.presentation.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.SearchBooks;
import com.seoul.publicbooksearcher.presentation.view.component.BookListView;
import com.seoul.publicbooksearcher.presentation.view.component.BookTitleAutoCompleteTextView;
import com.seoul.publicbooksearcher.presentation.view.component.InstantAutoComplete;

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

        ListView listView = (ListView) findViewById(R.id.book_list);
        listView.setEmptyView(findViewById(R.id.empty_txt));
        bookListView = new BookListView(this, listView, (RelativeLayout)findViewById(R.id.google_progress));

        setBooksIfBooksExist(savedInstanceState);

        BookTitleAutoCompleteTextView bookTitleAutoCompleteTextView = new BookTitleAutoCompleteTextView(this, (InstantAutoComplete) findViewById(R.id.auto_edit));
        bookTitleAutoCompleteTextView.setSearchBooks(new SearchBooks(this, bookListView, bookTitleAutoCompleteTextView));
    }

    private void setBooksIfBooksExist(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey("books")){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Book>>(){}.getType();
            List<Book> books = gson.fromJson(savedInstanceState.getString("books"), type);
            bookListView.setBooks(books);
        }
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
