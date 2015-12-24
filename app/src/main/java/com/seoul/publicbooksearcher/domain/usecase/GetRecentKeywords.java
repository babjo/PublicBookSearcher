package com.seoul.publicbooksearcher.domain.usecase;

import com.seoul.publicbooksearcher.data.KeywordRepository;
import com.seoul.publicbooksearcher.data.RecentSearchKeywordRepository;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class GetRecentKeywords implements UseCase<List<String>, Void> {

    @Bean(RecentSearchKeywordRepository.class)
    KeywordRepository keywordRepository;

    @Override
    public List<String> execute(Void arg) {
        return this.keywordRepository.selectAll();
    }
}
