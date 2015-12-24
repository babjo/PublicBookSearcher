package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.Library;

import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 24..
 */
@EBean
public class DobongLibraryBookCrawler extends BaseLibraryBookCrawler{

    public DobongLibraryBookCrawler() {
        libraryId = Library.DOBONG_LIB_ID;
    }
}
