package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.Library;

import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 24..
 */
@EBean
public class GodeokLifelongLearningCenterBookCrawler extends BaseLibraryBookCrawler {

    public GodeokLifelongLearningCenterBookCrawler() {
        libraryId = Library.GODEOK_LEARNING_CENTER_ID;
    }
}
