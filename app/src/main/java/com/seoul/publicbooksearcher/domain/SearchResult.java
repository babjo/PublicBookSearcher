package com.seoul.publicbooksearcher.domain;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    private List<Library> libraries;

    public SearchResult() {
        this.libraries = new ArrayList();
    }

    public void addEmptyLibrary(String library){
        this.libraries.add(new Library(library));
    }

    public void addLibrary(Library library) {
        this.libraries.add(library);
    }

    /*
    public void addAllAndArrange(List<Book> books){
        for(Book book : books){
            Library library;
            if(!contains(book.getLibrary())) {
                library = new Library(book.getLibrary());
                libraries.add(library);
            }else {
                library = get(book.getLibrary());
            }
            library.addBook(book);
        }
    }

    private boolean contains(String target){
        for(Library library : libraries)
            if(library.getName().equals(target))
                return true;
        return false;
    }

    private Library get(String target){
        for(Library library : libraries)
            if(library.getName().equals(target))
                return library;
        return null;
    }
    */

}
