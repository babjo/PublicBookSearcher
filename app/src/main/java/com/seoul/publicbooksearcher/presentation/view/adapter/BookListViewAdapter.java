package com.seoul.publicbooksearcher.presentation.view.adapter;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private final static String TAG = BookListViewAdapter.class.getName();
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;


    public BookListViewAdapter(Context context, List<Library> itemList) {
        super(itemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BookParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.book_listview_parent_item, viewGroup, false);
        final BookParentViewHolder bookParentViewHolder = new BookParentViewHolder(view);

        return bookParentViewHolder;
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
        bookParentViewHolder.arrowUpImageView.setImageResource(R.mipmap.ic_keyboard_arrow_up_black_24dp);
        bookParentViewHolder.bookState1.setText("" + library.getPossibleLendSize());
        bookParentViewHolder.bookState2.setText("" + library.getImpossibleLendSize());
        bookParentViewHolder.bookState3.setText("" + library.getPossibleReserveSize());
    }

    @Override
    public void onBindChildViewHolder(BookChildViewHolder bookChildViewHolder, int position, Object o) {
        Book book = (Book)o;

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
        public ImageView arrowUpImageView;

        public TextView bookState1;
        public TextView bookState2;
        public TextView bookState3;

        public BookParentViewHolder(View v) {
            super(v);
            library2 = (TextView) v.findViewById(R.id.book_library2);
            arrowUpImageView = (ImageView) v.findViewById(R.id.arrow);
            bookState1 = (TextView) v.findViewById(R.id.book_state_1);
            bookState2 = (TextView) v.findViewById(R.id.book_state_2);
            bookState3 = (TextView) v.findViewById(R.id.book_state_3);
        }

        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (!HONEYCOMB_AND_ABOVE) {return;}
            if (expanded) {
                arrowUpImageView.setRotation(ROTATED_POSITION);
            } else {
                arrowUpImageView.setRotation(INITIAL_POSITION);
            }
        }
    }

    public class BookChildViewHolder extends ChildViewHolder {
        public TextView title;
        public TextView status;
        public TextView location;
        public TextView callNumber;

        public BookChildViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.book_title);
            status = (TextView) v.findViewById(R.id.book_status);
            location = (TextView) v.findViewById(R.id.book_location);
            callNumber = (TextView) v.findViewById(R.id.book_callNumber);
        }
    }
}