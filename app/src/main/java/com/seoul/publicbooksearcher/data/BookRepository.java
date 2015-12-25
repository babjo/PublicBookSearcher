package com.seoul.publicbooksearcher.data;

import com.seoul.publicbooksearcher.domain.Book;

import java.util.List;

public interface BookRepository {

    List<Book> selectByKeywordAndLibraryId(String keyword, Long libraryId);
    void insertOrUpdateBooks(String keyword, Long libraryId, List<Book> books);

}
