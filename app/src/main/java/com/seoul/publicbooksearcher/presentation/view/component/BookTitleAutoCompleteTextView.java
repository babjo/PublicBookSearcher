package com.seoul.publicbooksearcher.presentation.view.component;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.seoul.publicbooksearcher.data.RecentSearchKeywordRepository;
import com.seoul.publicbooksearcher.data.open_api.NaverBookOpenApi;
import com.seoul.publicbooksearcher.domain.AddRecentKeyword;
import com.seoul.publicbooksearcher.domain.GetRecentKeywords;
import com.seoul.publicbooksearcher.domain.SearchTitles;
import com.seoul.publicbooksearcher.domain.UseCase;
import com.seoul.publicbooksearcher.presentation.UseCaseListener;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookTitleAutoCompleteTextViewAdapter;

import java.util.List;

public class BookTitleAutoCompleteTextView implements UseCaseListener<Void, List<String>> {

    private final AddRecentKeyword addRecentKeyword;
    private final GetRecentKeywords getRecentKeywords;

    private Context context;
    private UseCase searchTitles;
    private UseCase searchBooks;

    private AutoCompleteTextView autoCompleteTextView;

    private BookTitleAutoCompleteTextViewAdapter bookTitleAutoCompleteTextViewAdapter;
    private final static String TAG = BookTitleAutoCompleteTextView.class.getName();

    public BookTitleAutoCompleteTextView(Context context, final AutoCompleteTextView autoCompleteTextView) {
        this.searchTitles = new SearchTitles(this, new NaverBookOpenApi());

        RecentSearchKeywordRepository keywordRepository = new RecentSearchKeywordRepository(context);
        this.addRecentKeyword = new AddRecentKeyword(keywordRepository);
        this.getRecentKeywords = new GetRecentKeywords(keywordRepository);
        
        this.autoCompleteTextView = autoCompleteTextView;
        this.context = context;

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
                    getRecentKeywords();
                }
                else
                    BookTitleAutoCompleteTextView.this.searchTitles.execute(s.toString().trim());
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                search(selection);
            }

        });

        autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && !"".equals(getText().trim())) {
                    String keyword = getText();
                    search(keyword);
                    return true;
                }

                return false;
            }
        });

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getRecentKeywords();
                }
            }
        });
    }

    private void getRecentKeywords() {
        final List<String> keywords = BookTitleAutoCompleteTextView.this.getRecentKeywords.execute(null);
        String keywordContents = "";
        for (String keyword : keywords)
            keywordContents += keyword + ", ";
        Log.i(TAG, "Recent Keyword : " + keywordContents);

        new Handler().postDelayed(new Runnable() { // new Handler and Runnable
            @Override
            public void run() {
                setTitles(keywords);
            }
        }, 500);
    }

    private void search(String keyword) {
        Log.i(TAG, "entered keyword = " + keyword + "\n search start");
        addRecentKeyword.execute(keyword);
        Log.i(TAG, "addRecentKeyword = " + keyword);
        autoCompleteTextView.dismissDropDown();
        searchBooks.execute(keyword);
    }

    private void setTitles(List<String> titles){
        this.bookTitleAutoCompleteTextViewAdapter.setTitles(titles);
    }

    public String getText() {
        return autoCompleteTextView.getText().toString();
    }

    public void setSearchBooks(UseCase searchBooks){
        this.searchBooks = searchBooks;
    }

    @Override
    public void executeBefore(Void beforeArgs) {
        autoCompleteTextView.clearFocus();
    }

    @Override
    public void executeAfter(List<String> titles) {
        Log.i("UPDATE", "3");
        setTitles(titles);
    }

    @Override
    public void error(Exception e) {

    }
}
