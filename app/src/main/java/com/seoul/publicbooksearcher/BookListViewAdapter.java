package com.seoul.publicbooksearcher;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListViewAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<Book> books = new ArrayList();

    public BookListViewAdapter(Context mContext) {
        super();
        this.mContext = mContext;
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

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                holder.status.setBackgroundColor(0x2ecc71);
                break;
            case 2:
                holder.status.setBackgroundColor(0xe74c3c);
                break;
            case 3:
                holder.status.setBackgroundColor(0xf1c40f);
                break;
        }

        return convertView;
    }

    public void clearAndAddAll(List<Book> books){
        this.books.clear();
        books.addAll(books);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView library;
        public TextView title;
        public TextView status;
    }
}
