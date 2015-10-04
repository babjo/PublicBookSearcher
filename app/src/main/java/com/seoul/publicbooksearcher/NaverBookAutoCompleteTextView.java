package com.seoul.publicbooksearcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

import java.util.List;

public class NaverBookAutoCompleteTextView implements UserRequestListener {

    private AutoCompleteTextView autoCompleteTextView;
    private UserRequester naver;
    private UserRequester library;

    private ArrayAdapter<String> booksAdapter;
    private final static String TAG = MainActivity.class.getName();

    public NaverBookAutoCompleteTextView(Context context, final UserRequester library, final AutoCompleteTextView autoCompleteTextView) {
        this.autoCompleteTextView = autoCompleteTextView;
        this.library = library;
        this.naver = new Naver(this);

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
        booksAdapter = new ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line) {

            @Override
            public Filter getFilter() {
                return nullFilter;
            }
        };

        autoCompleteTextView.setAdapter(booksAdapter);
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
        });
        booksAdapter.setNotifyOnChange(false);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Log.i(TAG, "selected keyword = " + selection);
                library.search(selection);
            }

        });
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
