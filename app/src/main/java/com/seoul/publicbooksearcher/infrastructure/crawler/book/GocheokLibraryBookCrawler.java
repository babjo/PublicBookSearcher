package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.Library;

import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 24..
 */
@EBean
public class GocheokLibraryBookCrawler extends BaseLibraryBookCrawler {

    public GocheokLibraryBookCrawler() {
        libraryId = Library.GOCHEOK_LIB_ID;
    }
}
