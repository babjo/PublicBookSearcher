package com.seoul.publicbooksearcher.domain;

import java.util.ArrayList;
import java.util.List;

public class LibraryList {

    private List<Library> libraries;

    public LibraryList() {
        this.libraries = new ArrayList();
    }

    public void add(Library library){
        this.libraries.add(library);
    }

    public List<Library> array() {
        return libraries;
    }

    public boolean isEmpty() {
        return libraries.isEmpty();
    }
}
