package com.seoul.publicbooksearcher.presentation.view.component;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.domain.SearchTitles;
import com.seoul.publicbooksearcher.domain.UseCase;
import com.seoul.publicbooksearcher.presentation.listener.SearchTitlesListener;
import com.seoul.publicbooksearcher.presentation.view.activity.MainActivity;
import com.seoul.publicbooksearcher.presentation.view.adapter.BookTitleAutoCompleteTextViewAdapter;

import java.util.List;

public class BookTitleAutoCompleteTextView implements SearchTitlesListener {

    private Context context;
    private UseCase searchTitles;
    private UseCase searchBooks;

    private AutoCompleteTextView autoCompleteTextView;

    private BookTitleAutoCompleteTextViewAdapter BookTitleAutoCompleteTextViewAdapter;
    private final static String TAG = MainActivity.class.getName();

    public BookTitleAutoCompleteTextView(Context context, AutoCompleteTextView autoCompleteTextView) {
        this.searchTitles = new SearchTitles(this);
        this.autoCompleteTextView = autoCompleteTextView;
        this.context = context;

        this.BookTitleAutoCompleteTextViewAdapter =
                new BookTitleAutoCompleteTextViewAdapter(context, android.R.layout.simple_dropdown_item_1line);

        autoCompleteTextView.setAdapter(BookTitleAutoCompleteTextViewAdapter);
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
                    BookTitleAutoCompleteTextView.this.autoCompleteTextView.dismissDropDown();
                    BookTitleAutoCompleteTextView.this.searchBooks.execute(keyword);
                }

                return true;
            }
        });

        /*
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) ((Activity) BookTitleAutoCompleteTextView.this.context).findViewById(R.id.main_root_layout)).setGravity(Gravity.NO_GRAVITY);


                if(isTouch == false)
                    moveViewToScreenTop(BookTitleAutoCompleteTextView.this.autoCompleteTextView);
            }
        });*/
    }

    private boolean isTouch = false;
    private void moveViewToScreenTop(View view)  {

        final LinearLayout root = (LinearLayout) ((Activity) context).findViewById(R.id.main_root_layout);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2]; view.getLocationOnScreen( originalPos );

        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2) - statusBarOffset;

        int height = autoCompleteTextView.getMeasuredHeight();
        int amountToMove = -200;

        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 500 - originalPos[1]);
        anim.setDuration(1000);
        anim.setFillAfter(true);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                ((LinearLayout) ((Activity) context).findViewById(R.id.main_root_layout)).setGravity(Gravity.NO_GRAVITY);
                BookTitleAutoCompleteTextView.this.isTouch = true;
                // BookTitleAutoCompleteTextView.this.autoCompleteTextView.setVisibility(View.VISIBLE);
            }
        });

        view.startAnimation(anim);
    }

    public String getText() {
        return autoCompleteTextView.getText().toString();
    }
    public void dismissDropDown(){
        autoCompleteTextView.dismissDropDown();
    }

    @Override
    public void searchBefore() {
        autoCompleteTextView.clearFocus();
    }

    @Override
    public void searchCompleted(List<String> titles) {
        Log.i("UPDATE", "3");
        BookTitleAutoCompleteTextViewAdapter.clear();
        BookTitleAutoCompleteTextViewAdapter.addAll(titles);
        BookTitleAutoCompleteTextViewAdapter.notifyDataSetChanged();
    }

    public void setSearchBooks(UseCase searchBooks){
        this.searchBooks = searchBooks;
    }


}
