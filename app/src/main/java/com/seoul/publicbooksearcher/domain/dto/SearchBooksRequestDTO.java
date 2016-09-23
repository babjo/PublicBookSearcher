package com.seoul.publicbooksearcher.domain.dto;

import com.seoul.publicbooksearcher.infrastructure.crawler.book.BookCrawler;

/**
 * Created by LCH on 2016. 9. 23..
 */
public class SearchBooksRequestDTO {
    private String keyword;
    private BookCrawler bookCrawler;

    public SearchBooksRequestDTO(String keyword, BookCrawler bookCrawler) {
        this.keyword = keyword;
        this.bookCrawler = bookCrawler;
    }

    public String getKeyword() {
        return keyword;
    }

    public BookCrawler getBookCrawler() {
        return bookCrawler;
    }
}
