package com.seoul.publicbooksearcher.presentation.view.adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.Library;
import com.seoul.publicbooksearcher.domain.Location;

import java.util.List;

public class BookListViewItem implements ParentListItem{

    private Library library;
    private int possibleLendSize;
    private int impossibleLendSize;
    private int possibleReserveSize;
    private int searchState;
    private int sortState;
    private double distance;

    public final static int SEARCH_COMPLETE = 1;
    public final static int SEARCH_BEFORE = 2;
    public final static int ERROR = 3;

    public final static int SORT_COMPLETE = 4;

    public BookListViewItem(Library library){
        this.library = library;
    }

    @Override
    public List<?> getChildItemList() {
        return library.getBooks();
    }
    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
    public String getLibraryName() {
        return library.getName();
    }

    private void arrangeAndAdd(Book book){
        if(book.getStatusCode() == 1){
            possibleLendSize++;
        }else if(book.getStatusCode() == 2){
            impossibleLendSize++;
        }else{
            possibleReserveSize++;
        }
        library.add(book);
    }

    public void arrange(List<Book> books) {
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
        this.library.clear();
        this.possibleLendSize=0;
        this.impossibleLendSize=0;
        this.possibleReserveSize=0;
    }

    public int getSearchState() {
        return searchState;
    }
    public void setSearchState(int searchState) {
        this.searchState = searchState;
    }
    public int getSortState() {
        return sortState;
    }
    public void setSortState(int sortState) {
        this.sortState = sortState;
    }

    public double getDistance() {
        return distance;
    }

    public void calcDistance(Location currentLocation) {
        distance = library.distance(currentLocation);
    }

    public int childSize() {
        return library.getBooks().size();
    }

    public int getLibraryIconColor() {
        return library.getColor();
    }
}
