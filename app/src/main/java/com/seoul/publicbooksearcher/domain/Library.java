package com.seoul.publicbooksearcher.domain;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class Library implements ParentListItem{

    private String name;
    private List<Book> books;
    private int possibleLendSize;
    private int impossibleLendSize;
    private int possibleReserveSize;

    public Library(String name){
        this.name = name;
        books = new ArrayList<Book>();
    }

    public Library(String name, List<Book> books) {
        this.name = name;
        this.books = books;
    }

    @Override
    public List<?> getChildItemList() {
        return books;
    }
    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
    public String getName() {
        return name;
    }
    public int bookCount() {
        return books.size();
    }

    public void addBook(Book book){
        if(book.getStatusCode() == 1){
            possibleLendSize++;
        }else if(book.getStatusCode() == 2){
            impossibleLendSize++;
        }else{
            possibleReserveSize++;
        }
        books.add(book);
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
}
