package com.seoul.publicbooksearcher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NaverListener{

    private UserRequester naver;
    private ArrayAdapter<String> booksAdapter;

    private final static String TAG = MainActivity.class.getName();
    private AutoCompleteTextView autoCompleteTextView;

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        naver = new Naver(this);
        filter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.i("Filter",
                        "Filter:" + constraint + " thread: " + Thread.currentThread());
                if (constraint != null && constraint.length() > 1) {
                    Log.i("Filter", "doing a search ..");
                    naver.search(constraint.toString());
                }
                return null;
            }
        };

        booksAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line){
            @Override
            public Filter getFilter() {
                return filter;
            }
        };
        /*booksAdapter = new BookAdapter(this, android.R.layout.simple_dropdown_item_1line);*/
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_edit);
        autoCompleteTextView.setAdapter(booksAdapter);
        /*
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "================================== afterTextChanged ======================================");
                naver.search(s.toString().trim());
            }
        });*/
        booksAdapter.setNotifyOnChange(false);
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
    public void searchCompleted(List<Book> books) {
        Log.i("UPDATE", "3");
        booksAdapter.clear();

        for (Book book : books) {
            booksAdapter.add(book.getTitle());
        }


        booksAdapter.notifyDataSetChanged();
        autoCompleteTextView.showDropDown();
    }
}
