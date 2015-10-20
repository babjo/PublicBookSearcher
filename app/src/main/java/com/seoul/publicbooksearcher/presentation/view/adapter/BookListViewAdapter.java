package com.seoul.publicbooksearcher.presentation.view.adapter;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.domain.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListViewAdapter extends ExpandableRecyclerAdapter<BookListViewAdapter.BookParentViewHolder, BookListViewAdapter.BookChildViewHolder> {

    private LayoutInflater mInflater;
    private final static String TAG = BookListViewAdapter.class.getName();
    private static final boolean HONEYCOMB_AND_ABOVE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
    private List<BookListViewItem> bookListViewItemList;
    private Map<String, Integer> libraryPositionMap = new HashMap();


    public BookListViewAdapter(Context context, List<BookListViewItem> bookListViewItemList) {
        super(bookListViewItemList);
        mInflater = LayoutInflater.from(context);
        this.bookListViewItemList = bookListViewItemList;

        int position = 0;
        for(BookListViewItem bookListViewItem : bookListViewItemList) {
            libraryPositionMap.put(bookListViewItem.getLibrary(), position);
            position++;
        }
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
        BookListViewItem bookListViewItem = (BookListViewItem) parentListItem;
        bookParentViewHolder.library2.setText(bookListViewItem.getLibrary());

        switch (bookListViewItem.getState()){
            case BookListViewItem.PROGRESS_GONE:
                bookParentViewHolder.arrowUpImageView.setImageResource(R.mipmap.ic_keyboard_arrow_up_black_24dp);
                bookParentViewHolder.bookState1.setText("" + bookListViewItem.getPossibleLendSize());
                bookParentViewHolder.bookState2.setText("" + bookListViewItem.getImpossibleLendSize());
                bookParentViewHolder.bookState3.setText("" + bookListViewItem.getPossibleReserveSize());
                bookParentViewHolder.progressBar.setVisibility(View.GONE);
                bookParentViewHolder.resultLayout.setVisibility(View.VISIBLE);
                break;
            case BookListViewItem.PROGRESS_VISIBLE:
                bookParentViewHolder.resultLayout.setVisibility(View.GONE);
                bookParentViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
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
        public RelativeLayout progressBar;

        public LinearLayout resultLayout;
        public ImageView arrowUpImageView;
        public TextView bookState1;
        public TextView bookState2;
        public TextView bookState3;

        public BookParentViewHolder(View v) {
            super(v);
            library2 = (TextView) v.findViewById(R.id.book_library2);
            progressBar = (RelativeLayout) v.findViewById(R.id.progress_layout);
            resultLayout = (LinearLayout) v.findViewById(R.id.result_layout);
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


    public void updateItem(String library, List<Book> books) {
        int position = getPosition(library);
        if(position != -1){
            bookListViewItemList.get(position).clearAndArrange(books);
            notifyParentItemChanged(position);
        }
    }

    private int getPosition(String library){
        /*
        int i=0;
        for(BookListViewItem item : bookListViewItemList) {
            if (item.getLibrary().equals(library))
                return i;
            else
                i++;
        }
        return -1;*/
        return libraryPositionMap.get(library);
    }

    public void progressVisible(String library) {
        int position = getPosition(library);
        bookListViewItemList.get(position).setState(BookListViewItem.PROGRESS_VISIBLE);
        notifyParentItemChanged(position);
    }


    public void progressGone(String library) {
        int position = getPosition(library);
        bookListViewItemList.get(position).setState(BookListViewItem.PROGRESS_GONE);
        notifyParentItemChanged(position);
    }
}