package com.seoul.publicbooksearcher.domain;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

public class Library implements ParentListItem{

    private String name;
    protected List<Book> books;

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

    public List<Book> getBooks() {
        return books;
    }
}
