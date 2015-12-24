package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.Library;

import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 24..
 */
@EBean
public class GuroLibraryBookCrawler extends BaseLibraryBookCrawler{

    public GuroLibraryBookCrawler() {
        libraryId = Library.GURO_LIB_ID;
    }
}
