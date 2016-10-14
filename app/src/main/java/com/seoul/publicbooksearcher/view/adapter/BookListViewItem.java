package com.seoul.publicbooksearcher.view.adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.seoul.publicbooksearcher.domain.models.Book;
import com.seoul.publicbooksearcher.domain.models.Library;
import com.seoul.publicbooksearcher.domain.models.Location;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookListViewItem implements ParentListItem{

    private Library library;
    private int loanPossibleSize;
    private int loanImpossibleSize;
    private int loaningSize;
    private int searchState;
    private int sortState;
    private double distance;

    public final static int SEARCH_COMPLETE = 1;
    public final static int SEARCH_BEFORE = 2;
    public final static int ERROR = 3;

    public final static int SORT_COMPLETE = 4;
    private Long libraryId;

    public BookListViewItem(Library library){
        this.library = library;
    }

    @Override
    public List<?> getChildItemList() {
        Collections.sort(library.getBooks(), new StateCompare());
        return library.getBooks();
    }

    private class StateCompare implements Comparator<Book>{
        @Override
        public int compare(Book lhs, Book rhs) {
            return lhs.getStatusCode() < rhs.getStatusCode() ? -1 : lhs.getStatusCode() > rhs.getStatusCode() ? 1:0;
        }
    }



    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
    public String getLibraryName() {
        return library.getName();
    }

    private void arrangeAndAdd(Book book){
        if(book.getStatusCode() == Book.BOOK_STATE_LOAN_POSSIBLE){
            loanPossibleSize++;
        }else if(book.getStatusCode() == Book.BOOK_STATE_LOAN_IMPOSSIBLE){
            loanImpossibleSize++;
        }else{
            loaningSize++;
        }
        library.add(book);
    }

    public void arrange(List<Book> books) {
        for(Book book : books)
            arrangeAndAdd(book);
    }

    public int getLoanPossibleSize() {
        return loanPossibleSize;
    }
    public int getLoanImpossibleSize() {
        return loanImpossibleSize;
    }
    public int getLoaningSize() {
        return loaningSize;
    }

    public void clearBooks() {
        this.library.clear();
        this.loanPossibleSize =0;
        this.loanImpossibleSize =0;
        this.loaningSize =0;
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

    public boolean noChild(){
        return childSize() == 0;
    }

    public Long getLibraryId() {
        return library.getId();
    }
}
