package com.seoul.publicbooksearcher.infrastructure.crawler.book;


import com.seoul.publicbooksearcher.domain.Library;

import org.androidannotations.annotations.EBean;

@EBean
public class NamsanLibraryBookCrawler extends BaseLibraryBookCrawler {

    public NamsanLibraryBookCrawler() {
        libraryId = Library.NAMSAN_LIB_ID;
    }

}
