package com.seoul.publicbooksearcher.presentation.view;

import com.seoul.publicbooksearcher.domain.Book;
import com.seoul.publicbooksearcher.domain.Location;

import java.util.List;

/**
 * Created by LCH on 2016. 9. 23..
 */

public interface MainView {
    void onLoadRecentKeywords(List<String> keywords);

    void onPreSearchBooks(long libraryId);
    void onPostSearchBooks(Long libraryId, List<Book> books);
    void onErrorSearchBooks(Long libraryId, String message);

    void onPreSortLibrariesByDistance();
    void onPostSortLibrariesByDistance(Location location);
    void onErrorSortLibrariesByDistance();
}
