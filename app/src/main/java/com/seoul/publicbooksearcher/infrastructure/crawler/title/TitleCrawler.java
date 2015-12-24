package com.seoul.publicbooksearcher.infrastructure.crawler.title;

import java.util.List;

/**
 * Created by LCH on 2015. 12. 24..
 */
public interface TitleCrawler {
    List<String> crawling(String keyword);
}
