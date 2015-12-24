package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.Library;

import org.androidannotations.annotations.EBean;

@EBean
public class GangnamLibraryBookCrawler extends BaseLibraryBookCrawler{

    public GangnamLibraryBookCrawler() {
        libraryId = Library.GANGNAM_LIB_ID;
    }

}
