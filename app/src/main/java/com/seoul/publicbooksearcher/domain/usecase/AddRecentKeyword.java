package com.seoul.publicbooksearcher.domain.usecase;


import com.seoul.publicbooksearcher.data.KeywordRepository;
import com.seoul.publicbooksearcher.data.RecentSearchKeywordRepository;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class AddRecentKeyword implements UseCase<Void, String> {

    @Bean(RecentSearchKeywordRepository.class)
    KeywordRepository keywordRepository;

    @Override
    public Void execute(String keyword) {
        this.keywordRepository.insertKeyword(keyword);
        return null;
    }
}
