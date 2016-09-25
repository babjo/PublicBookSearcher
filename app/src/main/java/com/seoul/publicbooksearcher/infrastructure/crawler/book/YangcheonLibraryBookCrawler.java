package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.models.Library;

import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 24..
 */

@EBean
public class YangcheonLibraryBookCrawler extends BaseLibraryBookCrawler {

    public YangcheonLibraryBookCrawler() {
        libraryId = Library.YANGCHEON_LIB_ID;
    }
}
