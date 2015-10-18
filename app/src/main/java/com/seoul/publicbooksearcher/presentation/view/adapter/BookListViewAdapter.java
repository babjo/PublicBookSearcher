package com.seoul.publicbooksearcher.presentation.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.Library;

import java.util.List;

public class BookListViewAdapter extends ExpandableRecyclerAdapter<BookListViewAdapter.BookParentViewHolder, BookListViewAdapter.BookChildViewHolder> {

    private LayoutInflater mInflater;

    public BookListViewAdapter(Context context, List<Library> itemList) {
        super(itemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BookParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.book_listview_parent_item, viewGroup, false);
        return new BookParentViewHolder(view);
    }

    @Override
    public BookChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.book_listview_child_item, viewGroup, false);
        return new BookChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(BookParentViewHolder bookParentViewHolder, int i, ParentListItem parentListItem) {
        Library library = (Library) parentListItem;
        bookParentViewHolder.library2.setText(library.getName());
    }

    @Override
    public void onBindChildViewHolder(BookChildViewHolder bookChildViewHolder, int position, Object o) {
        Book book = (Book)o;

        bookChildViewHolder.library.setText(book.getLibrary());
        bookChildViewHolder.title.setText(book.getTitle());
        bookChildViewHolder.callNumber.setText(book.getCallNumber());
        bookChildViewHolder.location.setText(book.getLocation());

        switch (book.getStatusCode()){
            case 1:
                bookChildViewHolder.status.setBackgroundResource(R.color.flatGreen);
                break;
            case 2:
                bookChildViewHolder.status.setBackgroundResource(R.color.flatRed);
                break;
            case 3:
                bookChildViewHolder.status.setBackgroundResource(R.color.flatYellow);
                break;
        }
    }

    public class BookParentViewHolder extends ParentViewHolder {
        public TextView library2;

        public BookParentViewHolder(View v) {
            super(v);
            library2 = (TextView) v.findViewById(R.id.book_library2);
        }
    }

    public class BookChildViewHolder extends ChildViewHolder {
        public TextView library;
        public TextView title;
        public TextView status;
        public TextView location;
        public TextView callNumber;

        public BookChildViewHolder(View v) {
            super(v);
            library = (TextView) v.findViewById(R.id.book_library);
            title = (TextView) v.findViewById(R.id.book_title);
            status = (TextView) v.findViewById(R.id.book_status);
            location = (TextView) v.findViewById(R.id.book_location);
            callNumber = (TextView) v.findViewById(R.id.book_callNumber);
        }
    }
}
