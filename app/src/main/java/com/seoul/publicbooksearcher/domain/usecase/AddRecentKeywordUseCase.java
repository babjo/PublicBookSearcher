package com.seoul.publicbooksearcher.domain.usecase;


import com.seoul.publicbooksearcher.domain.dto.AddRecentKeywordRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.AddRecentKeywordResponseDTO;
import com.seoul.publicbooksearcher.domain.models.KeywordEntity;
import com.seoul.publicbooksearcher.infrastructure.Requery;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.Date;

import rx.Observable;

@EBean
public class AddRecentKeywordUseCase extends UseCase<AddRecentKeywordRequestDTO> {

    @Bean
    Requery mRequery;

    @Override
    protected Observable buildUseCaseObservable(AddRecentKeywordRequestDTO addRecentKeywordRequestDTO) {
        return Observable.create(subscriber -> {
            KeywordEntity keywordEntity = new KeywordEntity();
            keywordEntity.setValue(addRecentKeywordRequestDTO.getKeyword());
            keywordEntity.setCreateAt(new Date());
            mRequery.getData().insert(keywordEntity);
            subscriber.onNext(new AddRecentKeywordResponseDTO());
            subscriber.onCompleted();
        });
    }
}
