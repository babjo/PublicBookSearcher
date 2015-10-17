package com.seoul.publicbooksearcher.presentation.view.adapter;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListViewAdapter extends RecyclerView.Adapter<BookListViewAdapter.ViewHolder> {

    private List<Book> books = new ArrayList();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_listview_item_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = books.get(position);

        holder.library.setText(book.getLibrary());
        holder.title.setText(book.getTitle());
        holder.callNumber.setText(book.getCallNumber());
        holder.location.setText(book.getLocation());

        switch (book.getStatusCode()){
            case 1:
                holder.status.setBackgroundResource(R.color.flatGreen);
                break;
            case 2:
                holder.status.setBackgroundResource(R.color.flatRed);
                break;
            case 3:
                holder.status.setBackgroundResource(R.color.flatYellow);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView library;
        public TextView title;
        public TextView status;
        public TextView location;
        public TextView callNumber;

        public ViewHolder(View v) {
            super(v);
            library = (TextView) v.findViewById(R.id.book_library);
            title = (TextView) v.findViewById(R.id.book_title);
            status = (TextView) v.findViewById(R.id.book_status);
            location = (TextView) v.findViewById(R.id.book_location);
            callNumber = (TextView) v.findViewById(R.id.book_callNumber);
        }
    }

    public void clearAndAddAll(List<Book> books){
        this.books.clear();
        this.books.addAll(books);
        notifyDataSetChanged();
    }

    public void clear() {
        this.books.clear();
        notifyDataSetChanged();
    }

    public List<Book> getItems(){
        return books;
    }

    public void setItems(List<Book> books) {
        clearAndAddAll(books);
    }

    public void addAll(List<Book> books) {
        this.books.addAll(books);
        notifyDataSetChanged();
    }
}
