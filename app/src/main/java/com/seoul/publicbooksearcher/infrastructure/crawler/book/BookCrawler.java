package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.models.Book;

import java.util.List;

public interface BookCrawler {
    List<Book> crawling(String keyword);
    Long getLibraryId();
}
