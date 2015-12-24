package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.Library;

import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 24..
 */
@EBean
public class JongRoLibraryBookCrawler extends BaseLibraryBookCrawler {

    public JongRoLibraryBookCrawler() {
        libraryId = Library.JONGRO_LIB_ID;
    }
}
