package com.seoul.publicbooksearcher.view;

import com.seoul.publicbooksearcher.domain.models.Book;

import java.util.List;

/**
 * Created by LCH on 2016. 9. 23..
 */

public interface MainView {
    void onLoadRecentKeywords(List<String> keywords);
    void onPreSearchBooks(long libraryId);
    void onPostSearchBooks(Long libraryId, List<Book> books);
    void onErrorSearchBooks(Long libraryId, String message);

}
