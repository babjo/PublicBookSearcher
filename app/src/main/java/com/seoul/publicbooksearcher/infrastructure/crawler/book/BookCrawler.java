package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.Book;

import java.util.List;

public interface BookCrawler {
    List<Book> crawling(String keyword);
    Long getLibraryId();
}
