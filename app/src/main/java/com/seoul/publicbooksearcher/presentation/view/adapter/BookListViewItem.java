package com.seoul.publicbooksearcher.presentation.view.adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.seoul.publicbooksearcher.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListViewItem implements ParentListItem{

    private String library;
    private List<Book> books;
    private int possibleLendSize;
    private int impossibleLendSize;
    private int possibleReserveSize;
    private int state;

    public final static int PROGRESS_GONE = 1;
    public final static int PROGRESS_VISIBLE = 2;
    public final static int ERROR = 3;

    public BookListViewItem(String library){
        this.library = library;
        this.books = new ArrayList();
    }

    @Override
    public List<?> getChildItemList() {
        return books;
    }
    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
    public String getLibrary() {
        return library;
    }
    public int bookCount() {
        return books.size();
    }

    private void arrangeAndAdd(Book book){
        if(book.getStatusCode() == 1){
            possibleLendSize++;
        }else if(book.getStatusCode() == 2){
            impossibleLendSize++;
        }else{
            possibleReserveSize++;
        }
        books.add(book);
    }

    public void clearAndArrange(List<Book> books) {
        clearBooks();
        for(Book book : books)
            arrangeAndAdd(book);
    }

    public int getPossibleLendSize() {
        return possibleLendSize;
    }
    public int getImpossibleLendSize() {
        return impossibleLendSize;
    }
    public int getPossibleReserveSize() {
        return possibleReserveSize;
    }

    public void clearBooks() {
        this.books.clear();
        this.possibleLendSize=0;
        this.impossibleLendSize=0;
        this.possibleReserveSize=0;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }


}
