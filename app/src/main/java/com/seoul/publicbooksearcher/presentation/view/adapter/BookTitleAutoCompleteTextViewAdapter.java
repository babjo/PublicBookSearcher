package com.seoul.publicbooksearcher.presentation.view.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

/**
 * Created by LCH on 2015. 10. 9..
 */
public class BookTitleAutoCompleteTextViewAdapter extends ArrayAdapter {

    private final static Filter NULL_FILTER = new Filter() {
        @Override
        protected void publishResults (CharSequence constraint, Filter.FilterResults results){
        }
        @Override
        protected Filter.FilterResults performFiltering (CharSequence constraint){
            Log.i("Filter", "Filter:" + constraint + " thread: " + Thread.currentThread());
            return null;
        }
    };


    public BookTitleAutoCompleteTextViewAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public Filter getFilter() {
        return NULL_FILTER;
    }
}
