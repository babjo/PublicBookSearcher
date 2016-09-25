package com.seoul.publicbooksearcher.infrastructure.crawler.book;

import com.seoul.publicbooksearcher.domain.models.Library;

import org.androidannotations.annotations.EBean;

/**
 * Created by LCH on 2015. 12. 24..
 */
@EBean
public class GangseoLibraryBookCrawler extends BaseLibraryBookCrawler{

    public GangseoLibraryBookCrawler() {
        libraryId = Library.GANGSEO_LIB_ID;
    }
}
