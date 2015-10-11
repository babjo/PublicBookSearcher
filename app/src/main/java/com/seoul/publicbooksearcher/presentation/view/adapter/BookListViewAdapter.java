package com.seoul.publicbooksearcher.presentation.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.seoul.publicbooksearcher.R;
import com.seoul.publicbooksearcher.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListViewAdapter extends BaseAdapter {

    private Context context = null;
    private List<Book> books = new ArrayList();

    public BookListViewAdapter(Context mContext) {
        super();
        this.context = mContext;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_listview_item_row, null);

            holder.library = (TextView) convertView.findViewById(R.id.book_library);
            holder.title = (TextView) convertView.findViewById(R.id.book_title);
            holder.status = (TextView) convertView.findViewById(R.id.book_status);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = books.get(position);

        holder.library.setText(book.getLibrary());
        holder.title.setText(book.getTitle());

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

        return convertView;
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

    public void AddAll(List<Book> books) {
        this.books.addAll(books);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView library;
        public TextView title;
        public TextView status;
    }
}
