package com.seoul.publicbooksearcher.presentation.view.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

import com.seoul.publicbooksearcher.domain.SearchTitles;
import com.seoul.publicbooksearcher.domain.UseCase;
import com.seoul.publicbooksearcher.presentation.listener.SearchTitlesListener;
import com.seoul.publicbooksearcher.presentation.view.activity.MainActivity;

import java.util.List;

public class BookTitleAutoCompleteTextView implements SearchTitlesListener {

    private UseCase searchTitles;
    private UseCase searchBooks;

    private AutoCompleteTextView autoCompleteTextView;

    private ArrayAdapter<String> booksTitleListAdapter;
    private final static String TAG = MainActivity.class.getName();

    public BookTitleAutoCompleteTextView(Context context, final AutoCompleteTextView autoCompleteTextView) {
        this.searchTitles = new SearchTitles(this);
        this.autoCompleteTextView = autoCompleteTextView;

        final Filter nullFilter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.i("Filter", "Filter:" + constraint + " thread: " + Thread.currentThread());
                return null;
            }
        };
        booksTitleListAdapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line) {
            @Override
            public Filter getFilter() {
                return nullFilter;
            }
        };

        autoCompleteTextView.setAdapter(booksTitleListAdapter);
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
                BookTitleAutoCompleteTextView.this.searchTitles.execute(s.toString().trim());
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Log.i(TAG, "selected keyword = " + selection);
                BookTitleAutoCompleteTextView.this.searchBooks.execute(selection);
            }

        });

        autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String keyword = getText();
                    Log.i(TAG, "entered keyword = " + keyword);
                    autoCompleteTextView.dismissDropDown();
                    BookTitleAutoCompleteTextView.this.searchBooks.execute(keyword);
                }

                return true;
            }
        });
    }

    public String getText(){
        return autoCompleteTextView.getText().toString();
    }

    @Override
    public void searchBefore() {
        autoCompleteTextView.clearFocus();
    }

    @Override
    public void searchCompleted(List<String> titles) {
        Log.i("UPDATE", "3");
        booksTitleListAdapter.clear();
        booksTitleListAdapter.addAll(titles);
        booksTitleListAdapter.notifyDataSetChanged();
    }

    public void setSearchBooks(UseCase searchBooks){
        this.searchBooks = searchBooks;
    }


}
