package com.seoul.publicbooksearcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private LayoutInflater layoutInflater;
    private List<Book> books;

    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Book)resultValue).getTitle();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<Book> suggestions = new ArrayList();
                for (Book book : books) {
                    // Note: change the "contains" to "startsWith" if you only want starting matches
                    if (book.getTitle().contains(constraint.toString())) {
                        suggestions.add(book);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<Book>) results.values);
            } else {
                // no filter, add entire original list back in
                addAll(books);
            }
            notifyDataSetChanged();
        }
    };

    public BookAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.books = new ArrayList();
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_item_row, null);
        }

        Book customer = getItem(position);

        TextView name = (TextView) view.findViewById(R.id.book_title_tv);
        name.setText(customer.getTitle());

        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void clearAndAddAll(List<Book> books) {
        this.books.clear();
        this.books.addAll(books);
        notifyDataSetChanged();
    }
}
