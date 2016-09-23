package com.seoul.publicbooksearcher.domain;


import com.seoul.publicbooksearcher.data.KeywordRepository;
import com.seoul.publicbooksearcher.data.RecentSearchKeywordRepository;
import com.seoul.publicbooksearcher.domain.dto.AddRecentKeywordRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.AddRecentKeywordResponseDTO;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import rx.Observable;

@EBean
public class AddRecentKeywordUseCase extends com.seoul.publicbooksearcher.domain.UseCase<AddRecentKeywordRequestDTO> {

    @Bean(RecentSearchKeywordRepository.class)
    KeywordRepository keywordRepository;

    @Override
    protected Observable buildUseCaseObservable(AddRecentKeywordRequestDTO addRecentKeywordRequestDTO) {
        return Observable.create(subscriber -> {
            this.keywordRepository.insertKeyword(addRecentKeywordRequestDTO.getKeyword());
            subscriber.onNext(new AddRecentKeywordResponseDTO());
            subscriber.onCompleted();
        });
    }
}
