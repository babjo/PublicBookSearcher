package com.seoul.publicbooksearcher.presentation.view.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.seoul.publicbooksearcher.presentation.presenter.BookPresenter;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookTitleAutoCompleteTextViewAdapter;

import java.util.List;

public class BookTitleAutoCompleteTextView {

    private BookPresenter bookPresenter;
    private final AutoCompleteTextView autoCompleteTextView;

    private BookTitleAutoCompleteTextViewAdapter bookTitleAutoCompleteTextViewAdapter;
    private final static String TAG = BookTitleAutoCompleteTextView.class.getName();

    public BookTitleAutoCompleteTextView(Context context, final AutoCompleteTextView autoCompleteTextView) {

        this.autoCompleteTextView = autoCompleteTextView;
        this.bookTitleAutoCompleteTextViewAdapter = new BookTitleAutoCompleteTextViewAdapter(context, android.R.layout.simple_dropdown_item_1line);

        autoCompleteTextView.setAdapter(bookTitleAutoCompleteTextViewAdapter);
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
                if (s.toString().equals("")) {
                    Log.i(TAG, "===========afterTextChanged getRecentKeywords=============");
                    bookPresenter.getRecentKeywords();
                } else
                    bookPresenter.searchTitles(s.toString().trim());
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                bookPresenter.searchBooks(selection);
            }

        });

        autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && !"".equals(getText().trim())) {
                    String keyword = getText();
                    bookPresenter.searchBooks(keyword);
                    return true;
                }

                return false;
            }
        });

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    bookPresenter.getRecentKeywords();
                }
            }
        });
    }

    public void setTitles(List<String> titles){ this.bookTitleAutoCompleteTextViewAdapter.setTitles(titles); }
    public void dismissDropDown() { autoCompleteTextView.dismissDropDown(); }
    public void clearFocus(){ autoCompleteTextView.clearFocus(); }
    private String getText() {
        return autoCompleteTextView.getText().toString();
    }
    public void setBookPresenter(BookPresenter bookPresenter) {this.bookPresenter = bookPresenter;}
}
