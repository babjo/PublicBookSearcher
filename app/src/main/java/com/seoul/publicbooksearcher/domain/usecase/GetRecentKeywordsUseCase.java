package com.seoul.publicbooksearcher.domain.usecase;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsRequestDTO;
import com.seoul.publicbooksearcher.domain.dto.GetRecentKeywordsResponseDTO;
import com.seoul.publicbooksearcher.domain.models.KeywordEntity;
import com.seoul.publicbooksearcher.infrastructure.Requery;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

import rx.Observable;

/**
 * Created by LCH on 2016. 9. 23..
 */

@EBean
public class GetRecentKeywordsUseCase extends UseCase<GetRecentKeywordsRequestDTO> {

    @Bean
    Requery mRequery;

    @Override
    protected Observable buildUseCaseObservable(GetRecentKeywordsRequestDTO getRecentKeywordsRequestDTO) {
        return Observable.create(subscriber -> {
            try {
                List<KeywordEntity> result = mRequery.getData().select(KeywordEntity.class).orderBy(KeywordEntity.CREATE_AT.desc()).get().toList();
                subscriber.onNext(new GetRecentKeywordsResponseDTO(Stream.of(result).map(keywordEntity -> keywordEntity.getValue()).collect(Collectors.toList())));
                subscriber.onCompleted();
            }catch (Exception e){
                subscriber.onError(e);
            }finally {
                subscriber.onCompleted();
            }
        });
    }
}
